package ui;

import model.Score;
import util.UIUtils;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * VictoryDialog.java
 * Modal dialog shown when all pairs are matched.
 *
 * OOP: Encapsulation (private callbacks), Composition (contains panels/labels).
 */
public class VictoryDialog extends JDialog {

    private final Score    score;
    private final int      totalPairs;
    private final Runnable onPlayAgain;
    private final Runnable onMainMenu;

    // Confetti animation
    private Timer      confettiTimer;
    private int[][]    confettiData; // [x, y, dx, dy, colorIdx] per particle
    private static final Color[] CONFETTI_COLORS = {
        UIUtils.ACCENT_PRIMARY, UIUtils.SUCCESS, UIUtils.WARNING,
        UIUtils.ACCENT_SECONDARY, UIUtils.TEXT_PRIMARY
    };
    private static final int PARTICLE_COUNT = 60;

    public VictoryDialog(Window owner, Score score, int totalPairs,
                         Runnable onPlayAgain, Runnable onMainMenu) {
        super(owner, "Victory!", ModalityType.APPLICATION_MODAL);
        this.score       = score;
        this.totalPairs  = totalPairs;
        this.onPlayAgain = onPlayAgain;
        this.onMainMenu  = onMainMenu;
        buildUI();
        initConfetti();
        pack();
        setLocationRelativeTo(owner);
        setResizable(false);
    }

    // ── UI ─────────────────────────────────────────────────────────────────────
    private void buildUI() {
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        // Canvas with confetti
        JPanel canvas = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                UIUtils.applyRenderingHints(g2);
                UIUtils.paintGradientBackground(g2, getWidth(), getHeight(),
                    UIUtils.BG_DEEP, new Color(0x1A0E2E));
                drawConfetti(g2);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        canvas.setOpaque(false);
        canvas.setPreferredSize(new Dimension(480, 480));
        canvas.setBorder(new EmptyBorder(30, 40, 30, 40));

        // Content
        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        // Trophy emoji
        JLabel trophy = centeredLabel("🎉", new Font("Segoe UI Emoji", Font.PLAIN, 60),
                                       UIUtils.WARNING);
        // Header
        JLabel wonLabel = centeredLabel("YOU DID IT!",
            UIUtils.font(Font.BOLD, 34f), UIUtils.TEXT_PRIMARY);
        JLabel congrats = centeredLabel("Congratulations, " + score.getPlayerName() + "!",
            UIUtils.font(Font.ITALIC, 16f), UIUtils.TEXT_SECONDARY);

        // Stats panel
        JPanel statsPanel = buildStatsPanel();

        // Buttons
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 0));
        btnRow.setOpaque(false);

        RoundedButton playAgainBtn = new RoundedButton("▶  PLAY AGAIN",
            UIUtils.ACCENT_PRIMARY, UIUtils.ACCENT_PRIMARY.brighter());
        playAgainBtn.setPreferredSize(new Dimension(160, 44));
        playAgainBtn.addActionListener(e -> {
            stopConfetti();
            dispose();
            onPlayAgain.run();
        });

        RoundedButton menuBtn = new RoundedButton("⌂  MAIN MENU",
            UIUtils.BG_PANEL, UIUtils.BG_CARD);
        menuBtn.setPreferredSize(new Dimension(160, 44));
        menuBtn.addActionListener(e -> {
            stopConfetti();
            dispose();
            onMainMenu.run();
        });

        btnRow.add(playAgainBtn);
        btnRow.add(menuBtn);

        content.add(trophy);
        content.add(Box.createVerticalStrut(4));
        content.add(wonLabel);
        content.add(Box.createVerticalStrut(6));
        content.add(congrats);
        content.add(Box.createVerticalStrut(24));
        content.add(statsPanel);
        content.add(Box.createVerticalStrut(24));
        content.add(btnRow);

        canvas.add(content, BorderLayout.CENTER);
        setContentPane(canvas);
    }

    private JPanel buildStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 10, 0));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(statBox("FINAL SCORE", String.valueOf(score.getScore()), UIUtils.ACCENT_PRIMARY));
        panel.add(statBox("TIME",        score.getTime(),                   UIUtils.ACCENT_SECONDARY));
        panel.add(statBox("MOVES",       String.valueOf(score.getMoves()),   UIUtils.WARNING));
        panel.add(statBox("MATCHES",     totalPairs + " / " + totalPairs,  UIUtils.SUCCESS));
        return panel;
    }

    private StatCard statBox(String title, String value, Color accent) {
        StatCard sc = new StatCard(title, value, accent);
        sc.setPreferredSize(new Dimension(100, 80));
        return sc;
    }

    private JLabel centeredLabel(String text, Font f, Color c) {
        JLabel lbl = new JLabel(text, SwingConstants.CENTER);
        lbl.setFont(f);
        lbl.setForeground(c);
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        return lbl;
    }

    // ── Confetti ──────────────────────────────────────────────────────────────
    private void initConfetti() {
        confettiData = new int[PARTICLE_COUNT][5];
        for (int i = 0; i < PARTICLE_COUNT; i++) {
            resetParticle(i, true);
        }
        confettiTimer = new Timer(30, e -> {
            for (int i = 0; i < PARTICLE_COUNT; i++) {
                confettiData[i][1] += confettiData[i][3]; // y += dy
                confettiData[i][0] += confettiData[i][2]; // x += dx
                if (confettiData[i][1] > 480) resetParticle(i, false);
            }
            repaint();
        });
        confettiTimer.start();
    }

    private void resetParticle(int i, boolean initial) {
        int w = 480;
        confettiData[i][0] = (int)(Math.random() * w);                         // x
        confettiData[i][1] = initial ? (int)(Math.random() * 480) : -10;       // y
        confettiData[i][2] = (int)(Math.random() * 4) - 2;                     // dx
        confettiData[i][3] = 3 + (int)(Math.random() * 4);                     // dy
        confettiData[i][4] = (int)(Math.random() * CONFETTI_COLORS.length);    // colorIdx
    }

    private void drawConfetti(Graphics2D g2) {
        if (confettiData == null) return;
        for (int[] p : confettiData) {
            g2.setColor(CONFETTI_COLORS[p[4]]);
            g2.fillOval(p[0], p[1], 7, 7);
        }
    }

    private void stopConfetti() {
        if (confettiTimer != null && confettiTimer.isRunning()) confettiTimer.stop();
    }

    @Override
    public void dispose() {
        stopConfetti();
        super.dispose();
    }
}
