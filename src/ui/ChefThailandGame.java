package ui;

import game.GameManager;

import javax.swing.*;
import java.awt.*;

/**
 * Entry Point ของแอปพลิเคชัน
 * ทำหน้าที่สร้าง JFrame หลัก สร้างและเชื่อมต่อ Panel ทุกหน้า
 * และ implement Navigator เพื่อจัดการการสลับหน้าจอด้วย CardLayout
 */
public class ChefThailandGame extends JFrame implements Navigator {

    /** ตัวควบคุมตรรกะเกมหลัก ใช้ร่วมกันทุก Panel */
    private final GameManager gm = new GameManager();

    /** Layout ที่ใช้สลับระหว่าง Panel หลายหน้าด้วยชื่อ */
    private final CardLayout cards = new CardLayout();

    /** Container หลักที่ถือ Panel ทุกหน้าไว้ */
    private final JPanel root = new JPanel(cards);

    /** หน้าเลือกระดับความยาก (ต้องอ้างอิงเพื่อเรียก setChefName) */
    private final DifficultyPanel diffPanel;

    /** หน้าเล่นเกม (ต้องอ้างอิงเพื่อเรียก refresh) */
    private final GamePanel gamePanel;

    /** หน้าสรุปผล (ต้องอ้างอิงเพื่อเรียก refresh) */
    private final ResultPanel resultPanel;

    /** หน้า Leaderboard standalone (ต้องอ้างอิงเพื่อเรียก refresh) */
    private final LeaderboardPanel boardPanel;

    /**
     * สร้างหน้าต่างหลัก ประกอบร่าง Panel ทั้งหมด และแสดงหน้าเมนูก่อน
     */
    public ChefThailandGame() {
        super("เชฟน้อยเมืองเหนือ — Mini Chef Chiangmai");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(780, 600);
        setLocationRelativeTo(null); // จัดให้อยู่กึ่งกลางหน้าจอ
        setResizable(false);

        // สร้าง Panel ทุกหน้าพร้อมส่ง GameManager และ Navigator (this) ไปด้วย
        diffPanel   = new DifficultyPanel(gm, this);
        gamePanel   = new GamePanel(gm, this);
        resultPanel = new ResultPanel(gm, this);
        boardPanel  = new LeaderboardPanel(gm, this);

        // ลงทะเบียน Panel ทุกหน้าใน CardLayout ด้วยชื่อที่ Navigator ใช้
        root.add(new MenuPanel(this), "MENU");
        root.add(diffPanel,           "DIFF");
        root.add(gamePanel,           "GAME");
        root.add(resultPanel,         "RESULT");
        root.add(boardPanel,          "BOARD");

        add(root);
        cards.show(root, "MENU"); // เริ่มที่หน้าเมนูหลัก
        setVisible(true);
    }

    // ─── Navigator (การสลับหน้าจอ) ───────────────────────────────────────────────

    /** แสดงหน้าเมนูหลัก */
    @Override public void showMenu() { cards.show(root, "MENU"); }

    /**
     * แสดงหน้าเลือกความยาก พร้อมตั้งชื่อเชฟที่จะใช้เมื่อเริ่มเกม
     * @param name ชื่อเชฟจากหน้าเมนู
     */
    @Override public void showDiff(String name) { diffPanel.setChefName(name); cards.show(root, "DIFF"); }

    /** โหลดเมนูแรกและเริ่มจับเวลา แล้วแสดงหน้าเล่นเกม */
    @Override public void showGame()   { gamePanel.refresh();   cards.show(root, "GAME"); }

    /** อัปเดตคะแนนและ Leaderboard แล้วแสดงหน้าสรุปผล */
    @Override public void showResult() { resultPanel.refresh(); cards.show(root, "RESULT"); }

    /** โหลดข้อมูลล่าสุดจากไฟล์แล้วแสดงหน้า Leaderboard */
    @Override public void showBoard()  { boardPanel.refresh();  cards.show(root, "BOARD"); }

    // ─── Entry Point ─────────────────────────────────────────────────────────────

    /**
     * จุดเริ่มต้นของโปรแกรม
     * สร้าง JFrame บน Event Dispatch Thread ตามข้อกำหนดของ Swing
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(ChefThailandGame::new);
    }
}
