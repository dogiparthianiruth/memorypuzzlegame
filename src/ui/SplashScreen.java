package ui;

import util.UIUtils;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;

/**
 * SplashScreen.java
 * Timed introductory window shown for ~2 seconds on startup.
 *
 * OOP: Inheritance (extends JWindow), method overriding (paintComponent).
 */
public class SplashScreen extends JWindow {

    private JProgressBar progressBar;
    private Timer        loadTimer;
    private int          progress = 0;

    /** Callback fired when the splash is done. */
    private final Runnable onComplete;

    public SplashScreen(Runnable onComplete) {
        this.onComplete = onComplete;
        buildUI();
        pack();
        setLocationRelativeTo(null);
    }

    // ── UI Construction ───────────────────────────────────────────────────────

    private void buildUI() {
        JPanel content = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                UIUtils.applyRenderingHints(g2);
                UIUtils.paintGradientBackground(g2, getWidth(), getHeight(),
                    UIUtils.BG_DEEP, new Color(0x1A0E2E));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        content.setOpaque(false);
        content.setPreferredSize(new Dimension(480, 300));
        content.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));

        // Title
        JLabel titleLabel = new JLabel("MEMORY", SwingConstants.CENTER);
        titleLabel.setFont(UIUtils.font(Font.BOLD, 56f));
        titleLabel.setForeground(UIUtils.TEXT_PRIMARY);

        JLabel puzzleLabel = new JLabel("PUZZLE", SwingConstants.CENTER);
        puzzleLabel.setFont(UIUtils.font(Font.BOLD, 56f));
        puzzleLabel.setForeground(UIUtils.ACCENT_PRIMARY);

        JLabel taglineLabel = new JLabel("Train your memory. Beat your best.", SwingConstants.CENTER);
        taglineLabel.setFont(UIUtils.font(Font.ITALIC, 14f));
        taglineLabel.setForeground(UIUtils.TEXT_SECONDARY);

        // Progress bar
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(false);
        progressBar.setForeground(UIUtils.ACCENT_PRIMARY);
        progressBar.setBackground(UIUtils.BG_PANEL);
        progressBar.setBorder(BorderFactory.createEmptyBorder());
        progressBar.setPreferredSize(new Dimension(300, 5));

        JPanel barWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        barWrapper.setOpaque(false);
        barWrapper.add(progressBar);

        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        puzzleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        taglineLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        barWrapper.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(titleLabel);
        centerPanel.add(puzzleLabel);
        centerPanel.add(Box.createVerticalStrut(16));
        centerPanel.add(taglineLabel);
        centerPanel.add(Box.createVerticalStrut(30));
        centerPanel.add(barWrapper);
        centerPanel.add(Box.createVerticalGlue());

        content.add(centerPanel, BorderLayout.CENTER);
        setContentPane(content);

        // Round the window corners slightly
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 24, 24));
            }
        });
    }

    // ── Animation ─────────────────────────────────────────────────────────────

    public void start() {
        setVisible(true);
        loadTimer = new Timer(20, e -> {
            progress += 2;
            progressBar.setValue(Math.min(progress, 100));
            if (progress >= 100) {
                loadTimer.stop();
                dispose();
                onComplete.run();
            }
        });
        loadTimer.start();
    }
}
