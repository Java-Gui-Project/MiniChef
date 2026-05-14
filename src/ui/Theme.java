package ui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * รวบรวมค่าคงที่ด้านสีและฟอนต์ รวมถึง Factory Method สำหรับสร้าง Widget ที่ใช้ร่วมกันทุกหน้าจอ
 * คลาสนี้มีแต่ static member ไม่ต้องสร้าง instance
 */
public class Theme {

    // ─── สี ──────────────────────────────────────────────────────────────────────

    /** สีพื้นหลังหลักของเกม (ครีมอ่อน) */
    public static final Color BG     = new Color(0xFF, 0xF3, 0xDC);

    /** สีส้ม ใช้กับปุ่มหลักและกรอบ */
    public static final Color ORANGE = new Color(0xFF, 0x8C, 0x00);

    /** สีแดง ใช้แสดงข้อความผิดพลาด */
    public static final Color RED    = new Color(0xC0, 0x39, 0x2B);

    /** สีเขียว ใช้กับปุ่มยืนยันและข้อความถูกต้อง */
    public static final Color GREEN  = new Color(0x27, 0xAE, 0x60);

    /** สีของระดับ Easy */
    public static final Color EASY   = new Color(0x2E, 0xCC, 0x71);

    /** สีของระดับ Medium */
    public static final Color MED    = new Color(0xF3, 0x9C, 0x12);

    /** สีของระดับ Hard */
    public static final Color HARD   = new Color(0xE7, 0x4C, 0x3C);

    // ─── ฟอนต์ ───────────────────────────────────────────────────────────────────

    /** ฟอนต์หลักแบบตัวหนา ขนาด 15 */
    public static final Font F_BOLD  = new Font("Thasadith", Font.BOLD, 15);

    /** ฟอนต์หลักขนาด 14 ใช้กับข้อความทั่วไป */
    public static final Font F_PLAIN = new Font("Thasadith", Font.BOLD, 14);

    // ─── วัตถุดิบหลอก ────────────────────────────────────────────────────────────

    /**
     * รายการวัตถุดิบหลอกที่จะนำมาผสมในกริดเลือกวัตถุดิบ
     * แต่ละรายการ: [ชื่อ, emoji]
     */
    public static final String[][] DECOY_POOL = {
        {"พริกแกง", "🌶"}, {"หอมแดง", "🧅"}, {"กะทิ", "🥥"}, {"มะนาว", "🍋"}
    };

    // ─── Factory Methods ─────────────────────────────────────────────────────────

    /**
     * สร้างปุ่มรูปทรงกลมมน (pill) พร้อมสีพื้นหลังและข้อความขาว
     * @param text ข้อความบนปุ่ม
     * @param bg   สีพื้นหลังปุ่ม
     */
    public static JButton pill(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setOpaque(true);
        b.setContentAreaFilled(true);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setFont(F_BOLD);
        return b;
    }

    /**
     * สร้าง JLabel ข้อความตัวหนา
     * @param text ข้อความที่แสดง
     */
    public static JLabel bold(String text) {
        JLabel l = new JLabel(text);
        l.setFont(F_BOLD);
        return l;
    }

    /**
     * สร้าง JLabel ข้อความธรรมดาจัดกึ่งกลาง
     * @param text ข้อความที่แสดง
     */
    public static JLabel plainCenter(String text) {
        JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setFont(F_PLAIN);
        return l;
    }

    /**
     * สร้าง JLabel แสดง emoji ขนาดใหญ่จัดกึ่งกลาง
     * @param text emoji ที่ต้องการแสดง
     * @param size ขนาดฟอนต์
     */
    public static JLabel emoji(String text, int size) {
        JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setFont(new Font("Segoe UI Emoji", Font.PLAIN, size));
        return l;
    }

    /**
     * สร้างกรอบเส้นมน
     * @param color     สีของเส้นกรอบ
     * @param thickness ความหนาของเส้น
     */
    public static Border roundBorder(Color color, int thickness) {
        return BorderFactory.createLineBorder(color, thickness, true);
    }

    /**
     * สร้าง EmptyBorder สำหรับเว้นระยะขอบ
     * @param top    ระยะบน
     * @param left   ระยะซ้าย
     * @param bottom ระยะล่าง
     * @param right  ระยะขวา
     */
    public static EmptyBorder pad(int top, int left, int bottom, int right) {
        return new EmptyBorder(top, left, bottom, right);
    }

    /**
     * สร้าง JPanel จัด Component แนวตั้ง (Vertical Box)
     * @param components Component ที่ต้องการจัดเรียง
     */
    public static JPanel vbox(JComponent... components) {
        JPanel p = new JPanel(new GridLayout(components.length, 1, 0, 5));
        p.setOpaque(false);
        for (JComponent c : components) p.add(c);
        return p;
    }

    /**
     * สร้าง GridBagConstraints พร้อม insets มาตรฐาน
     * ใช้กับ GridBagLayout ในหน้าเมนูหลัก
     */
    public static GridBagConstraints gbc() {
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(10, 10, 10, 10);
        return g;
    }

    // ─── Animation ───────────────────────────────────────────────────────────────

    /**
     * ทำให้ Component สั่น (Shake Animation) เพื่อแจ้งเตือนเมื่อข้อมูลไม่ถูกต้อง
     * @param c Component ที่ต้องการให้สั่น
     */
    public static void shake(Component c) {
        Point origin = c.getLocation();
        new javax.swing.Timer(50, new ActionListener() {
            int count = 0;
            public void actionPerformed(ActionEvent e) {
                if (count >= 6) {
                    c.setLocation(origin);
                    ((javax.swing.Timer) e.getSource()).stop();
                } else {
                    c.setLocation(origin.x + (count % 2 == 0 ? 5 : -5), origin.y);
                    count++;
                }
            }
        }).start();
    }

    /**
     * หน่วงเวลาแล้วรัน action บน Event Dispatch Thread
     * ใช้สำหรับรอแสดงผลก่อนเปลี่ยนหน้าจอ
     * @param ms     เวลาหน่วง (มิลลิวินาที)
     * @param action คำสั่งที่จะรันหลังหน่วงเวลา
     */
    public static void delay(int ms, Runnable action) {
        javax.swing.Timer t = new javax.swing.Timer(ms, e -> action.run());
        t.setRepeats(false);
        t.start();
    }
}
