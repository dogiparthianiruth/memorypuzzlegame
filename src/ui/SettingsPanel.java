package ui;

import util.ScoreManager;
import util.UIUtils;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * SettingsPanel.java
 * Settings screen: toggle options and reset high scores.
 *
 * OOP: Encapsulation (private settings state), Composition.
 */
public class SettingsPanel extends JPanel {

    private final MainMenu      mainMenu;
    private final ScoreManager  scoreManager;

    // Settings state
    private boolean soundEnabled     = true;
    private boolean animationEnabled = true;

    // Toggle button references for visual update
    private RoundedButton soundBtn;
    private RoundedButton animBtn;

    public SettingsPanel(MainMenu mainMenu, ScoreManager scoreManager) {
        super(new BorderLayout());
        this.mainMenu     = mainMenu;
        this.scoreManager = scoreManager;
        setOpaque(false);
        buildUI();
    }

    public boolean isAnimationEnabled() { return animationEnabled; }
    public boolean isSoundEnabled()     { return soundEnabled;     }

    // ── UI ─────────────────────────────────────────────────────────────────────
    private void buildUI() {
        JPanel bg = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                UIUtils.applyRenderingHints(g2);
                UIUtils.paintGradientBackground(g2, getWidth(), getHeight(),
                    UIUtils.BG_DEEP, new Color(0x160A2E));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        bg.setOpaque(false);

        // Header
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(new EmptyBorder(40, 40, 20, 40));
        header.add(centeredLabel("⚙  SETTINGS", UIUtils.font(Font.BOLD, 28f), UIUtils.TEXT_PRIMARY));
        header.add(Box.createVerticalStrut(6));
        header.add(centeredLabel("Customise your experience",
            UIUtils.font(Font.ITALIC, 13f), UIUtils.TEXT_SECONDARY));

        // Content
        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(20, 80, 20, 80));

        // Sound toggle
        content.add(sectionRow("🔊  Sound Effects",
            "Toggle in-game sound effects (architecture ready)",
            () -> {
                soundEnabled = !soundEnabled;
                updateToggleBtn(soundBtn, soundEnabled);
            }, true));
        content.add(Box.createVerticalStrut(16));

        // Animation toggle
        content.add(sectionRow("✨  Card Animations",
            "Toggle card flip animations",
            () -> {
                animationEnabled = !animationEnabled;
                updateToggleBtn(animBtn, animationEnabled);
            }, false));
        content.add(Box.createVerticalStrut(30));

        // Theme info
        content.add(sectionHeader("THEME"));
        content.add(infoRow("Current Theme:", "Dark Modern"));
        content.add(infoRow("Colour Scheme:", "Deep Blue / Purple Accent"));
        content.add(Box.createVerticalStrut(30));

        // Reset high scores
        content.add(sectionHeader("DATA"));
        RoundedButton resetBtn = new RoundedButton("🗑  Reset High Scores",
            new Color(0x3B1D1D), UIUtils.ERROR.darker());
        resetBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        resetBtn.setMaximumSize(new Dimension(240, 44));
        resetBtn.addActionListener(e -> confirmReset());
        content.add(resetBtn);

        // Back button
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 16));
        btnRow.setOpaque(false);
        RoundedButton back = new RoundedButton("← BACK", UIUtils.BG_PANEL, UIUtils.BG_CARD);
        back.addActionListener(e -> mainMenu.showMainMenu());
        btnRow.add(back);

        bg.add(header,  BorderLayout.NORTH);
        bg.add(content, BorderLayout.CENTER);
        bg.add(btnRow,  BorderLayout.SOUTH);
        add(bg, BorderLayout.CENTER);
    }

    // ── Helper builders ───────────────────────────────────────────────────────
    private JPanel sectionRow(String label, String desc,
                               Runnable toggle, boolean isSound) {
        JPanel row = new JPanel(new BorderLayout(12, 4));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel textSide = new JPanel();
        textSide.setOpaque(false);
        textSide.setLayout(new BoxLayout(textSide, BoxLayout.Y_AXIS));

        JLabel lbl = new JLabel(label);
        lbl.setFont(UIUtils.font(Font.BOLD, 15f));
        lbl.setForeground(UIUtils.TEXT_PRIMARY);

        JLabel dsc = new JLabel(desc);
        dsc.setFont(UIUtils.smallFont());
        dsc.setForeground(UIUtils.TEXT_SECONDARY);

        textSide.add(lbl);
        textSide.add(dsc);

        RoundedButton btn = new RoundedButton("ON",
            UIUtils.SUCCESS.darker(), UIUtils.SUCCESS.darker().brighter());
        btn.setPreferredSize(new Dimension(80, 36));

        if (isSound) { soundBtn = btn; }
        else         { animBtn  = btn; }

        btn.addActionListener(e -> toggle.run());

        row.add(textSide, BorderLayout.CENTER);
        row.add(btn,      BorderLayout.EAST);
        return row;
    }

    private void updateToggleBtn(RoundedButton btn, boolean on) {
        btn.setText(on ? "ON" : "OFF");
        // Colour change is handled by RoundedButton's paint; just repaint
        btn.repaint();
    }

    private JLabel sectionHeader(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(UIUtils.labelFont());
        lbl.setForeground(UIUtils.ACCENT_SECONDARY);
        lbl.setBorder(new EmptyBorder(0, 0, 8, 0));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JPanel infoRow(String label, String value) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 2));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel l = new JLabel(label);
        l.setFont(UIUtils.bodyFont());
        l.setForeground(UIUtils.TEXT_SECONDARY);
        l.setPreferredSize(new Dimension(150, 20));

        JLabel v = new JLabel(value);
        v.setFont(UIUtils.font(Font.BOLD, 14f));
        v.setForeground(UIUtils.TEXT_PRIMARY);

        row.add(l);
        row.add(v);
        return row;
    }

    private void confirmReset() {
        int choice = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete all high scores?\nThis cannot be undone.",
            "Reset High Scores", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (choice == JOptionPane.YES_OPTION) {
            scoreManager.clearScores();
            JOptionPane.showMessageDialog(this,
                "High scores have been reset.", "Done", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private JLabel centeredLabel(String text, Font f, Color c) {
        JLabel lbl = new JLabel(text, SwingConstants.CENTER);
        lbl.setFont(f);
        lbl.setForeground(c);
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        return lbl;
    }
}
