package model;

import java.util.*;
import java.util.stream.Collectors;

/**
 * แทนสูตรอาหารหนึ่งเมนู
 * เก็บข้อมูลชื่อเมนู จังหวัด คะแนน ระดับความยาก และรายการวัตถุดิบที่ต้องใช้
 */
public class Recipe {

    /**
     * ระดับความยากของเมนู
     * EASY = ง่าย, MEDIUM = ปานกลาง, HARD = ยาก
     */
    public enum Difficulty { EASY, MEDIUM, HARD }

    /** ชื่อเมนูอาหาร เช่น "ข้าวซอย" */
    public String dish;

    /** จังหวัดต้นกำเนิดของเมนู เช่น "เชียงใหม่" */
    public String province;

    /** คะแนนพื้นฐานที่ได้รับเมื่อทำเมนูนี้สำเร็จ */
    public int baseScore;

    /** ระดับความยากของเมนูนี้ */
    public Difficulty diff;

    /** รายการวัตถุดิบที่ต้องใช้ในสูตรนี้ */
    public List<Ingredient> ingredients = new ArrayList<>();

    /**
     * สร้างสูตรอาหารใหม่
     * @param dish      ชื่อเมนู
     * @param province  จังหวัดต้นกำเนิด
     * @param baseScore คะแนนพื้นฐาน
     * @param diff      ระดับความยาก
     */
    public Recipe(String dish, String province, int baseScore, Difficulty diff) {
        this.dish      = dish;
        this.province  = province;
        this.baseScore = baseScore;
        this.diff      = diff;
    }

    /**
     * เพิ่มวัตถุดิบเข้าไปในสูตรนี้
     * @param name  ชื่อวัตถุดิบ
     * @param emoji emoji แทนวัตถุดิบ
     */
    public void add(String name, String emoji) {
        ingredients.add(new Ingredient(name, emoji));
    }

    /**
     * ตรวจสอบว่าวัตถุดิบที่ผู้เล่นเลือกถูกต้องครบถ้วนหรือไม่
     * จำนวนต้องเท่ากันและมีชื่อตรงกันทุกรายการ
     * @param selected รายการชื่อวัตถุดิบที่ผู้เล่นเลือก
     * @return true ถ้าถูกต้องครบถ้วน
     */
    public boolean check(List<String> selected) {
        if (selected.size() != ingredients.size()) return false;
        List<String> required = ingredients.stream()
                .map(i -> i.name)
                .collect(Collectors.toList());
        return selected.containsAll(required);
    }

    /**
     * คืนจำนวนวัตถุดิบหลอกที่จะแสดงในกริด
     * ยิ่งยากยิ่งมีตัวลวงมาก
     */
    public int decoys() {
        switch (diff) {
            case EASY:   return 2;
            case MEDIUM: return 4;
            default:     return 6;
        }
    }

    /**
     * คืนเวลาจำกัด (วินาที) ของเมนูนี้
     * ยิ่งยากยิ่งมีเวลาน้อย
     */
    public int timeLimit() {
        switch (diff) {
            case EASY:   return 75;
            case MEDIUM: return 55;
            default:     return 35;
        }
    }

    /**
     * คืนตัวคูณโบนัสคะแนนจากเวลาที่เหลือ
     * คะแนนโบนัส = เวลาที่เหลือ × timeBonus()
     */
    public int timeBonus() {
        switch (diff) {
            case EASY:   return 2;
            case MEDIUM: return 3;
            default:     return 4;
        }
    }

    /** คืนชื่อระดับความยากเป็นภาษาไทย */
    public String diffLabel() {
        switch (diff) {
            case EASY:   return "ง่าย";
            case MEDIUM: return "ปานกลาง";
            default:     return "ยาก";
        }
    }
}
