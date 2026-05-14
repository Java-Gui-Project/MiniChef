package ui;

import game.GameManager;
import model.Chef;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/** End-of-game summary screen: final score and top-5 leaderboard. */
public class ResultPanel extends JPanel {

    private final GameManager gm;
    private final Navigator   nav;

    public ResultPanel(GameManager gm, Navigator nav) {
        this.gm  = gm;
        this.nav = nav;
        setName("RESULT");
    }

    /** Rebuilds the screen with the latest score and leaderboard data. */
    public void refresh() {
        removeAll();
        setLayout(new BorderLayout(20, 20));
        setBackground(Color.WHITE);
        setBorder(Theme.pad(30, 50, 30, 50));

        // Congratulation header
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

        // Leaderboard table
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
        table.setEnabled(false);
        table.getTableHeader().setFont(Theme.F_BOLD);
        table.getTableHeader().setBackground(Theme.ORANGE);
        table.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(400, 180));
        scrollPane.setBorder(BorderFactory.createTitledBorder(" 🏆 อันดับเชฟยอดเยี่ยม 🏆 "));

        // Back button
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
