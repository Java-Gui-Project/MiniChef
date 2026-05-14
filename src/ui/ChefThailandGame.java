package ui;

import game.GameManager;

import javax.swing.*;
import java.awt.*;

/** Application entry point. Owns the JFrame, wires all panels together, and implements navigation. */
public class ChefThailandGame extends JFrame implements Navigator {

    private final GameManager      gm          = new GameManager();
    private final CardLayout       cards       = new CardLayout();
    private final JPanel           root        = new JPanel(cards);

    private final DifficultyPanel  diffPanel;
    private final GamePanel        gamePanel;
    private final ResultPanel      resultPanel;
    private final LeaderboardPanel boardPanel;

    public ChefThailandGame() {
        super("เชฟน้อยเมืองเหนือ — Mini Chef Chiangmai");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(780, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        diffPanel   = new DifficultyPanel(gm, this);
        gamePanel   = new GamePanel(gm, this);
        resultPanel = new ResultPanel(gm, this);
        boardPanel  = new LeaderboardPanel(gm, this);

        root.add(new MenuPanel(this), "MENU");
        root.add(diffPanel,           "DIFF");
        root.add(gamePanel,           "GAME");
        root.add(resultPanel,         "RESULT");
        root.add(boardPanel,          "BOARD");

        add(root);
        cards.show(root, "MENU");
        setVisible(true);
    }

    // ─── Navigator ───────────────────────────────────────────────────────────────

    @Override public void showMenu()            { cards.show(root, "MENU"); }
    @Override public void showDiff(String name) { diffPanel.setChefName(name); cards.show(root, "DIFF"); }
    @Override public void showGame()            { gamePanel.refresh();  cards.show(root, "GAME"); }
    @Override public void showResult()          { resultPanel.refresh(); cards.show(root, "RESULT"); }
    @Override public void showBoard()           { boardPanel.refresh();  cards.show(root, "BOARD"); }

    // ─── Entry point ─────────────────────────────────────────────────────────────

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ChefThailandGame::new);
    }
}
