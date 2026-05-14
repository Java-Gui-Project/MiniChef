package game;

import model.Chef;
import model.Ingredient;
import model.Recipe;
import repository.ScoreRepository;

import java.util.*;
import java.util.stream.*;

/**
 * ควบคุมตรรกะหลักของเกมทั้งหมด
 * ทำหน้าที่จัดการลำดับเมนู คำนวณคะแนน จับเวลา และเรียกใช้ ScoreRepository
 */
public class GameManager {

    /** รายการสูตรอาหารทั้งหมดในเกม (ทุกระดับความยาก) */
    public List<Recipe> allRecipes = new ArrayList<>();

    /** ข้อมูลผู้เล่นในรอบปัจจุบัน */
    public Chef chef;

    /** ดัชนีเมนูปัจจุบันในรายการที่กรองตามความยากแล้ว */
    public int recipeIndex;

    /** เวลาที่เหลือ (วินาที) สำหรับเมนูปัจจุบัน */
    public int timeLeft;

    /** จำนวนครั้งที่ผู้เล่นตอบผิดในรอบนี้ */
    public int wrongCount = 0;

    /** ระดับความยากที่ผู้เล่นเลือกในรอบนี้ */
    public Recipe.Difficulty difficulty;

    /** ตัวจัดการการบันทึกและโหลดคะแนนจากไฟล์ */
    private final ScoreRepository repository = new ScoreRepository();

    /** สร้าง GameManager และโหลดสูตรอาหารทั้งหมดทันที */
    public GameManager() {
        buildRecipes();
    }

    // ─── Setup ───────────────────────────────────────────────────────────────────

    /** สร้างรายการสูตรอาหารทั้งหมดพร้อมวัตถุดิบ แยกตามระดับความยาก */
    private void buildRecipes() {
        Recipe easy = new Recipe("ข้าวซอย", "เชียงใหม่", 100, Recipe.Difficulty.EASY);
        easy.add("เส้นบะหมี่", "🍜");
        easy.add("ไก่",        "🍗");
        easy.add("กะทิ",       "🥥");
        easy.add("พริกแกง",    "🌶️");
        easy.add("หัวหอม",     "🧅");
        easy.add("มะนาว",      "🍋");
        allRecipes.add(easy);

        Recipe medium = new Recipe("แกงฮังเล", "เชียงใหม่", 150, Recipe.Difficulty.MEDIUM);
        medium.add("หมูสามชั้น",   "🥓");
        medium.add("พริกแกงฮังเล", "🌶️");
        medium.add("ขิง",          "🫚");
        medium.add("กระเทียม",     "🧄");
        medium.add("ถั่วลิสง",     "🥜");
        medium.add("มะขามเปียก",   "🟤");
        medium.add("น้ำปลา",       "🐟");
        allRecipes.add(medium);

        Recipe hard = new Recipe("น้ำพริกหนุ่ม", "เชียงใหม่", 220, Recipe.Difficulty.HARD);
        hard.add("พริกหนุ่ม",  "🌶️");
        hard.add("หอมแดง",     "🧅");
        hard.add("กระเทียม",   "🧄");
        hard.add("กะปิ",       "🦐");
        hard.add("มะเขือเทศ", "🍅");
        hard.add("เกลือ",      "🧂");
        hard.add("น้ำปลา",     "🐟");
        allRecipes.add(hard);
    }

    // ─── Game flow ───────────────────────────────────────────────────────────────

    /**
     * เริ่มเกมใหม่ — สร้างเชฟ รีเซ็ตดัชนี และตั้งเวลาสำหรับเมนูแรก
     * @param name ชื่อผู้เล่น (ถ้าว่างใช้ "Guest")
     * @param d    ระดับความยากที่เลือก
     */
    public void start(String name, Recipe.Difficulty d) {
        if (name == null || name.isEmpty()) name = "Guest";
        this.difficulty  = d;
        this.chef        = new Chef(name, d.name());
        this.recipeIndex = 0;
        this.wrongCount  = 0;
        resetTime();
    }

    /**
     * คืนเมนูปัจจุบันที่ผู้เล่นต้องทำ (กรองตามระดับความยากที่เลือก)
     * @return Recipe ปัจจุบัน หรือ null ถ้าทำครบทุกเมนูแล้ว
     */
    public Recipe current() {
        List<Recipe> filtered = filteredRecipes();
        return recipeIndex < filtered.size() ? filtered.get(recipeIndex) : null;
    }

    /**
     * ตรวจสอบว่ายังมีเมนูให้เล่นอยู่หรือไม่
     * @return true ถ้ายังมีเมนูถัดไป
     */
    public boolean hasNext() {
        return recipeIndex < filteredRecipes().size();
    }

    /**
     * รับคำตอบจากผู้เล่น ตรวจสอบความถูกต้อง และคำนวณคะแนน
     * ถ้าถูก: เพิ่มคะแนน ปลดล็อคเมนู และเลื่อนไปเมนูถัดไป
     * ถ้าผิด: เพิ่ม wrongCount และคืน 0
     * @param selected รายชื่อวัตถุดิบที่ผู้เล่นเลือก
     * @return คะแนนที่ได้รับ หรือ 0 ถ้าตอบผิด
     */
    public int submit(List<String> selected) {
        Recipe r = current();
        if (r == null) return 0;

        if (!r.check(selected)) {
            wrongCount++;
            return 0;
        }

        int earned = r.baseScore + (timeLeft * r.timeBonus());
        chef.addScore(earned);
        chef.unlock(r.dish);
        recipeIndex++;
        resetTime();
        return earned;
    }

    /**
     * ข้ามเมนูปัจจุบัน (ใช้เมื่อเวลาหมด)
     * ไม่ได้คะแนน แต่เลื่อนไปเมนูถัดไปและรีเซ็ตเวลา
     */
    public void skip() {
        recipeIndex++;
        resetTime();
    }

    // ─── Timer ───────────────────────────────────────────────────────────────────

    /** ลดเวลาลง 1 วินาที (ถ้าเวลายังเหลืออยู่) */
    public void decrementTime() { if (timeLeft > 0) timeLeft--; }

    /** รีเซ็ตเวลาให้ตรงกับ timeLimit ของเมนูปัจจุบัน */
    public void resetTime()     { Recipe r = current(); timeLeft = (r != null) ? r.timeLimit() : 0; }

    /** คืนเวลาที่เหลืออยู่ (วินาที) */
    public int getTime()        { return timeLeft; }

    /** คืนจำนวนครั้งที่ตอบผิดในรอบนี้ */
    public int getWrongCount()  { return wrongCount; }

    // ─── Persistence ─────────────────────────────────────────────────────────────

    /** บันทึกคะแนนของเชฟลงไฟล์ scores.txt */
    public void save()                  { if (chef != null) repository.save(chef); }

    /** โหลด Leaderboard จากไฟล์ คืนรายการเรียงตามคะแนนสูงสุด TOP 5 */
    public List<Chef> loadLeaderboard() { return repository.loadLeaderboard(); }

    // ─── Private helpers ─────────────────────────────────────────────────────────

    /** กรองสูตรอาหารเฉพาะที่ตรงกับระดับความยากที่เลือก */
    private List<Recipe> filteredRecipes() {
        return allRecipes.stream()
                .filter(r -> r.diff == difficulty)
                .collect(Collectors.toList());
    }
}
