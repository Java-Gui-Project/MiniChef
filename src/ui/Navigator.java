package ui;

/**
 * กำหนดสัญญา (Contract) สำหรับการสลับหน้าจอในเกม
 * แต่ละ Panel จะเรียกใช้ Navigator แทนการแตะ CardLayout โดยตรง
 * ทำให้ Panel แต่ละตัวไม่ต้องรู้จักโครงสร้างของหน้าจออื่น
 */
public interface Navigator {

    /** กลับไปหน้าเมนูหลัก */
    void showMenu();

    /**
     * ไปหน้าเลือกระดับความยาก พร้อมส่งชื่อเชฟไปด้วย
     * @param chefName ชื่อที่ผู้เล่นกรอกในหน้าเมนู
     */
    void showDiff(String chefName);

    /** เริ่มหน้าเล่นเกม (โหลด Recipe และเริ่มจับเวลา) */
    void showGame();

    /** แสดงหน้าสรุปผลหลังจบเกม */
    void showResult();

    /** แสดงหน้า Leaderboard แบบ standalone จากเมนูหลัก */
    void showBoard();
}
