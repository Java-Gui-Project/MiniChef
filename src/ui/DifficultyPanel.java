package ui;

import game.GameManager;
import model.Recipe;

import javax.swing.*;
import java.awt.*;

/** Difficulty selection screen. Call setChefName() before showing this panel. */
public class DifficultyPanel extends JPanel {

    private String chefName = "";

    public DifficultyPanel(GameManager gm, Navigator nav) {
        setName("DIFF");
        setBackground(Theme.BG);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("เลือกระดับความยาก", SwingConstants.CENTER);
        title.setFont(new Font("Tahoma", Font.BOLD, 26));
        title.setBorder(Theme.pad(30, 0, 20, 0));

        JPanel row = new JPanel(new GridLayout(1, 3, 20, 0));
        row.setOpaque(false);
        row.setBorder(Theme.pad(0, 40, 40, 40));

        Object[][] levels = {
            {"🐣",      "ง่าย",     "EASY",   Theme.EASY, "ข้าวซอย",      "75 วินาที | 2 ตัวลวง"},
            {"👨‍🍳", "ปานกลาง", "MEDIUM", Theme.MED,  "แกงฮังเล",     "55 วินาที | 4 ตัวลวง"},
            {"🔥",      "ยาก",      "HARD",   Theme.HARD, "น้ำพริกหนุ่ม", "35 วินาที | 6 ตัวลวง"},
        };

        for (Object[] lv : levels) {
            Color color = (Color) lv[3];

            JLabel lvName = new JLabel((String) lv[1], SwingConstants.CENTER);
            lvName.setFont(new Font("Tahoma", Font.BOLD, 20));
            lvName.setForeground(color);

            JPanel card = new JPanel(new GridLayout(4, 1));
            card.setBackground(Color.WHITE);
            card.setBorder(Theme.roundBorder(color, 2));
            card.add(Theme.emoji((String) lv[0], 40));
            card.add(lvName);
            card.add(Theme.plainCenter((String) lv[4]));
            card.add(Theme.plainCenter((String) lv[5]));

            JButton btn = Theme.pill("เลือก!", color);
            btn.addActionListener(e -> {
                gm.start(chefName, Recipe.Difficulty.valueOf((String) lv[2]));
                nav.showGame();
            });

            JPanel wrap = new JPanel(new BorderLayout(0, 10));
            wrap.setOpaque(false);
            wrap.add(card, BorderLayout.CENTER);
            wrap.add(btn,  BorderLayout.SOUTH);
            row.add(wrap);
        }

        add(title, BorderLayout.NORTH);
        add(row,   BorderLayout.CENTER);
    }

    public void setChefName(String name) {
        this.chefName = name;
    }
}
