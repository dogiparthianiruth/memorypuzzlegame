package ui;

import controller.GameController;
import model.Card;
import model.Game;
import model.Score;
import util.UIUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * GameBoard.java
 * The main game view: HUD + grid of CardButtons.
 *
 * OOP: Composition (contains CardButtons, StatCards), MVC View layer.
 * Does NOT contain game logic – it delegates card clicks to GameController.
 */
public class GameBoard extends JPanel {

    // ── References ────────────────────────────────────────────────────────────
    private final Game            game;
    private final GameController  controller;
    private final MainMenu        mainMenu;

    // ── HUD widgets ───────────────────────────────────────────────────────────
    private StatCard timeCard;
    private StatCard movesCard;
    private StatCard matchCard;
    private StatCard scoreCard;

    // ── Card grid ─────────────────────────────────────────────────────────────
    private final List<CardButton> cardButtons = new ArrayList<>();
    private JPanel                 gridPanel;

    // ── Feedback label ────────────────────────────────────────────────────────
    private JLabel feedbackLabel;
    private Timer  feedbackTimer;

    // ── Constructor ────────────────────────────────────────────────────────────
    public GameBoard(Game game, GameController controller, MainMenu mainMenu) {
        super(new BorderLayout(0, 0));
        this.game       = game;
        this.controller = controller;
        this.mainMenu   = mainMenu;
        setOpaque(false);
        buildUI();
    }

    // ── UI construction ────────────────────────────────────────────────────────
    private void buildUI() {
        add(buildTopBar(),   BorderLayout.NORTH);
        add(buildGrid(),     BorderLayout.CENTER);
        add(buildBottomBar(), BorderLayout.SOUTH);
    }

    // ── Top bar: title + player info ─────────────────────────────────────────
    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                UIUtils.applyRenderingHints(g2);
                g2.setColor(UIUtils.BG_SECONDARY);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        bar.setOpaque(false);
        bar.setBorder(new EmptyBorder(14, 20, 10, 20));

        // Left: game title
        JLabel title = new JLabel("MEMORY PUZZLE");
        title.setFont(UIUtils.font(Font.BOLD, 22f));
        title.setForeground(UIUtils.ACCENT_PRIMARY);

        // Right: player + difficulty
        JPanel info = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        info.setOpaque(false);

        JLabel playerLbl = new JLabel("Player: " + game.getPlayer().getName());
        playerLbl.setFont(UIUtils.bodyFont());
        playerLbl.setForeground(UIUtils.TEXT_SECONDARY);

        JLabel diffLbl = new JLabel("Difficulty: " + game.getLevel().getName());
        diffLbl.setFont(UIUtils.bodyFont());
        diffLbl.setForeground(UIUtils.TEXT_SECONDARY);

        info.add(playerLbl);
        info.add(diffLbl);

        // HUD row
        JPanel hudRow = buildHudRow();

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        JPanel titleRow = new JPanel(new BorderLayout());
        titleRow.setOpaque(false);
        titleRow.add(title, BorderLayout.WEST);
        titleRow.add(info,  BorderLayout.EAST);

