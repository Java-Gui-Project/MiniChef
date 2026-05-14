package model;

import java.util.*;
import java.util.stream.Collectors;

public class Recipe {
    public enum Difficulty { EASY, MEDIUM, HARD }

    public String dish, province;
    public int baseScore;
    public Difficulty diff;
    public List<Ingredient> ingredients = new ArrayList<>();

    public Recipe(String dish, String province, int baseScore, Difficulty diff) {
        this.dish      = dish;
        this.province  = province;
        this.baseScore = baseScore;
        this.diff      = diff;
    }

    public void add(String name, String emoji) {
        ingredients.add(new Ingredient(name, emoji));
    }

    /** Returns true only when selected contains exactly the required ingredients. */
    public boolean check(List<String> selected) {
        if (selected.size() != ingredients.size()) return false;
        List<String> required = ingredients.stream()
                .map(i -> i.name)
                .collect(Collectors.toList());
        return selected.containsAll(required);
    }

    public int decoys() {
        switch (diff) {
            case EASY:   return 2;
            case MEDIUM: return 4;
            default:     return 6;
        }
    }

    public int timeLimit() {
        switch (diff) {
            case EASY:   return 75;
            case MEDIUM: return 55;
            default:     return 35;
        }
    }

    public int timeBonus() {
        switch (diff) {
            case EASY:   return 2;
            case MEDIUM: return 3;
            default:     return 4;
        }
    }

    public String diffLabel() {
        switch (diff) {
            case EASY:   return "ง่าย";
            case MEDIUM: return "ปานกลาง";
            default:     return "ยาก";
        }
    }
}
