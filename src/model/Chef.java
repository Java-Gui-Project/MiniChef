package model;

import java.util.*;

public class Chef {
    public String name, difficultyPreference;
    public int score;
    public List<String> unlockedDishes = new ArrayList<>();

    public Chef(String name, String difficultyPreference) {
        this.name                 = name;
        this.difficultyPreference = difficultyPreference;
        this.score                = 0;
    }

    public void addScore(int pts) { this.score += pts; }

    public void unlock(String dish) {
        if (!unlockedDishes.contains(dish)) unlockedDishes.add(dish);
    }

    /** Serializes to CSV: "name,score,difficulty,dish1;dish2" */
    @Override
    public String toString() {
        return String.format("%s,%d,%s,%s",
                name, score, difficultyPreference, String.join(";", unlockedDishes));
    }

    /** Parses a CSV line back into a Chef. Returns a fallback on error. */
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

    public String getRank() {
        if (score >= 400) return "เชฟระดับตำนาน! 🏆";
        if (score >= 200) return "เชฟมืออาชีพ 🍳";
        return "เชฟหน้าใหม่ 🔪";
    }
}
