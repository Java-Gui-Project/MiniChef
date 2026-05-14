package repository;

import model.Chef;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.*;

/** Handles all reading and writing of scores.txt. */
public class ScoreRepository {
    private static final String SAVE_FILE   = "scores.txt";
    private static final int    TOP_DISPLAY = 5;

    public void save(Chef chef) {
        try (BufferedWriter w = new BufferedWriter(new FileWriter(SAVE_FILE, true))) {
            w.write(chef.toString());
            w.newLine();
        } catch (IOException e) {
            System.err.println("ไม่สามารถบันทึกคะแนนได้: " + e.getMessage());
        }
    }

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
