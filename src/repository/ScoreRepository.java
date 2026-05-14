package repository;

import model.Chef;

import java.io.*;
import java.util.*;
import java.util.stream.*;

/**
 * จัดการการอ่านและเขียนไฟล์ scores.txt
 * รับผิดชอบการบันทึกคะแนนและโหลดข้อมูล Leaderboard เท่านั้น
 * ไม่มีตรรกะของเกมอยู่ในคลาสนี้
 */
public class ScoreRepository {

    /** ชื่อไฟล์ที่ใช้เก็บคะแนนผู้เล่นทุกคน */
    private static final String SAVE_FILE   = "scores.txt";

    /** จำนวนอันดับสูงสุดที่จะแสดงใน Leaderboard */
    private static final int    TOP_DISPLAY = 5;

    /**
     * บันทึกข้อมูลคะแนนของเชฟต่อท้ายไฟล์ scores.txt
     * ใช้รูปแบบ CSV: "ชื่อ,คะแนน,ความยาก,เมนู1;เมนู2"
     * @param chef ข้อมูลเชฟที่ต้องการบันทึก
     */
    public void save(Chef chef) {
        try (BufferedWriter w = new BufferedWriter(new FileWriter(SAVE_FILE, true))) {
            w.write(chef.toString());
            w.newLine();
        } catch (IOException e) {
            System.err.println("ไม่สามารถบันทึกคะแนนได้: " + e.getMessage());
        }
    }

    /**
     * โหลดข้อมูลจากไฟล์ scores.txt แล้วคืนรายชื่อเชฟ
     * เรียงจากคะแนนมากไปน้อย และดึงมาเฉพาะ TOP_DISPLAY อันดับแรก
     * @return รายการเชฟที่มีคะแนนสูงสุด ถ้าไม่มีไฟล์คืน List ว่าง
     */
    public List<Chef> loadLeaderboard() {
        List<Chef> list = new ArrayList<>();
        File file = new File(SAVE_FILE);
        if (!file.exists()) return list;

        try (BufferedReader r = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = r.readLine()) != null) {
                if (!line.isBlank()) {
                    try { list.add(Chef.parse(line)); } catch (Exception ignored) {}
                }
            }
        } catch (IOException e) {
            System.err.println("ไม่สามารถโหลด Leaderboard ได้: " + e.getMessage());
        }

        return list.stream()
                .sorted((a, b) -> b.score - a.score)
                .limit(TOP_DISPLAY)
                .collect(Collectors.toList());
    }
}
