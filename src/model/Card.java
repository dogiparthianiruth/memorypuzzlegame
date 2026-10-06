package model;

/**
 * Card.java
 * Represents a single card in the Memory Puzzle game.
 *
 * OOP Concepts Demonstrated:
 * - Encapsulation: All fields are private; state changes happen through methods.
 * - Abstraction: Callers work with reveal(), hide(), markMatched() without knowing internals.
 */
public class Card {

    // ── Fields ──────────────────────────────────────────────────────────────
    private final String symbol;   // The emoji/symbol on this card
    private final int    id;       // Unique card index (0-based)
    private boolean      revealed; // True while face-up (but not yet matched)
    private boolean      matched;  // True after a successful match

    // ── Constructor ──────────────────────────────────────────────────────────
    public Card(int id, String symbol) {
        this.id       = id;
        this.symbol   = symbol;
        this.revealed = false;
        this.matched  = false;
    }

    // ── State Transitions ────────────────────────────────────────────────────

    /** Flip this card face-up (does nothing if already matched). */
    public void reveal() {
        if (!matched) {
            revealed = true;
        }
    }

    /** Flip this card face-down (does nothing if matched). */
    public void hide() {
        if (!matched) {
            revealed = false;
        }
    }

    /** Lock this card as permanently matched. */
    public void markMatched() {
        this.matched  = true;
        this.revealed = true;
    }

    /** Reset card to its initial hidden state. */
    public void reset() {
        this.revealed = false;
        this.matched  = false;
    }

    // ── Getters ──────────────────────────────────────────────────────────────
    public String  getSymbol()   { return symbol;   }
    public int     getId()       { return id;        }
    public boolean isRevealed()  { return revealed;  }
    public boolean isMatched()   { return matched;   }

    // ── Object overrides ─────────────────────────────────────────────────────
    @Override
    public String toString() {
        return "Card{id=" + id + ", symbol='" + symbol + "', revealed=" + revealed + ", matched=" + matched + "}";
    }
}
