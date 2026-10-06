package ui;

import util.UIUtils;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * HowToPlayPanel.java
 * Instructional screen explaining game rules and scoring.
 *
 * OOP: Composition (helper methods build sections), Encapsulation.
 */
public class HowToPlayPanel extends JPanel {

    private final MainMenu mainMenu;

    public HowToPlayPanel(MainMenu mainMenu) {
        super(new BorderLayout());
        this.mainMenu = mainMenu;
        setOpaque(false);
        buildUI();
    }

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
        header.setBorder(new EmptyBorder(36, 40, 14, 40));
        header.add(centeredLabel("❓  HOW TO PLAY",
            UIUtils.font(Font.BOLD, 28f), UIUtils.TEXT_PRIMARY));
        header.add(Box.createVerticalStrut(6));
        header.add(centeredLabel("Everything you need to know",
            UIUtils.font(Font.ITALIC, 13f), UIUtils.TEXT_SECONDARY));

        // Scrollable content
        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(10, 50, 10, 50));

        content.add(sectionLabel("OBJECTIVE"));
        content.add(bodyText("Match every pair of cards using as few moves and as little time as possible."));
        content.add(Box.createVerticalStrut(20));

        content.add(sectionLabel("HOW TO PLAY"));
        content.add(numberedStep("1.", "Select any face-down card to reveal it."));
        content.add(numberedStep("2.", "Select a second face-down card."));
        content.add(numberedStep("3.", "If they match, both stay revealed  ✓"));
        content.add(numberedStep("4.", "If they don't match, both flip back  ✗"));
        content.add(numberedStep("5.", "Match every pair to win the game!"));
        content.add(Box.createVerticalStrut(20));

        content.add(sectionLabel("SCORING"));
        content.add(scoreLine("✓  Correct match",    "+100 pts",  UIUtils.SUCCESS));
        content.add(scoreLine("✗  Wrong pair",        "−10 pts",   UIUtils.ERROR));
        content.add(scoreLine("⏱  Time bonus",        "up to +300",UIUtils.ACCENT_SECONDARY));
        content.add(scoreLine("🎯  Move bonus",        "up to +500",UIUtils.WARNING));
        content.add(Box.createVerticalStrut(20));

        content.add(sectionLabel("DIFFICULTIES"));
        content.add(diffLine("Easy",   "4 × 4",  "8 pairs",  "Best for beginners"));
        content.add(diffLine("Medium", "6 × 6",  "18 pairs", "A balanced challenge"));
        content.add(diffLine("Hard",   "8 × 8",  "32 pairs", "For memory masters"));

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        // Back button
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 16));
        btnRow.setOpaque(false);
        RoundedButton back = new RoundedButton("← BACK", UIUtils.BG_PANEL, UIUtils.BG_CARD);
        back.addActionListener(e -> mainMenu.showMainMenu());
        btnRow.add(back);

        bg.add(header, BorderLayout.NORTH);
        bg.add(scroll, BorderLayout.CENTER);
        bg.add(btnRow, BorderLayout.SOUTH);
        add(bg, BorderLayout.CENTER);
    }

    // ── Section builders ──────────────────────────────────────────────────────
    private JLabel sectionLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(UIUtils.font(Font.BOLD, 14f));
        lbl.setForeground(UIUtils.ACCENT_SECONDARY);
        lbl.setBorder(new EmptyBorder(0, 0, 6, 0));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JLabel bodyText(String text) {
        JLabel lbl = new JLabel("<html><div style='width:420px'>" + text + "</div></html>");
        lbl.setFont(UIUtils.bodyFont());
        lbl.setForeground(UIUtils.TEXT_PRIMARY);
        lbl.setBorder(new EmptyBorder(0, 0, 4, 0));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JPanel numberedStep(String num, String text) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 2));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel n = new JLabel(num);
        n.setFont(UIUtils.font(Font.BOLD, 14f));
        n.setForeground(UIUtils.ACCENT_PRIMARY);
        n.setPreferredSize(new Dimension(20, 20));

        JLabel t = new JLabel(text);
        t.setFont(UIUtils.bodyFont());
        t.setForeground(UIUtils.TEXT_PRIMARY);

        row.add(n);
        row.add(t);
        return row;
    }

    private JPanel scoreLine(String label, String value, Color valueColor) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 2));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel l = new JLabel(label);
        l.setFont(UIUtils.bodyFont());
        l.setForeground(UIUtils.TEXT_SECONDARY);
        l.setPreferredSize(new Dimension(200, 20));

        JLabel v = new JLabel(value);
        v.setFont(UIUtils.font(Font.BOLD, 14f));
        v.setForeground(valueColor);

        row.add(l);
        row.add(v);
        return row;
    }

    private JPanel diffLine(String name, String grid, String pairs, String desc) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 2));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel n = new JLabel(name);
        n.setFont(UIUtils.font(Font.BOLD, 14f));
        n.setForeground(UIUtils.ACCENT_PRIMARY);
        n.setPreferredSize(new Dimension(70, 20));

        JLabel g = new JLabel(grid + "  •  " + pairs + "  •  " + desc);
        g.setFont(UIUtils.bodyFont());
        g.setForeground(UIUtils.TEXT_SECONDARY);

        row.add(n);
        row.add(g);
        return row;
    }

    private JLabel centeredLabel(String text, Font f, Color c) {
        JLabel lbl = new JLabel(text, SwingConstants.CENTER);
        lbl.setFont(f);
        lbl.setForeground(c);
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        return lbl;
    }
}