        top.add(titleRow, BorderLayout.NORTH);
        top.add(hudRow,   BorderLayout.CENTER);
        top.setBorder(new EmptyBorder(14, 20, 0, 20));
        return top;
    }

    // ── HUD stat cards row ────────────────────────────────────────────────────
    private JPanel buildHudRow() {
        timeCard  = new StatCard("Time",    "00:00", UIUtils.ACCENT_SECONDARY);
        movesCard = new StatCard("Moves",   "0",     UIUtils.WARNING);
        matchCard = new StatCard("Matches", "0 / " + game.getLevel().getPairs(), UIUtils.SUCCESS);
        scoreCard = new StatCard("Score",   "0",     UIUtils.ACCENT_PRIMARY);

        JPanel row = new JPanel(new GridLayout(1, 4, 12, 0));
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(12, 0, 12, 0));
        row.add(timeCard);
        row.add(movesCard);
        row.add(matchCard);
        row.add(scoreCard);
        return row;
    }

    // ── Card grid ─────────────────────────────────────────────────────────────
    private JPanel buildGrid() {
        int rows = game.getLevel().getGridRows();
        int cols = game.getLevel().getGridCols();
        int size = game.getLevel().getCardSize();
        int gap  = 10;

        gridPanel = new JPanel(new GridLayout(rows, cols, gap, gap)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                UIUtils.applyRenderingHints(g2);
                UIUtils.paintGradientBackground(g2, getWidth(), getHeight(),
                    UIUtils.BG_DEEP, new Color(0x160A2E));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        gridPanel.setOpaque(false);
        gridPanel.setBorder(new EmptyBorder(gap, gap + 20, gap, gap + 20));

        cardButtons.clear();
        for (Card card : game.getCards()) {
            CardButton btn = new CardButton(card, size);
            btn.addActionListener(e -> controller.onCardClicked(card));
            cardButtons.add(btn);
            gridPanel.add(btn);
        }

        // Scrollable in case window is too small
        JScrollPane scroll = new JScrollPane(gridPanel);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(scroll, BorderLayout.CENTER);
        return wrapper;
    }

    // ── Bottom bar: feedback + nav buttons ────────────────────────────────────
    private JPanel buildBottomBar() {
        JPanel bar = new JPanel(new BorderLayout(10, 0)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(UIUtils.BG_SECONDARY);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        bar.setOpaque(false);
        bar.setBorder(new EmptyBorder(10, 20, 14, 20));

        feedbackLabel = new JLabel(" ", SwingConstants.CENTER);
        feedbackLabel.setFont(UIUtils.font(Font.BOLD, 15f));
        feedbackLabel.setForeground(UIUtils.SUCCESS);

        RoundedButton restartBtn = new RoundedButton("↺ Restart",
            UIUtils.BG_PANEL, UIUtils.BG_CARD);
        restartBtn.addActionListener(e -> controller.restartGame());

        RoundedButton menuBtn = new RoundedButton("⌂ Main Menu",
            UIUtils.BG_PANEL, UIUtils.BG_CARD);
        menuBtn.addActionListener(e -> {
            controller.stopAllTimers();
            mainMenu.showMainMenu();
        });

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btns.setOpaque(false);
        btns.add(restartBtn);
        btns.add(menuBtn);

        bar.add(feedbackLabel, BorderLayout.CENTER);
        bar.add(btns,          BorderLayout.EAST);
        return bar;
    }

    // ── Rebuild card buttons when restarting ──────────────────────────────────
    public void rebuildCards() {
        gridPanel.removeAll();
        cardButtons.clear();
        int size = game.getLevel().getCardSize();
        for (Card card : game.getCards()) {
            CardButton btn = new CardButton(card, size);
            btn.addActionListener(e -> controller.onCardClicked(card));
            cardButtons.add(btn);
            gridPanel.add(btn);
        }
        gridPanel.revalidate();
        gridPanel.repaint();
        updateHud();
    }

    // ── View update methods called by controller ──────────────────────────────

    /** Update a single card's visual state. */
    public void syncCardState(Card card, boolean animate) {
        for (CardButton btn : cardButtons) {
            if (btn.getCard() == card) {
                btn.syncState(animate);
                break;
            }
        }
    }

    /** Refresh all HUD stat cards. */
    public void updateHud() {
        timeCard .setValue(game.getFormattedTime());
        movesCard.setValue(String.valueOf(game.getMoves()));
        matchCard.setValue(game.getMatches() + " / " + game.getLevel().getPairs());
        scoreCard.setValue(String.valueOf(game.getScore()));
    }

    /** Show a brief match / no-match feedback message. */
    public void showMatchFeedback(boolean matched) {
        if (feedbackTimer != null && feedbackTimer.isRunning()) feedbackTimer.stop();

        if (matched) {
            feedbackLabel.setText("✓  Match found!  +100");
            feedbackLabel.setForeground(UIUtils.SUCCESS);
        } else {
            feedbackLabel.setText("✗  No match  -10");
            feedbackLabel.setForeground(UIUtils.ERROR);
        }

        feedbackTimer = new Timer(1500, e -> {
            feedbackLabel.setText(" ");
            ((Timer) e.getSource()).stop();
        });
        feedbackTimer.setRepeats(false);
        feedbackTimer.start();
    }

    /** Called by controller when the game is won. */
    public void showVictory(Score score) {
        SwingUtilities.invokeLater(() -> {
            VictoryDialog dlg = new VictoryDialog(
                SwingUtilities.getWindowAncestor(this),
                score,
                game.getLevel().getPairs(),
                // Play Again
                () -> controller.restartGame(),
                // Main Menu
                () -> mainMenu.showMainMenu()
            );
            dlg.setVisible(true);
        });
    }
}
