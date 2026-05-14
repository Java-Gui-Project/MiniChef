package ui;

import javax.swing.*;
import java.awt.*;

/** Main menu screen: name input, play button, and leaderboard shortcut. */
public class MenuPanel extends JPanel {

    public MenuPanel(Navigator nav) {
        setName("MENU");
        setLayout(new BorderLayout());

        JLabel title = new JLabel("เชฟน้อยเมืองเหนือ", SwingConstants.CENTER);
        title.setFont(new Font("Thasadith", Font.BOLD, 40));
        title.setForeground(Theme.ORANGE);

        JLabel subtitle = new JLabel("Mini Chef Chiangmai | อาหารพื้นเมืองเชียงใหม่", SwingConstants.CENTER);
        subtitle.setFont(new Font("Thasadith", Font.BOLD, 16));

        JPanel topSection = Theme.vbox(title, subtitle);
        topSection.setBorder(Theme.pad(50, 0, 10, 0));

        JTextField nameField = new JTextField(14);
        nameField.setFont(Theme.F_BOLD);
        nameField.setHorizontalAlignment(JTextField.CENTER);

        JButton bPlay  = Theme.pill("▶ เริ่มเกม", Theme.ORANGE);
        JButton bBoard = Theme.pill("Leaderboard", Theme.GREEN);

        bPlay.addActionListener(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) { Theme.shake(nameField); return; }

            String rules =
                "🍜 กติกาการเล่น:\n"
                + "1. เลือกวัตถุดิบให้ตรงตามสูตรอาหารที่โจทย์กำหนด\n"
                + "2. หากเลือกผิด สามารถกดซ้ำที่วัตถุดิบเดิมเพื่อเอาออกได้\n"
                + "3. ทำเวลาให้เร็วที่สุดเพื่อคะแนนสูงสุด!\n\n"
                + "💡 เกร็ดความรู้:\n"
                + "- 'ข้าวซอย' จุดเด่นคือเส้นบะหมี่ไข่และน้ำแกงกะทิ\n"
                + "- 'น้ำพริกหนุ่ม' ทำจากพริกหนุ่มย่าง หอมแดง และกระเทียม";

            JOptionPane.showMessageDialog(
                SwingUtilities.getWindowAncestor(this),
                rules, "เตรียมตัวก่อนเข้าครัว", JOptionPane.INFORMATION_MESSAGE);

            nav.showDiff(name);
        });

        bBoard.addActionListener(e -> nav.showBoard());

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

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setPaint(new GradientPaint(0, 0, new Color(0xFF, 0xE8, 0xA0), 0, getHeight(), Theme.BG));
        g2.fillRect(0, 0, getWidth(), getHeight());
    }
}
