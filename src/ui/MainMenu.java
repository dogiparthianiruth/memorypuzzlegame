package ui;

import controller.GameController;
import level.EasyLevel;
import level.MediumLevel;
import level.HardLevel;
import model.Game;
import model.Player;
import util.UIUtils;
import util.ScoreManager;

import java.awt.*;
import javax.swing.*;

/**
 * MainMenu.java
 * The primary navigation hub of the application.
 *
 * OOP: Encapsulation (private fields), Composition (contains panels/buttons).
 * Uses a CardLayout to swap between screens inside the same JFrame.
 */
public class MainMenu extends JFrame {

    // ── Card names for CardLayout ─────────────────────────────────────────────
    public static final String CARD_MENU        = "MENU";
    public static final String CARD_SETUP       = "SETUP";
    public static final String CARD_GAME        = "GAME";
    public static final String CARD_HIGHSCORES  = "HIGHSCORES";
    public static final String CARD_HOW         = "HOW";
    public static final String CARD_SETTINGS    = "SETTINGS";

    // ── Core components ────────────────────────────────────────────────────────
    private final JPanel          rootPanel;
    private final CardLayout      cardLayout;
    private final GameController  controller;
    private final ScoreManager    scoreManager;

    // Lazily built panels
    private HighScorePanel  highScorePanel;
    private HowToPlayPanel  howToPlayPanel;
    private SettingsPanel   settingsPanel;
    private PlayerSetupPanel playerSetupPanel;
    private GameBoard        gameBoardPanel;

    // ── Constructor ────────────────────────────────────────────────────────────
    public MainMenu() {
        super("Memory Puzzle");
        this.controller   = new GameController();
        this.scoreManager = controller.getScoreManager();
        this.cardLayout   = new CardLayout();
        this.rootPanel    = new JPanel(cardLayout);
        rootPanel.setBackground(UIUtils.BG_DEEP);

        buildMenuPanel();
        configureFrame();
    }

