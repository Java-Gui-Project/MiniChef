package game;

import model.Chef;
import model.Ingredient;
import model.Recipe;
import repository.ScoreRepository;

import java.util.*;
import java.util.stream.*;

/** Controls game state: recipe progression, scoring, and the countdown timer. */
public class GameManager {
    public List<Recipe> allRecipes = new ArrayList<>();
    public Chef              chef;
    public int               recipeIndex;
    public int               timeLeft;
    public int               wrongCount = 0;
    public Recipe.Difficulty difficulty;

    private final ScoreRepository repository = new ScoreRepository();

    public GameManager() {
        buildRecipes();
    }

    // ─── Setup ───────────────────────────────────────────────────────────────────

    private void buildRecipes() {
        Recipe easy = new Recipe("ข้าวซอย", "เชียงใหม่", 100, Recipe.Difficulty.EASY);
        easy.add("เส้นบะหมี่", "🍜");
        easy.add("ไก่",    "🍗");
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
        medium.add("น้ำปลา",   "🐟");
        allRecipes.add(medium);

        Recipe hard = new Recipe("น้ำพริกหนุ่ม", "เชียงใหม่", 220, Recipe.Difficulty.HARD);
        hard.add("พริกหนุ่ม", "🌶️");
        hard.add("หอมแดง",    "🧅");
        hard.add("กระเทียม",  "🧄");
        hard.add("กะปิ",      "🦐");
        hard.add("มะเขือเทศ",      "🍅");
        hard.add("เกลือ",     "🧂");
        hard.add("น้ำปลา",     "🐟");
        allRecipes.add(hard);
    }

    // ─── Game flow ───────────────────────────────────────────────────────────────

    public void start(String name, Recipe.Difficulty d) {
        if (name == null || name.isEmpty()) name = "Guest";
        this.difficulty  = d;
        this.chef        = new Chef(name, d.name());
        this.recipeIndex = 0;
        this.wrongCount  = 0;
        resetTime();
    }

    /** Returns the current recipe for the selected difficulty, or null when finished. */
    public Recipe current() {
        List<Recipe> filtered = filteredRecipes();
        return recipeIndex < filtered.size() ? filtered.get(recipeIndex) : null;
    }

    public boolean hasNext() {
        return recipeIndex < filteredRecipes().size();
    }

    /**
     * Validates the player's ingredient selection.
     * @return points earned, or 0 on a wrong answer.
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

    public void skip() {
        recipeIndex++;
        resetTime();
    }

    // ─── Timer ───────────────────────────────────────────────────────────────────

    public void decrementTime() { if (timeLeft > 0) timeLeft--; }
    public void resetTime()     { Recipe r = current(); timeLeft = (r != null) ? r.timeLimit() : 0; }
    public int  getTime()       { return timeLeft; }
    public int  getWrongCount() { return wrongCount; }

    // ─── Persistence ─────────────────────────────────────────────────────────────

    public void save()                  { if (chef != null) repository.save(chef); }
    public List<Chef> loadLeaderboard() { return repository.loadLeaderboard(); }

    // ─── Private helpers ─────────────────────────────────────────────────────────

    private List<Recipe> filteredRecipes() {
        return allRecipes.stream()
                .filter(r -> r.diff == difficulty)
                .collect(Collectors.toList());
    }
}
