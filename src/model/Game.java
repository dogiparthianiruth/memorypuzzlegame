package model;

import level.GameLevel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Game.java
 * Central model that manages all game state.
 *
 * OOP Concepts:
 * - Encapsulation: Every field is private; all mutations go through methods.
 * - Composition: A Game is composed of a List<Card> and references a Player
 *   and a GameLevel.
 * - No Swing imports – pure logic layer.
 */
public class Game {

    // ── Symbol pool – 32 unique symbols (enough for 8×8 = 32 pairs) ──────────
    private static final String[] SYMBOL_POOL = {
        "🍎", "🚀", "🐱", "⭐", "🎵", "⚽", "🌈", "🔥",
        "🍕", "🎮", "🦋", "🌻", "🐼", "🎯", "🚗", "🍀",
        "🎈", "🌙", "☀", "🍉", "🐸", "🦊", "💎", "🎁",
        "🏀", "🍩", "🐵", "🌟", "🎲", "🎧", "🛸", "🍓"
    };

    // ── Fields ────────────────────────────────────────────────────────────────
    private final Player          player;
    private final GameLevel       level;
    private final List<Card>      cards;       // Composition

    private Card      firstSelected;
    private Card      secondSelected;

    private int       moves;
    private int       matches;
    private int       score;
    private GameState state;

    private long      startTimeMillis;
    private long      endTimeMillis;

    // ── Constructor ───────────────────────────────────────────────────────────
    public Game(Player player, GameLevel level) {
        this.player  = player;
        this.level   = level;
        this.cards   = new ArrayList<>();
        this.state   = GameState.IDLE;
        buildCards();
    }

    // ── Card generation ───────────────────────────────────────────────────────

    private void buildCards() {
        cards.clear();
        int pairs = level.getPairs();
        // Pick 'pairs' symbols from the pool
        List<String> symbolsToUse = new ArrayList<>();
        for (int i = 0; i < pairs; i++) {
            symbolsToUse.add(SYMBOL_POOL[i % SYMBOL_POOL.length]);
            symbolsToUse.add(SYMBOL_POOL[i % SYMBOL_POOL.length]); // duplicate
        }
        Collections.shuffle(symbolsToUse);
        for (int i = 0; i < symbolsToUse.size(); i++) {
            cards.add(new Card(i, symbolsToUse.get(i)));
        }
    }

    // ── Game lifecycle ────────────────────────────────────────────────────────

    /** Begin the game – called once the board is shown. */
    public void start() {
        moves            = 0;
        matches          = 0;
        score            = 0;
        firstSelected    = null;
        secondSelected   = null;
        state            = GameState.RUNNING;
        startTimeMillis  = System.currentTimeMillis();
        endTimeMillis    = 0;
    }

    /** Restart: reshuffle cards and reset counters. */
    public void restart() {
        for (Card c : cards) c.reset();
        buildCards();
        start();
    }

    // ── Card selection ────────────────────────────────────────────────────────

    /**
     * Try to select a card.
     * @return true if the selection was accepted, false if it must be ignored.
     */
    public boolean selectCard(Card card) {
        if (state != GameState.RUNNING) return false;
        if (card.isMatched())           return false;
        if (card.isRevealed())          return false; // Same card or already face-up

        card.reveal();

        if (firstSelected == null) {
            firstSelected = card;
            return true;
        }

        secondSelected = card;
        state          = GameState.WAITING;
        moves++;
        return true;
    }

    /**
     * Evaluate the two selected cards.
     * @return true if they match, false otherwise.
     */
    public boolean evaluateSelection() {
        if (firstSelected == null || secondSelected == null) return false;

        boolean match = firstSelected.getSymbol().equals(secondSelected.getSymbol());

        if (match) {
            firstSelected.markMatched();
            secondSelected.markMatched();
            matches++;
            score = Math.max(0, score + 100);
        } else {
            score = Math.max(0, score - 10);
        }
        return match;
    }

    /** Hide the two unmatched selected cards and reset selection. */
    public void hideUnmatched() {
        if (firstSelected  != null && !firstSelected.isMatched())  firstSelected.hide();
        if (secondSelected != null && !secondSelected.isMatched()) secondSelected.hide();
        clearSelection();
    }

    /** Clear selection state and return to RUNNING. */
    public void clearSelection() {
        firstSelected  = null;
        secondSelected = null;
        if (state == GameState.WAITING) state = GameState.RUNNING;
    }

    /** Check if all pairs are matched. */
    public boolean isComplete() {
        return matches == level.getPairs();
    }

    /** Finalise the game and apply completion bonuses. */
    public void finalise() {
        endTimeMillis = System.currentTimeMillis();
        state         = GameState.WON;
        applyBonuses();
    }

    // ── Scoring bonuses ────────────────────────────────────────────────────────

    private void applyBonuses() {
        long elapsedSec = getElapsedSeconds();
        int pairs       = level.getPairs();

        // Time bonus: up to +300; faster = more
        int timeBonus = 0;
        int bestTime  = pairs * 3; // heuristic "best possible" seconds
        if (elapsedSec <= bestTime) {
            timeBonus = 300;
        } else {
            long excess = elapsedSec - bestTime;
            timeBonus = (int) Math.max(0, 300 - excess * 2);
        }

        // Move bonus: up to +500; fewer moves = more
        int moveBonus   = 0;
        int minMoves    = pairs; // perfect game
        if (moves <= minMoves) {
            moveBonus = 500;
        } else {
            int extra = moves - minMoves;
            moveBonus = (int) Math.max(0, 500 - extra * 10);
        }

        score += timeBonus + moveBonus;
    }

    // ── Elapsed time ─────────────────────────────────────────────────────────

    public long getElapsedSeconds() {
        long end = (endTimeMillis > 0) ? endTimeMillis : System.currentTimeMillis();
        return (end - startTimeMillis) / 1000L;
    }

    public String getFormattedTime() {
        long secs = getElapsedSeconds();
        return String.format("%02d:%02d", secs / 60, secs % 60);
    }

    // ── Getters ───────────────────────────────────────────────────────────────
    public Player         getPlayer()         { return player;          }
    public GameLevel      getLevel()          { return level;           }
    public List<Card>     getCards()          { return cards;           }
    public int            getMoves()          { return moves;           }
    public int            getMatches()        { return matches;         }
    public int            getScore()          { return score;           }
    public GameState      getState()          { return state;           }
    public Card           getFirstSelected()  { return firstSelected;   }
    public Card           getSecondSelected() { return secondSelected;  }
}
