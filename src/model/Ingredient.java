package model;

public class Ingredient {
    public String name, emoji;

    public Ingredient(String name, String emoji) {
        this.name  = name;
        this.emoji = emoji;
    }

    @Override
    public String toString() {
        return emoji + " " + name;
    }
}
