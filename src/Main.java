import ui.MainMenu;
import ui.SplashScreen;
import util.UIUtils;

import javax.swing.SwingUtilities;

/**
 * Main.java
 * Application entry point.
 *
 * Responsibilities:
 *  1. Apply the custom Look-and-Feel overrides.
 *  2. Show the SplashScreen.
 *  3. On splash completion, open the MainMenu on the EDT.
 *
 * OOP: SwingUtilities.invokeLater() ensures all Swing creation
 *      happens on the Event Dispatch Thread (thread-safety).
 */
public class Main {

    public static void main(String[] args) {
        // Apply custom LAF settings before any component is created
        UIUtils.setLookAndFeel();

        SwingUtilities.invokeLater(() -> {
            SplashScreen splash = new SplashScreen(() -> {
                // This callback fires when the splash finishes
                SwingUtilities.invokeLater(() -> {
                    MainMenu menu = new MainMenu();
                    menu.setVisible(true);
                });
            });
            splash.start();
        });
    }
}
