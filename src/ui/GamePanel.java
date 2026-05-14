package ui;

import game.GameManager;
import model.Ingredient;
import model.Recipe;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/** Active gameplay screen: ingredient selection, tray, timer, and submission. */
public class GamePanel extends JPanel {

    private final GameManager     gm;
    private final Navigator       nav;

    private JLabel                lblDish, lblTimer, lblScore, lblFeedback;
    private JPanel                pIngr, pSel;
    private List<String>          picked    = new ArrayList<>();
    private javax.swing.Timer     gameTimer;

    public GamePanel(GameManager gm, Navigator nav) {
        this.gm  = gm;
        this.nav = nav;
        setName("GAME");
        buildUI();
    }

    private void buildUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Theme.BG);
        setBorder(Theme.pad(15, 15, 15, 15));

        // Top bar: score | dish name | timer
        lblScore = Theme.bold(" คะแนน: 0 ");
        lblDish  = new JLabel("ชื่อเมนู", SwingConstants.CENTER);
        lblDish.setFont(new Font("Tahoma", Font.BOLD, 22));
        lblTimer = new JLabel("⏱ 00 ", SwingConstants.RIGHT);
        lblTimer.setFont(new Font("Tahoma", Font.BOLD, 22));

        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(Color.WHITE);
        bar.setBorder(Theme.roundBorder(Theme.ORANGE, 2));
        bar.add(lblScore, BorderLayout.WEST);
        bar.add(lblDish,  BorderLayout.CENTER);
        bar.add(lblTimer, BorderLayout.EAST);

        // Ingredient grid
        pIngr = new JPanel(new GridLayout(0, 4, 10, 10));
        pIngr.setBackground(Theme.BG);
        JScrollPane scroll = new JScrollPane(pIngr);
        scroll.setBorder(BorderFactory.createTitledBorder(" เลือกวัตถุดิบที่ถูกต้อง "));

        // Selection tray
        pSel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pSel.setPreferredSize(new Dimension(0, 80));
        pSel.setBackground(new Color(0xFF, 0xFB, 0xF0));
        pSel.setBorder(BorderFactory.createTitledBorder(" ถาดวัตถุดิบของคุณ "));

        lblFeedback = new JLabel("เตรียมพร้อม...", SwingConstants.CENTER);

        JButton bSubmit = Theme.pill("🍳 ส่งอาหาร", Theme.GREEN);
        bSubmit.addActionListener(e -> submitDish());

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        bottom.add(pSel,        BorderLayout.NORTH);
        bottom.add(lblFeedback, BorderLayout.CENTER);
        bottom.add(bSubmit,     BorderLayout.SOUTH);

        add(bar,    BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }

    /** Called by the navigator before showing this panel. */
    public void refresh() {
        loadRecipe();
        startTimer();
    }

    private void loadRecipe() {
        Recipe r = gm.current();
        if (r == null) return;

        lblDish.setText(r.dish);
        lblScore.setText(" คะแนน: " + gm.chef.score);
        picked.clear();
        updateTray();
        pIngr.removeAll();

        List<Ingredient> pool = new ArrayList<>(r.ingredients);
        for (int i = 0; i < r.decoys() && i < Theme.DECOY_POOL.length; i++) {
            pool.add(new Ingredient(Theme.DECOY_POOL[i][0], Theme.DECOY_POOL[i][1]));
        }
        Collections.shuffle(pool);

        for (Ingredient ing : pool) {
            JButton btn = new JButton("<html><center>" + ing.emoji + "<br>" + ing.name + "</center></html>");
            btn.setOpaque(true);
            btn.setBackground(Color.WHITE);
            btn.addActionListener(e -> {
                if (picked.contains(ing.name)) {
                    picked.remove(ing.name);
                    btn.setBackground(Color.WHITE);
                } else {
                    picked.add(ing.name);
                    btn.setBackground(new Color(0x90, 0xEE, 0x90));
                }
                updateTray();
            });
            pIngr.add(btn);
        }

        pIngr.revalidate();
        pIngr.repaint();
    }

    private void updateTray() {
        pSel.removeAll();
        for (String name : picked) {
            JLabel item = new JLabel(name + " ");
            item.setFont(Theme.F_PLAIN);
            pSel.add(item);
        }
        pSel.revalidate();
        pSel.repaint();
    }

    private void submitDish() {
        if (picked.isEmpty()) { Theme.shake(pSel); return; }

        int earned = gm.submit(new ArrayList<>(picked));
        if (earned > 0) {
            lblFeedback.setText("ถูกต้อง! ได้รับ " + earned + " แต้ม");
            lblFeedback.setForeground(Theme.GREEN);
            if (gameTimer != null) gameTimer.stop();

            Theme.delay(1500, () -> {
                if (gm.hasNext()) {
                    loadRecipe();
                    startTimer();
                } else {
                    gm.save();
                    nav.showResult();
                }
            });
        } else {
            lblFeedback.setText("วัตถุดิบไม่ถูกต้อง ลองใหม่!");
            lblFeedback.setForeground(Theme.RED);
            Theme.shake(pSel);
        }
    }

    private void startTimer() {
        if (gameTimer != null) gameTimer.stop();
        gameTimer = new javax.swing.Timer(1000, e -> {
            gm.decrementTime();
            lblTimer.setText("⏱ " + gm.getTime() + " ");
            if (gm.getTime() <= 0) {
                gameTimer.stop();
                gm.skip();
                if (gm.hasNext()) {
                    loadRecipe();
                    startTimer();
                } else {
                    gm.save();
                    nav.showResult();
                }
            }
        });
        gameTimer.start();
    }
}
