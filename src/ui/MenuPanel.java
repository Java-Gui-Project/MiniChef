package ui;

import javax.swing.*;
import java.awt.*;

/**
 * หน้าจอเมนูหลักของเกม
 * แสดงชื่อเกม ช่องกรอกชื่อเชฟ ปุ่มเริ่มเกม และปุ่มดู Leaderboard
 * เมื่อกดเริ่มเกมจะแสดงกติกาก่อน แล้วจึงนำไปสู่หน้าเลือกความยาก
 */
public class MenuPanel extends JPanel {

    /**
     * สร้างหน้าเมนูหลัก
     * @param nav ตัวจัดการการเปลี่ยนหน้าจอ
     */
    public MenuPanel(Navigator nav) {
        setName("MENU");
        setLayout(new BorderLayout());

        // ส่วนหัว: ชื่อเกมและคำอธิบาย
        JLabel title = new JLabel("เชฟน้อยเมืองเหนือ", SwingConstants.CENTER);
        title.setFont(new Font("Thasadith", Font.BOLD, 40));
        title.setForeground(Theme.ORANGE);

        JLabel subtitle = new JLabel("Mini Chef Chiangmai | อาหารพื้นเมืองเชียงใหม่", SwingConstants.CENTER);
        subtitle.setFont(new Font("Thasadith", Font.BOLD, 16));

        JPanel topSection = Theme.vbox(title, subtitle);
        topSection.setBorder(Theme.pad(50, 0, 10, 0));

        // ช่องกรอกชื่อเชฟ
        JTextField nameField = new JTextField(14);
        nameField.setFont(Theme.F_BOLD);
        nameField.setHorizontalAlignment(JTextField.CENTER);

        // ปุ่มเริ่มเกมและ Leaderboard
        JButton bPlay  = Theme.pill("▶ เริ่มเกม", Theme.ORANGE);
        JButton bBoard = Theme.pill("Leaderboard", Theme.GREEN);

        // เมื่อกดเริ่มเกม: ตรวจสอบชื่อ → แสดงกติกา → ไปหน้าเลือกความยาก
        bPlay.addActionListener(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) { Theme.shake(nameField); return; } // สั่นถ้าไม่ได้กรอกชื่อ

            String rules =
                "🍜 กติกาการเล่น:\n"
                + "1. ใส่ชื่อและเลือกระดับความยากก่อนเริ่มเกม\n"
                + "2. เลือกวัตถุดิบให้ครบและถูกต้องตามสูตร ห้ามเกินหรือขาด\n"
                + "3. ระวังตัวลวงที่แฝงอยู่ในวัตถุดิบ ถ้าเลือกผิดต้องลองใหม่\n"
                + "4. ส่งอาหารให้เร็วที่สุด ยิ่งเร็วยิ่งได้คะแนนโบนัสมาก\n"
                + "5. ถ้าหมดเวลาจะไม่ได้คะแนนและข้ามเมนูนั้นไปเลย\n\n"
                + "💡 เกร็ดความรู้:\n"
                + "- ข้าวซอย ทำจาก เส้นบะหมี่ ไก่ กะทิ พริกแกง หัวหอม มะนาว\n"
                + "- แกงฮังเล ทำจาก หมูสามชั้น พริกแกงฮังเล ขิง กระเทียม ถั่วลิสง มะขามเปียก น้ำปลา\n"
                + "- น้ำพริกหนุ่ม ทำจาก พริกหนุ่มย่าง หอมแดง กระเทียม กะปิ มะเขือเทศ เกลือ น้ำปลา";

            JOptionPane.showMessageDialog(
                SwingUtilities.getWindowAncestor(this),
                rules, "เตรียมตัวก่อนเข้าครัว", JOptionPane.INFORMATION_MESSAGE);

            nav.showDiff(name); // ส่งชื่อเชฟไปยังหน้าเลือกความยาก
        });

        // เมื่อกด Leaderboard: ไปหน้ากระดานคะแนน
        bBoard.addActionListener(e -> nav.showBoard());

        // จัดวาง Widget ตรงกลางด้วย GridBagLayout
        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        GridBagConstraints g = Theme.gbc();
        g.gridwidth = 2; center.add(Theme.bold("ใส่ชื่อเชฟของคุณ:"), g);
        g.gridy = 1;     center.add(nameField, g);
        g.gridy = 2; g.gridwidth = 1; center.add(bPlay, g);
        g.gridx = 1;     center.add(bBoard, g);

        add(topSection, BorderLayout.NORTH);
        add(center,     BorderLayout.CENTER);
    }

    /** วาดพื้นหลัง Gradient จากสีเหลืองอ่อนไปครีม */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setPaint(new GradientPaint(0, 0, new Color(0xFF, 0xE8, 0xA0), 0, getHeight(), Theme.BG));
        g2.fillRect(0, 0, getWidth(), getHeight());
    }
}
