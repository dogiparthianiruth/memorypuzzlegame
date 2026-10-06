package level;

/**
 * HardLevel.java
 * Concrete difficulty: 8×8 grid, 32 pairs.
 *
 * OOP: Inherits GameLevel; overrides getDescription() and getCardSize().
 */
public class HardLevel extends GameLevel {

    public HardLevel() {
        super("Hard", 8, 8);
    }

    @Override
    public String getDescription() {
        return "For memory masters";
    }

    @Override
    public int getCardSize() {
        return 75; // Smaller cards to fit the denser grid
    }
}
