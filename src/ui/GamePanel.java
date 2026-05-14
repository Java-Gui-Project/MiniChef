package ui;

import game.GameManager;
import model.Ingredient;
import model.Recipe;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

import static ui.Theme.F_BOLD;

/**
 * หน้าจอเล่นเกมหลัก
 * แสดงชื่อเมนู กริดวัตถุดิบ (ของจริงปนของหลอก) ถาดวัตถุดิบที่เลือก นาฬิกาจับเวลา และปุ่มส่งอาหาร
 * เรียก refresh() ทุกครั้งก่อนแสดงหน้านี้เพื่อโหลดเมนูและเริ่มจับเวลาใหม่
 */
public class GamePanel extends JPanel {

    /** GameManager สำหรับดึงข้อมูลเมนู ส่งคำตอบ และจัดการเวลา */
    private final GameManager gm;

    /** Navigator สำหรับเปลี่ยนไปหน้าสรุปผลเมื่อเกมจบ */
    private final Navigator nav;

    /** Label แสดงชื่อเมนูที่ต้องทำ */
    private JLabel lblDish;

    /** Label แสดงเวลาที่เหลือ */
    private JLabel lblTimer;

    /** Label แสดงคะแนนสะสม */
    private JLabel lblScore;

    /** Label แสดงผลลัพธ์หลังกดส่ง (ถูก/ผิด) */
    private JLabel lblFeedback;

    /** Panel กริดแสดงปุ่มวัตถุดิบทั้งหมด */
    private JPanel pIngr;

    /** Panel ถาดแสดงวัตถุดิบที่ผู้เล่นเลือกไว้ */
    private JPanel pSel;

    /** รายชื่อวัตถุดิบที่ผู้เล่นเลือกในรอบปัจจุบัน */
    private List<String> picked = new ArrayList<>();

    /** Timer ที่นับถอยหลังทุก 1 วินาที */
    private javax.swing.Timer gameTimer;

    /**
     * สร้างหน้าเล่นเกมและ build UI ทันที
     * @param gm  GameManager ที่ควบคุมสถานะเกม
     * @param nav Navigator สำหรับเปลี่ยนหน้าจอ
     */
    public GamePanel(GameManager gm, Navigator nav) {
        this.gm  = gm;
        this.nav = nav;
        setName("GAME");
        buildUI();
    }

