package util;

import model.Score;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ScoreManager.java
 * Handles persistent storage of high scores in a plain-text CSV file.
 *
 * OOP: Encapsulation (private path), Exception handling, Java File I/O.
 * The file is stored in the user's home directory for easy access.
 */
public class ScoreManager {

    private static final int  MAX_SCORES = 10;
    private static final Path SCORE_FILE =
        Paths.get(System.getProperty("user.home"), "MemoryPuzzleScores.txt");

    // ── Public API ────────────────────────────────────────────────────────────

    /**
     * Save a new score, keep the top-10, and persist.
     * Silently ignores IOExceptions so the game never crashes due to file issues.
     */
    public void saveScore(Score score) {
        List<Score> scores = loadScores();
        scores.add(score);
        Collections.sort(scores);
        if (scores.size() > MAX_SCORES) {
            scores = scores.subList(0, MAX_SCORES);
        }
        persistScores(scores);
    }

    /**
     * Load all saved scores from disk.
     * Returns an empty list if the file doesn't exist or is unreadable.
     */
    public List<Score> loadScores() {
        List<Score> scores = new ArrayList<>();
        if (!Files.exists(SCORE_FILE)) return scores;
        try (BufferedReader reader = Files.newBufferedReader(SCORE_FILE)) {
            String line;
            while ((line = reader.readLine()) != null) {
                Score s = Score.fromCsvLine(line);
                if (s != null) scores.add(s); // Null = malformed line, skip
            }
        } catch (IOException e) {
            // Return whatever was read successfully
        }
        Collections.sort(scores);
        return scores;
    }

    /** Delete all saved scores. */
    public void clearScores() {
        try {
            Files.deleteIfExists(SCORE_FILE);
        } catch (IOException ignored) {}
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    private void persistScores(List<Score> scores) {
        try (BufferedWriter writer = Files.newBufferedWriter(SCORE_FILE)) {
            for (Score s : scores) {
                writer.write(s.toCsvLine());
                writer.newLine();
            }
        } catch (IOException ignored) {}
    }
}
