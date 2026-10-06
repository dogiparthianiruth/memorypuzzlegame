package level;

/**
 * MediumLevel.java
 * Concrete difficulty: 6×6 grid, 18 pairs.
 *
 * OOP: Inherits GameLevel; overrides getDescription() and getCardSize().
 */
public class MediumLevel extends GameLevel {

    public MediumLevel() {
        super("Medium", 6, 6);
    }

    @Override
    public String getDescription() {
        return "A balanced challenge";
    }

    @Override
    public int getCardSize() {
        return 90; // Medium cards
    }
}
