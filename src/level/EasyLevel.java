package level;

/**
 * EasyLevel.java
 * Concrete difficulty: 4×4 grid, 8 pairs.
 *
 * OOP: Inherits GameLevel; overrides getDescription() and getCardSize().
 */
public class EasyLevel extends GameLevel {

    public EasyLevel() {
        super("Easy", 4, 4);
    }

    @Override
    public String getDescription() {
        return "Best for beginners";
    }

    @Override
    public int getCardSize() {
        return 110; // Larger cards on a small grid
    }
}
