package ui;

import game.GameManager;
import model.Recipe;

import javax.swing.*;
import java.awt.*;

/**
 * หน้าจอเลือกระดับความยาก
 * แสดง Card 3 ใบ (ง่าย / ปานกลาง / ยาก) พร้อมข้อมูลเมนูและเวลา
 * เมื่อกดเลือกจะเริ่มเกมและนำไปสู่หน้าเล่นทันที
 * ต้องเรียก setChefName() ก่อนแสดงหน้านี้เสมอ
 */
public class DifficultyPanel extends JPanel {

    /** ชื่อเชฟที่รับมาจากหน้าเมนูหลัก ใช้เมื่อเริ่มเกม */
    private String chefName = "";

    /**
     * สร้างหน้าเลือกความยาก
     * @param gm  GameManager สำหรับเริ่มเกมเมื่อผู้เล่นเลือกความยาก
     * @param nav ตัวจัดการการเปลี่ยนหน้าจอ
     */
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

        // ข้อมูลแต่ละระดับ: [emoji, ชื่อภาษาไทย, ชื่อ Enum, สี, เมนู, ข้อมูลเวลา/ตัวลวง]
        Object[][] levels = {
            {"🐣",      "ง่าย",     "EASY",   Theme.EASY, "ข้าวซอย",      "75 วินาที | 2 ตัวลวง"},
            {"👨‍🍳", "ปานกลาง", "MEDIUM", Theme.MED,  "แกงฮังเล",     "55 วินาที | 4 ตัวลวง"},
            {"🔥",      "ยาก",      "HARD",   Theme.HARD, "น้ำพริกหนุ่ม", "35 วินาที | 6 ตัวลวง"},
        };

        for (Object[] lv : levels) {
            Color color = (Color) lv[3];

            // ชื่อระดับความยาก
            JLabel lvName = new JLabel((String) lv[1], SwingConstants.CENTER);
            lvName.setFont(new Font("Tahoma", Font.BOLD, 20));
            lvName.setForeground(color);

            // Card แสดงข้อมูลระดับความยาก
            JPanel card = new JPanel(new GridLayout(4, 1));
            card.setBackground(Color.WHITE);
            card.setBorder(Theme.roundBorder(color, 2));
            card.add(Theme.emoji((String) lv[0], 40)); // emoji ขนาดใหญ่
            card.add(lvName);
            card.add(Theme.plainCenter((String) lv[4])); // ชื่อเมนู
            card.add(Theme.plainCenter((String) lv[5])); // เวลาและตัวลวง

            // ปุ่มเลือก: เริ่มเกมด้วยความยากนั้นและเปลี่ยนหน้า
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

    /**
     * ตั้งชื่อเชฟที่จะใช้เมื่อเริ่มเกม
     * ต้องเรียกก่อนที่ผู้เล่นจะกดเลือกความยาก
     * @param name ชื่อเชฟจากหน้าเมนูหลัก
     */
    public void setChefName(String name) {
        this.chefName = name;
    }
}
