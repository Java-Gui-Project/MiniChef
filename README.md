# เชฟน้อยเมืองเหนือ — Mini Chef Chiangmai

A Java Swing quiz game where players identify the correct ingredients for traditional Northern Thai dishes within a time limit.

---

## Gameplay

1. Enter your chef name on the main menu.
2. Choose a difficulty level — Easy, Medium, or Hard.
3. A dish name is shown at the top of the screen.
4. Select the correct ingredients from the shuffled grid (decoy ingredients are mixed in).
5. Press **ส่งอาหาร** (Submit) before time runs out.
6. Earn points based on base score + time remaining × difficulty multiplier.
7. Complete all dishes to see your final score and the leaderboard.

If time runs out on a dish, it is skipped automatically and the next one begins.

---

## Difficulty Levels

| Level | Dish | Time Limit | Decoys | Time Bonus |
|-------|------|------------|--------|------------|
| 🐣 Easy | ข้าวซอย | 75 sec | 2 | ×2 |
| 👨‍🍳 Medium | แกงฮังเล | 55 sec | 4 | ×3 |
| 🔥 Hard | น้ำพริกหนุ่ม | 35 sec | 6 | ×4 |

### Score Formula
```
score = baseScore + (timeLeft × timeBonus)
```

---

## Project Structure

```
src/
  model/
    Ingredient.java        — Ingredient name and emoji
    Recipe.java            — Recipe data, difficulty rules, ingredient validation
    Chef.java              — Player state, score tracking, CSV serialization

  repository/
    ScoreRepository.java   — Reads and writes scores.txt

  game/
    GameManager.java       — Game state: recipe progression, timer, scoring

  ui/
    Navigator.java         — Interface for switching between screens
    Theme.java             — Shared colors, fonts, and UI factory methods
    MenuPanel.java         — Main menu screen
    DifficultyPanel.java   — Difficulty selection screen
    GamePanel.java         — Active gameplay screen
    ResultPanel.java       — End-of-game summary and leaderboard
    LeaderboardPanel.java  — Standalone leaderboard screen
    ChefThailandGame.java  — JFrame entry point, wires all panels together
```

### Dependency flow
```
ui  →  game  →  model
ui  →  model
repository  →  model
game  →  repository
```

---

## How to Run

### Requirements
- Java 11 or higher
- IntelliJ IDEA (recommended) or any Java IDE

### IntelliJ IDEA
1. Open the `untitled` folder as a project.
2. Make sure `src/` is marked as the **Sources Root**.
3. Right-click `src/ui/ChefThailandGame.java` → **Run 'ChefThailandGame.main()'**.

### Command Line
```bash
# From the project root
javac -d out/production/untitled src/model/*.java src/repository/*.java src/game/*.java src/ui/*.java
java -cp out/production/untitled ui.ChefThailandGame
```

---

## Save File

Scores are saved to `scores.txt` in the working directory (CSV format):

```
name,score,difficulty,dish1;dish2
```

Example:
```
Somchai,320,EASY,ข้าวซอย
Malee,480,HARD,น้ำพริกหนุ่ม
```

The leaderboard displays the top 5 scores across all sessions.
