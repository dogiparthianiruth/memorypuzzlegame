package model;

/**
 * Score.java
 * Immutable data record for a completed game result.
 *
 * OOP: Encapsulation; used in Collections (ArrayList, sorting).
 */
public class Score implements Comparable<Score> {

    private final String playerName;
    private final String difficulty;
    private final int    score;
    private final int    moves;
    private final String time;      // "MM:SS"

    public Score(String playerName, String difficulty, int score, int moves, String time) {
        this.playerName = playerName;
        this.difficulty = difficulty;
        this.score      = score;
        this.moves      = moves;
        this.time       = time;
    }

    // Getters
    public String getPlayerName() { return playerName; }
    public String getDifficulty() { return difficulty; }
    public int    getScore()      { return score;      }
    public int    getMoves()      { return moves;      }
    public String getTime()       { return time;       }

    /** Natural ordering: highest score first. */
    @Override
    public int compareTo(Score other) {
        return Integer.compare(other.score, this.score);
    }

    /** Serialize to a single CSV line for file storage. */
    public String toCsvLine() {
        return playerName + "," + difficulty + "," + score + "," + moves + "," + time;
    }

    /**
     * Deserialize from a CSV line.
     * Returns null if the line is malformed.
     */
    public static Score fromCsvLine(String line) {
        if (line == null || line.isBlank()) return null;
        String[] parts = line.split(",", 5);
        if (parts.length < 5) return null;
        try {
            String player = parts[0].trim();
            String diff   = parts[1].trim();
            int    sc     = Integer.parseInt(parts[2].trim());
            int    mv     = Integer.parseInt(parts[3].trim());
            String tm     = parts[4].trim();
            return new Score(player, diff, sc, mv, tm);
        } catch (NumberFormatException e) {
            return null; // Corrupted entry – skip gracefully
        }
    }

    @Override
    public String toString() {
        return playerName + " | " + difficulty + " | " + score + " pts | " + moves + " moves | " + time;
    }
}
