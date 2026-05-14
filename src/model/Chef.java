package model;

import java.util.*;

/**
 * แทนผู้เล่น (เชฟ) หนึ่งคน
 * เก็บข้อมูลชื่อ คะแนนสะสม ระดับความยากที่เลือก และเมนูที่เคยทำสำเร็จ
 */
public class Chef {

    /** ชื่อเชฟที่ผู้เล่นกรอก */
    public String name;

    /** ระดับความยากที่เชฟเลือกเล่น เช่น "EASY", "MEDIUM", "HARD" */
    public String difficultyPreference;

    /** คะแนนสะสมรวมของเชฟในรอบนี้ */
    public int score;

    /** รายชื่อเมนูที่เชฟทำสำเร็จแล้วในรอบนี้ */
    public List<String> unlockedDishes = new ArrayList<>();

    /**
     * สร้างเชฟใหม่
     * @param name                 ชื่อเชฟ
     * @param difficultyPreference ระดับความยากที่เลือก
     */
    public Chef(String name, String difficultyPreference) {
        this.name                 = name;
        this.difficultyPreference = difficultyPreference;
        this.score                = 0;
    }

    /**
     * เพิ่มคะแนนให้เชฟ
     * @param pts คะแนนที่ได้รับ
     */
    public void addScore(int pts) { this.score += pts; }

    /**
     * บันทึกเมนูที่ทำสำเร็จ (ไม่บันทึกซ้ำถ้ามีอยู่แล้ว)
     * @param dish ชื่อเมนูที่ทำสำเร็จ
     */
    public void unlock(String dish) {
        if (!unlockedDishes.contains(dish)) unlockedDishes.add(dish);
    }

    /**
     * แปลงข้อมูลเชฟเป็น String รูปแบบ CSV เพื่อบันทึกลงไฟล์
     * รูปแบบ: "ชื่อ,คะแนน,ความยาก,เมนู1;เมนู2"
     */
    @Override
    public String toString() {
        return String.format("%s,%d,%s,%s",
                name, score, difficultyPreference, String.join(";", unlockedDishes));
    }

    /**
     * อ่าน String จากไฟล์แล้วแปลงกลับเป็น Chef object
     * ใช้สำหรับโหลดข้อมูลจาก scores.txt
     * @param line บรรทัด CSV จากไฟล์
     * @return Chef object หรือเชฟ default ถ้าข้อมูลผิดพลาด
     */
    public static Chef parse(String line) {
        try {
            String[] parts = line.split(",", -1);
            String name  = parts[0];
            int score    = (parts.length > 1 && !parts[1].isEmpty()) ? Integer.parseInt(parts[1]) : 0;
            String diff  = parts.length > 2 ? parts[2] : "EASY";

            Chef chef  = new Chef(name, diff);
            chef.score = score;

            if (parts.length > 3 && !parts[3].isEmpty()) {
                for (String dish : parts[3].split(";")) chef.unlock(dish);
            }
            return chef;
        } catch (Exception e) {
            System.err.println("ไม่สามารถอ่านข้อมูลเชฟได้: " + e.getMessage());
            return new Chef("Unknown", "EASY");
        }
    }

    /**
     * คืนยศของเชฟตามคะแนนสะสม
     * 400+ = ตำนาน, 200+ = มืออาชีพ, น้อยกว่า = หน้าใหม่
     */
    public String getRank() {
        if (score >= 400) return "เชฟระดับตำนาน! 🏆";
        if (score >= 200) return "เชฟมืออาชีพ 🍳";
        return "เชฟหน้าใหม่ 🔪";
    }
}
