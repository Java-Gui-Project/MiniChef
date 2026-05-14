package ui;

import game.GameManager;
import model.Chef;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/** Standalone leaderboard screen accessible from the main menu. */
public class LeaderboardPanel extends JPanel {

    private final GameManager gm;
    private final Navigator   nav;

    public LeaderboardPanel(GameManager gm, Navigator nav) {
        this.gm  = gm;
        this.nav = nav;
        setName("BOARD");
    }

    /** Rebuilds the screen with the latest data from scores.txt. */
    public void refresh() {
        removeAll();
        setLayout(new BorderLayout());
        setBackground(Theme.BG);

        List<Chef> top = gm.loadLeaderboard();
        StringBuilder html = new StringBuilder(
            "<html><center><h1>กระดานยอดเชฟ</h1><table border='0' width='300'>");
        for (Chef c : top) {
            html.append("<tr><td>").append(c.name)
                .append("</td><td align='right'>").append(c.score).append(" แต้ม</td></tr>");
        }
        html.append("</table></center></html>");

        JButton bBack = Theme.pill("กลับ", Color.GRAY);
        bBack.addActionListener(e -> nav.showMenu());

        add(new JLabel(html.toString(), SwingConstants.CENTER), BorderLayout.CENTER);
        add(bBack, BorderLayout.SOUTH);
        revalidate();
        repaint();
    }
}
