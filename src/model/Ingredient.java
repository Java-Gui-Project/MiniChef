package model;

/**
 * แทนวัตถุดิบหนึ่งชนิดในเกม
 * เก็บข้อมูลชื่อและไอคอน emoji ของวัตถุดิบนั้น
 */
public class Ingredient {

    /** ชื่อวัตถุดิบ เช่น "กะทิ", "มะนาว" */
    public String name;

    /** emoji แทนวัตถุดิบ เช่น "🥥", "🍋" */
    public String emoji;

    /**
     * สร้างวัตถุดิบใหม่
     * @param name  ชื่อวัตถุดิบ
     * @param emoji emoji แทนวัตถุดิบ
     */
    public Ingredient(String name, String emoji) {
        this.name  = name;
        this.emoji = emoji;
    }

    /** แสดงผลในรูปแบบ "🥥 กะทิ" */
    @Override
    public String toString() {
        return emoji + " " + name;
    }
}