    // ── Frame configuration ────────────────────────────────────────────────────
    private void configureFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(rootPanel);
        setMinimumSize(new Dimension(700, 600));
        setResizable(true);
        pack();
        setSize(820, 680);
        setLocationRelativeTo(null);
    }

    // ── Main Menu Panel ────────────────────────────────────────────────────────
    private void buildMenuPanel() {
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                UIUtils.applyRenderingHints(g2);
                UIUtils.paintGradientBackground(g2, getWidth(), getHeight(),
                    UIUtils.BG_DEEP, new Color(0x160A2E));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        panel.setOpaque(false);

        // ── Title area ─────────────────────────────────────────────────────────
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(60, 20, 20, 20));

        JLabel title1 = centeredLabel("MEMORY", UIUtils.font(Font.BOLD, 52f), UIUtils.TEXT_PRIMARY);
        JLabel title2 = centeredLabel("PUZZLE", UIUtils.font(Font.BOLD, 52f), UIUtils.ACCENT_PRIMARY);
        JLabel sub    = centeredLabel("Train your memory. Beat your best.",
                                      UIUtils.font(Font.ITALIC, 15f), UIUtils.TEXT_SECONDARY);

        titlePanel.add(title1);
        titlePanel.add(title2);
        titlePanel.add(Box.createVerticalStrut(10));
        titlePanel.add(sub);

        // ── Button area ────────────────────────────────────────────────────────
        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false);
        btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.Y_AXIS));
        btnPanel.setBorder(BorderFactory.createEmptyBorder(30, 120, 30, 120));

        btnPanel.add(menuButton("▶  PLAY GAME",    UIUtils.ACCENT_PRIMARY,
                                UIUtils.ACCENT_PRIMARY.brighter(), () -> showPlayerSetup()));
        btnPanel.add(Box.createVerticalStrut(12));
        btnPanel.add(menuButton("🏆  HIGH SCORES",  UIUtils.BG_PANEL,
                                UIUtils.BG_CARD, () -> showHighScores()));
        btnPanel.add(Box.createVerticalStrut(12));
        btnPanel.add(menuButton("❓  HOW TO PLAY",  UIUtils.BG_PANEL,
                                UIUtils.BG_CARD, () -> showHowToPlay()));
        btnPanel.add(Box.createVerticalStrut(12));
        btnPanel.add(menuButton("⚙  SETTINGS",      UIUtils.BG_PANEL,
                                UIUtils.BG_CARD, () -> showSettings()));
        btnPanel.add(Box.createVerticalStrut(12));
        btnPanel.add(menuButton("✕  EXIT",           new Color(0x3B1D1D),
                                UIUtils.ERROR.darker(), () -> System.exit(0)));

        // ── Footer ─────────────────────────────────────────────────────────────
        JLabel footer = new JLabel("Java OOP Project  •  B.Tech CSE", SwingConstants.CENTER);
        footer.setFont(UIUtils.smallFont());
        footer.setForeground(UIUtils.TEXT_SECONDARY);
        footer.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));

        panel.add(titlePanel, BorderLayout.NORTH);
        panel.add(btnPanel,   BorderLayout.CENTER);
        panel.add(footer,     BorderLayout.SOUTH);

        rootPanel.add(panel, CARD_MENU);
    }

    // ── Navigation helpers ─────────────────────────────────────────────────────

    public void showMainMenu() {
        // Stop any active game timer before returning
        controller.stopAllTimers();
        cardLayout.show(rootPanel, CARD_MENU);
        setSize(820, 680);
        setLocationRelativeTo(null);
    }

    private void showPlayerSetup() {
        if (playerSetupPanel == null) {
            playerSetupPanel = new PlayerSetupPanel(this);
            rootPanel.add(playerSetupPanel, CARD_SETUP);
        }
        playerSetupPanel.reset();
        cardLayout.show(rootPanel, CARD_SETUP);
    }

    public void launchGame(String playerName, String difficulty) {
        controller.stopAllTimers();

        // Build model
        level.GameLevel level = switch (difficulty) {
            case "Medium" -> new MediumLevel();
            case "Hard"   -> new HardLevel();
            default       -> new EasyLevel();
        };
        Player player = new Player(playerName);
        Game   game   = new Game(player, level);

        // Size the window for the grid
        int cardSize = level.getCardSize();
        int gap      = 10;
        int cols     = level.getGridCols();
        int rows     = level.getGridRows();
        int boardW   = cols * (cardSize + gap) + gap + 40;
        int boardH   = rows * (cardSize + gap) + gap + 200; // 200 for HUD
        int winW     = Math.max(boardW + 60, 700);
        int winH     = Math.max(boardH + 60, 600);

        // Build board
        gameBoardPanel = new GameBoard(game, controller, this);

        // Replace existing GAME card if any
        if (rootPanel.getComponentCount() > 0) {
            Component old = findCard(CARD_GAME);
            if (old != null) rootPanel.remove(old);
        }
        rootPanel.add(gameBoardPanel, CARD_GAME);

        setSize(winW, winH);
        setLocationRelativeTo(null);
        cardLayout.show(rootPanel, CARD_GAME);

        controller.startGame(game, gameBoardPanel);
    }

    private void showHighScores() {
        if (highScorePanel == null) {
            highScorePanel = new HighScorePanel(this, scoreManager);
            rootPanel.add(highScorePanel, CARD_HIGHSCORES);
        }
        highScorePanel.refresh();
        cardLayout.show(rootPanel, CARD_HIGHSCORES);
    }

    private void showHowToPlay() {
        if (howToPlayPanel == null) {
            howToPlayPanel = new HowToPlayPanel(this);
            rootPanel.add(howToPlayPanel, CARD_HOW);
        }
        cardLayout.show(rootPanel, CARD_HOW);
    }

    private void showSettings() {
        if (settingsPanel == null) {
            settingsPanel = new SettingsPanel(this, scoreManager);
            rootPanel.add(settingsPanel, CARD_SETTINGS);
        }
        cardLayout.show(rootPanel, CARD_SETTINGS);
    }

    // ── Factory helpers ────────────────────────────────────────────────────────

    private RoundedButton menuButton(String text, Color normal, Color hover, Runnable action) {
        RoundedButton btn = new RoundedButton(text, normal, hover);
        btn.setFont(UIUtils.font(Font.BOLD, 16f));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(340, 52));
        btn.setPreferredSize(new Dimension(340, 52));
        btn.addActionListener(e -> action.run());
        return btn;
    }

    private JLabel centeredLabel(String text, Font f, Color c) {
        JLabel lbl = new JLabel(text, SwingConstants.CENTER);
        lbl.setFont(f);
        lbl.setForeground(c);
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        return lbl;
    }

    private Component findCard(String name) {
        for (Component c : rootPanel.getComponents()) {
            // We can't query CardLayout by name directly, but we can use the
            // convention that GameBoard is the only instance of that class
            if (CARD_GAME.equals(name) && c instanceof GameBoard) return c;
        }
        return null;
    }
}
