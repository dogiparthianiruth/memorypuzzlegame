package level;

/**
 * GameLevel.java
 * Abstract base class that defines the contract for all difficulty levels.
 *
 * OOP Concepts:
 * - Abstraction: This class defines what every level MUST provide without
 *   committing to concrete values.
 * - Inheritance: EasyLevel, MediumLevel, HardLevel all extend this class.
 * - Polymorphism: The Game and GameController use GameLevel references,
 *   so they work with any concrete level without if/else chains.
 */
public abstract class GameLevel {

    // ── Shared fields ─────────────────────────────────────────────────────────
    protected final String name;
    protected final int    gridRows;
    protected final int    gridCols;
    protected final int    pairs;

    // ── Constructor ───────────────────────────────────────────────────────────
    protected GameLevel(String name, int gridRows, int gridCols) {
        this.name     = name;
        this.gridRows = gridRows;
        this.gridCols = gridCols;
        this.pairs    = (gridRows * gridCols) / 2;
    }

    // ── Abstract behaviour ────────────────────────────────────────────────────

    /**
     * A human-readable one-line description of this difficulty.
     * Each subclass provides its own flavour text (method overriding).
     */
    public abstract String getDescription();

    /**
     * Preferred card size in pixels for this level's board layout.
     * Subclasses override to suit their grid density.
     */
    public abstract int getCardSize();

    // ── Concrete accessors ────────────────────────────────────────────────────
    public String getName()     { return name;     }
    public int    getGridRows() { return gridRows; }
    public int    getGridCols() { return gridCols; }
    public int    getPairs()    { return pairs;    }

    @Override
    public String toString() {
        return name + " (" + gridRows + "×" + gridCols + ", " + pairs + " pairs)";
    }
}
