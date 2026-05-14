package ui;

import game.GameManager;
import model.Chef;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * หน้าจอสรุปผลหลังจบเกม
 * แสดงข้อความยินดี คะแนนสุดท้ายของเชฟ และตาราง Leaderboard TOP 5
 * เรียก refresh() ทุกครั้งก่อนแสดงหน้านี้เพื่อดึงข้อมูลล่าสุด
 */
public class ResultPanel extends JPanel {

    /** GameManager สำหรับดึงชื่อ คะแนน และโหลด Leaderboard */
    private final GameManager gm;

    /** Navigator สำหรับกลับหน้าเมนูหลัก */
    private final Navigator nav;

    /**
     * สร้าง ResultPanel (ยังไม่ build UI จนกว่าจะเรียก refresh())
     * @param gm  GameManager ที่มีข้อมูลผู้เล่นและ Leaderboard
     * @param nav Navigator สำหรับเปลี่ยนหน้าจอ
     */
    public ResultPanel(GameManager gm, Navigator nav) {
        this.gm  = gm;
        this.nav = nav;
        setName("RESULT");
    }

    /**
     * สร้างหน้าสรุปผลใหม่ทั้งหมดด้วยข้อมูลล่าสุด
     * ถูกเรียกโดย Navigator ทุกครั้งก่อนแสดงหน้านี้
     */
    public void refresh() {
        removeAll();
        setLayout(new BorderLayout(20, 20));
        setBackground(Color.WHITE);
        setBorder(Theme.pad(30, 50, 30, 50));

        // ส่วนบน: ข้อความยินดีและคะแนนสุดท้าย
        JLabel lblWin = new JLabel("🎉 ภารกิจสำเร็จ! 🎉", SwingConstants.CENTER);
        lblWin.setFont(new Font("Tahoma", Font.BOLD, 36));
        lblWin.setForeground(Theme.GREEN);

        JLabel lblScore = new JLabel(
            "เชฟ " + gm.chef.name + " ทำคะแนนได้ทั้งหมด: " + gm.chef.score + " แต้ม",
            SwingConstants.CENTER);
        lblScore.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblScore.setBorder(Theme.pad(10, 0, 10, 0));

        JPanel topSection = Theme.vbox(lblWin, lblScore);
        topSection.setOpaque(false);

        // ส่วนกลาง: ตาราง Leaderboard TOP 5
        List<Chef> topList = gm.loadLeaderboard();
        String[]   columns = {"อันดับ", "ชื่อเชฟ", "คะแนน"};
        Object[][] data    = new Object[topList.size()][3];
        for (int i = 0; i < topList.size(); i++) {
            data[i][0] = i + 1;
            data[i][1] = topList.get(i).name;
            data[i][2] = topList.get(i).score + " แต้ม";
        }

        JTable table = new JTable(data, columns);
        table.setFont(Theme.F_PLAIN);
        table.setRowHeight(30);
        table.setEnabled(false); // ป้องกันการแก้ไขตาราง
        table.getTableHeader().setFont(Theme.F_BOLD);
        table.getTableHeader().setBackground(Theme.ORANGE);
        table.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(400, 180));
        scrollPane.setBorder(BorderFactory.createTitledBorder(" 🏆 อันดับเชฟยอดเยี่ยม 🏆 "));

        // ส่วนล่าง: ปุ่มกลับหน้าหลัก
        JButton bHome = Theme.pill("กลับหน้าหลัก", Theme.ORANGE);
        bHome.addActionListener(e -> nav.showMenu());

        JPanel botSection = new JPanel(new FlowLayout(FlowLayout.CENTER));
        botSection.setOpaque(false);
        botSection.add(bHome);

        add(topSection, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(botSection, BorderLayout.SOUTH);
        revalidate();
        repaint();
    }
}