    /** สร้าง Layout และ Widget ทั้งหมดของหน้าเล่นเกม */
    private void buildUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Theme.BG);
        setBorder(Theme.pad(15, 15, 15, 15));

        // แถบบน: คะแนน | ชื่อเมนู | เวลา
        lblScore = Theme.bold(" คะแนน: 0 ");
        lblDish  = new JLabel("ชื่อเมนู", SwingConstants.CENTER);
        lblDish.setFont(F_BOLD);
        lblTimer = new JLabel("⏱ 00 ", SwingConstants.RIGHT);
        lblTimer.setFont(F_BOLD);

        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(Color.WHITE);
        bar.setBorder(Theme.roundBorder(Theme.ORANGE, 2));
        bar.add(lblScore, BorderLayout.WEST);
        bar.add(lblDish,  BorderLayout.CENTER);
        bar.add(lblTimer, BorderLayout.EAST);

        // กริดวัตถุดิบ (4 คอลัมน์ เลื่อนได้)
        pIngr = new JPanel(new GridLayout(0, 4, 10, 10));
        pIngr.setBackground(Theme.BG);
        JScrollPane scroll = new JScrollPane(pIngr);
        scroll.setBorder(BorderFactory.createTitledBorder(" เลือกวัตถุดิบที่ถูกต้อง "));

        // ถาดวัตถุดิบที่เลือก
        pSel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pSel.setPreferredSize(new Dimension(0, 80));
        pSel.setBackground(new Color(0xFF, 0xFB, 0xF0));
        pSel.setBorder(BorderFactory.createTitledBorder(" ถาดวัตถุดิบของคุณ "));

        lblFeedback = new JLabel("เตรียมพร้อม...", SwingConstants.CENTER);

        // ปุ่มส่งอาหาร
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

    /**
     * โหลดเมนูปัจจุบันและเริ่มจับเวลาใหม่
     * ถูกเรียกโดย Navigator ทุกครั้งก่อนแสดงหน้านี้
     */
    public void refresh() {
        loadRecipe();
        startTimer();
    }

    /**
     * โหลดวัตถุดิบของเมนูปัจจุบันลงในกริด
     * ผสมวัตถุดิบจริงกับของหลอกแล้ว Shuffle สุ่มลำดับ
     */
    private void loadRecipe() {
        Recipe r = gm.current();
        if (r == null) return;

        lblDish.setText(r.dish);
        lblScore.setText(" คะแนน: " + gm.chef.score);
        picked.clear();
        updateTray();
        pIngr.removeAll();

        // รวมวัตถุดิบจริง + ตัวลวงตามจำนวนที่กำหนดโดย difficulty
        List<Ingredient> pool = new ArrayList<>(r.ingredients);
        for (int i = 0; i < r.decoys() && i < Theme.DECOY_POOL.length; i++) {
            pool.add(new Ingredient(Theme.DECOY_POOL[i][0], Theme.DECOY_POOL[i][1]));
        }
        Collections.shuffle(pool);

        // สร้างปุ่มสำหรับวัตถุดิบแต่ละชนิด (Toggle เลือก/ยกเลิก)
        for (Ingredient ing : pool) {
            JButton btn = new JButton("<html><center>" + ing.emoji + "<br>" + ing.name + "</center></html>");
            btn.setOpaque(true);
            btn.setBackground(Color.WHITE);
            btn.addActionListener(e -> {
                if (picked.contains(ing.name)) {
                    picked.remove(ing.name);          // กดซ้ำ = ยกเลิกการเลือก
                    btn.setBackground(Color.WHITE);
                } else {
                    picked.add(ing.name);             // กดครั้งแรก = เลือก
                    btn.setBackground(new Color(0x90, 0xEE, 0x90));
                }
                updateTray();
            });
            pIngr.add(btn);
        }

        pIngr.revalidate();
        pIngr.repaint();
    }

    /** อัปเดตถาดวัตถุดิบให้แสดงรายชื่อที่ผู้เล่นเลือกไว้ล่าสุด */
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

    /**
     * ส่งคำตอบให้ GameManager ตรวจสอบ
     * ถ้าถูก: แสดงผลสำเร็จ หน่วง 1.5 วินาที แล้วเปลี่ยนเมนูหรือจบเกม
     * ถ้าผิด: แสดงข้อความผิดพลาดและสั่นถาด
     */
    private void submitDish() {
        if (picked.isEmpty()) { Theme.shake(pSel); return; } // ไม่เลือกอะไรเลย

        int earned = gm.submit(new ArrayList<>(picked));
        if (earned > 0) {
            lblFeedback.setText("ถูกต้อง! ได้รับ " + earned + " แต้ม");
            lblFeedback.setForeground(Theme.GREEN);
            if (gameTimer != null) gameTimer.stop(); // หยุด timer ระหว่างรอ

            Theme.delay(1500, () -> {
                if (gm.hasNext()) {
                    loadRecipe();   // โหลดเมนูถัดไป
                    startTimer();   // เริ่มนับเวลาใหม่
                } else {
                    gm.save();
                    nav.showResult(); // ทำครบทุกเมนู → ไปหน้าสรุปผล
                }
            });
        } else {
            lblFeedback.setText("วัตถุดิบไม่ถูกต้อง ลองใหม่!");
            lblFeedback.setForeground(Theme.RED);
            Theme.shake(pSel);
        }
    }

    /**
     * เริ่มนาฬิกาจับเวลาถอยหลังทุก 1 วินาที
     * เมื่อเวลาหมด: ข้ามเมนูปัจจุบันและโหลดเมนูถัดไปหรือจบเกม
     */
    private void startTimer() {
        if (gameTimer != null) gameTimer.stop(); // หยุด timer เก่าก่อนเสมอ
        gameTimer = new javax.swing.Timer(1000, e -> {
            gm.decrementTime();
            lblTimer.setText("⏱ " + gm.getTime() + " ");
            if (gm.getTime() <= 0) {
                gameTimer.stop();
                gm.skip(); // เวลาหมด = ข้ามเมนูโดยไม่ได้คะแนน
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
