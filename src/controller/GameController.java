package controller;

import model.Card;
import model.Game;
import model.GameState;
import model.Score;
import ui.GameBoard;
import util.ScoreManager;

import javax.swing.Timer;

/**
 * GameController.java
 * Coordinates the game model and the game view (GameBoard).
 *
 * OOP Concepts:
 * - MVC: Controller sits between Model (Game) and View (GameBoard).
 * - Composition: Holds references to Game, GameBoard, timers.
 * - Encapsulation: Game logic is centralised here, not scattered in listeners.
 */
public class GameController {

    // ── Dependencies ──────────────────────────────────────────────────────────
    private Game          game;
    private GameBoard     board;
    private final ScoreManager scoreManager;

    // ── Timers ────────────────────────────────────────────────────────────────
    private Timer clockTimer;       // Fires every second to update HUD
    private Timer flipBackTimer;    // One-shot delay before hiding unmatched cards

    private static final int FLIP_BACK_DELAY_MS = 700;

    // ── Constructor ───────────────────────────────────────────────────────────
    public GameController() {
        this.scoreManager = new ScoreManager();
    }

    // ── Session management ────────────────────────────────────────────────────

    /**
     * Start a fresh game session with the given model and board.
     * Called each time the player starts or restarts a game.
     */
    public void startGame(Game game, GameBoard board) {
        stopAllTimers(); // Ensure nothing carries over from a previous session

        this.game  = game;
        this.board = board;

        game.start();
        startClockTimer();
    }

    /** Restart the current game (same player, same level). */
    public void restartGame() {
        stopAllTimers();
        game.restart();
        board.rebuildCards();
        startClockTimer();
    }

    /** Stop everything – called when leaving the game screen. */
    public void stopAllTimers() {
        if (clockTimer    != null && clockTimer.isRunning())    clockTimer.stop();
        if (flipBackTimer != null && flipBackTimer.isRunning()) flipBackTimer.stop();
    }

    // ── Card selection ────────────────────────────────────────────────────────

    /**
     * Handle a player clicking a card button.
     * This is the main game-loop entry point.
     */
    public void onCardClicked(Card card) {
        if (game == null || game.getState() == GameState.WON) return;

        // Block clicks while waiting for unmatched cards to flip back
        if (game.getState() == GameState.WAITING) return;

        boolean accepted = game.selectCard(card);
        if (!accepted) return;

        // Animate the clicked card face-up
        board.syncCardState(card, true);

        if (game.getState() == GameState.WAITING) {
            // Two cards are now face-up – evaluate after a short pause
            scheduleEvaluation();
        }
        // Update HUD immediately
        board.updateHud();
    }

    // ── Evaluation ────────────────────────────────────────────────────────────

    private void scheduleEvaluation() {
        flipBackTimer = new Timer(FLIP_BACK_DELAY_MS, e -> {
            flipBackTimer.stop();
            evaluate();
        });
        flipBackTimer.setRepeats(false);
        flipBackTimer.start();
    }

    private void evaluate() {
        boolean match = game.evaluateSelection();

        Card first  = game.getFirstSelected();
        Card second = game.getSecondSelected();

        if (match) {
            // Mark matched – both cards stay face-up with green tint
            board.syncCardState(first,  true);
            board.syncCardState(second, true);
            board.showMatchFeedback(true);

            game.clearSelection(); // Returns state to RUNNING

            if (game.isComplete()) {
                onGameWon();
                return;
            }
        } else {
            // Hide both cards
            board.showMatchFeedback(false);
            game.hideUnmatched();
            board.syncCardState(first,  true);
            board.syncCardState(second, true);
        }

        board.updateHud();
    }

    // ── Victory ───────────────────────────────────────────────────────────────

    private void onGameWon() {
        stopAllTimers();
        game.finalise();
        board.updateHud();

        // Save score
        Score score = new Score(
            game.getPlayer().getName(),
            game.getLevel().getName(),
            game.getScore(),
            game.getMoves(),
            game.getFormattedTime()
        );
        scoreManager.saveScore(score);

        // Delegate to board to show the victory dialog
        board.showVictory(score);
    }

    // ── Clock ─────────────────────────────────────────────────────────────────

    private void startClockTimer() {
        clockTimer = new Timer(1000, e -> {
            if (game != null && game.getState() == GameState.RUNNING) {
                board.updateHud();
            }
        });
        clockTimer.start();
    }

    // ── Accessors ─────────────────────────────────────────────────────────────
    public Game          getGame()         { return game;         }
    public ScoreManager  getScoreManager() { return scoreManager; }
}
