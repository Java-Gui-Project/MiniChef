package ui;

import game.GameManager;
import model.Chef;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * หน้าจอ Leaderboard แบบ Standalone
 * เข้าถึงได้จากปุ่ม "Leaderboard" ในหน้าเมนูหลักโดยไม่ต้องเล่นก่อน
 * แสดงรายชื่อเชฟ TOP 5 ที่มีคะแนนสูงสุดตลอดกาล
 * เรียก refresh() ทุกครั้งก่อนแสดงเพื่อดึงข้อมูลล่าสุดจากไฟล์
 */
public class LeaderboardPanel extends JPanel {

    /** GameManager สำหรับโหลดข้อมูล Leaderboard จาก ScoreRepository */
    private final GameManager gm;

    /** Navigator สำหรับกลับหน้าเมนูหลัก */
    private final Navigator nav;

    /**
     * สร้าง LeaderboardPanel (ยังไม่ build UI จนกว่าจะเรียก refresh())
     * @param gm  GameManager ที่มี ScoreRepository
     * @param nav Navigator สำหรับเปลี่ยนหน้าจอ
     */
    public LeaderboardPanel(GameManager gm, Navigator nav) {
        this.gm  = gm;
        this.nav = nav;
        setName("BOARD");
    }

    /**
     * โหลดข้อมูลล่าสุดจาก scores.txt และ Rebuild หน้า Leaderboard
     * ถูกเรียกโดย Navigator ทุกครั้งก่อนแสดงหน้านี้
     */
    public void refresh() {
        removeAll();
        setLayout(new BorderLayout());
        setBackground(Theme.BG);

        // สร้างตาราง HTML สำหรับแสดงชื่อและคะแนน TOP 5
        List<Chef> top = gm.loadLeaderboard();
        StringBuilder html = new StringBuilder(
            "<html><center><h1>กระดานยอดเชฟ</h1><table border='0' width='300'>");
        for (Chef c : top) {
            html.append("<tr><td>").append(c.name)
                .append("</td><td align='right'>").append(c.score).append(" แต้ม</td></tr>");
        }
        html.append("</table></center></html>");

        // ปุ่มกลับหน้าหลัก
        JButton bBack = Theme.pill("กลับ", Color.GRAY);
        bBack.addActionListener(e -> nav.showMenu());

        add(new JLabel(html.toString(), SwingConstants.CENTER), BorderLayout.CENTER);
        add(bBack, BorderLayout.SOUTH);
        revalidate();
        repaint();
    }
}
