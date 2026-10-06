package ui;

import util.UIUtils;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * PlayerSetupPanel.java
 * Collects player name and difficulty selection before starting a game.
 *
 * OOP: Encapsulation (private fields), Composition (difficulty cards).
 */
public class PlayerSetupPanel extends JPanel {

    private final MainMenu      mainMenu;
    private       JTextField    nameField;
    private       String        selectedDifficulty = "Easy";

    // Difficulty card references for visual toggle
    private RoundedPanel easyCard;
    private RoundedPanel mediumCard;
    private RoundedPanel hardCard;

    public PlayerSetupPanel(MainMenu mainMenu) {
        super(new BorderLayout());
        this.mainMenu = mainMenu;
        setOpaque(false);
        buildUI();
    }

    // ── Reset state when revisited ─────────────────────────────────────────────
    public void reset() {
        if (nameField != null) nameField.setText("");
        selectedDifficulty = "Easy";
        highlightDifficulty("Easy");
    }

    // ── UI build ─────────────────────────────────────────────────────────────
    private void buildUI() {
        // Background
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

        // ── Header ─────────────────────────────────────────────────────────────
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(new EmptyBorder(40, 40, 10, 40));

        JLabel title = centeredLabel("WHO'S PLAYING?",
            UIUtils.font(Font.BOLD, 30f), UIUtils.TEXT_PRIMARY);
        JLabel sub   = centeredLabel("Enter your name and choose a difficulty",
            UIUtils.font(Font.ITALIC, 14f), UIUtils.TEXT_SECONDARY);
        header.add(title);
        header.add(Box.createVerticalStrut(6));
        header.add(sub);

        // ── Name input ─────────────────────────────────────────────────────────
        JPanel nameSection = new JPanel();
        nameSection.setOpaque(false);
        nameSection.setLayout(new BoxLayout(nameSection, BoxLayout.Y_AXIS));
        nameSection.setBorder(new EmptyBorder(20, 100, 10, 100));

        JLabel nameLabel = new JLabel("PLAYER NAME");
        nameLabel.setFont(UIUtils.labelFont());
        nameLabel.setForeground(UIUtils.ACCENT_SECONDARY);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        nameField = new JTextField();
        nameField.setFont(UIUtils.font(Font.PLAIN, 18f));
        nameField.setForeground(UIUtils.TEXT_PRIMARY);
        nameField.setBackground(UIUtils.BG_PANEL);
        nameField.setCaretColor(UIUtils.ACCENT_PRIMARY);
        nameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIUtils.BG_CARD, 2),
            BorderFactory.createEmptyBorder(10, 16, 10, 16)
        ));
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        nameField.setAlignmentX(Component.CENTER_ALIGNMENT);

        nameSection.add(nameLabel);
        nameSection.add(Box.createVerticalStrut(8));
        nameSection.add(nameField);

        // ── Difficulty cards ────────────────────────────────────────────────────
        JPanel diffSection = new JPanel();
        diffSection.setOpaque(false);
        diffSection.setLayout(new BoxLayout(diffSection, BoxLayout.Y_AXIS));
        diffSection.setBorder(new EmptyBorder(20, 60, 10, 60));

        JLabel diffLabel = centeredLabel("SELECT DIFFICULTY",
            UIUtils.labelFont(), UIUtils.ACCENT_SECONDARY);
        diffLabel.setBorder(new EmptyBorder(0, 0, 14, 0));

        JPanel cards = new JPanel(new GridLayout(1, 3, 14, 0));
        cards.setOpaque(false);

        easyCard   = diffCard("EASY",   "4 × 4",  "8 pairs",  "Best for beginners", "Easy");
        mediumCard = diffCard("MEDIUM", "6 × 6",  "18 pairs", "A balanced challenge", "Medium");
        hardCard   = diffCard("HARD",   "8 × 8",  "32 pairs", "For memory masters", "Hard");

        cards.add(easyCard);
        cards.add(mediumCard);
        cards.add(hardCard);

        diffSection.add(diffLabel);
        diffSection.add(cards);

        // ── Buttons ────────────────────────────────────────────────────────────
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 20));
        btnRow.setOpaque(false);

        RoundedButton back = new RoundedButton("← BACK", UIUtils.BG_PANEL, UIUtils.BG_CARD);
        back.addActionListener(e -> mainMenu.showMainMenu());

        RoundedButton start = new RoundedButton("START GAME →",
            UIUtils.ACCENT_PRIMARY, UIUtils.ACCENT_PRIMARY.brighter());
        start.setPreferredSize(new Dimension(180, 46));
        start.addActionListener(e -> handleStart());

        nameField.addActionListener(e -> handleStart());

        btnRow.add(back);
        btnRow.add(start);

        // ── Assemble ───────────────────────────────────────────────────────────
        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.add(nameSection);
        center.add(diffSection);
        center.add(btnRow);

        bg.add(header, BorderLayout.NORTH);
        bg.add(center, BorderLayout.CENTER);
        add(bg, BorderLayout.CENTER);

        highlightDifficulty("Easy");
    }

    // ── Difficulty card builder ────────────────────────────────────────────────
    private RoundedPanel diffCard(String title, String grid, String pairs,
                                   String desc, String diffKey) {
        RoundedPanel card = new RoundedPanel(UIUtils.BG_PANEL, 16);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(18, 14, 18, 14));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel lTitle = new JLabel(title, SwingConstants.CENTER);
        lTitle.setFont(UIUtils.font(Font.BOLD, 18f));
        lTitle.setForeground(UIUtils.TEXT_PRIMARY);
        lTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lGrid = new JLabel(grid, SwingConstants.CENTER);
        lGrid.setFont(UIUtils.font(Font.BOLD, 28f));
        lGrid.setForeground(UIUtils.ACCENT_PRIMARY);
        lGrid.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lPairs = new JLabel(pairs, SwingConstants.CENTER);
        lPairs.setFont(UIUtils.bodyFont());
        lPairs.setForeground(UIUtils.ACCENT_SECONDARY);
        lPairs.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lDesc = new JLabel("<html><div style='text-align:center'>" + desc + "</div></html>",
                                   SwingConstants.CENTER);
        lDesc.setFont(UIUtils.smallFont());
        lDesc.setForeground(UIUtils.TEXT_SECONDARY);
        lDesc.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(Box.createVerticalGlue());
        card.add(lTitle);
        card.add(Box.createVerticalStrut(8));
        card.add(lGrid);
        card.add(Box.createVerticalStrut(4));
        card.add(lPairs);
        card.add(Box.createVerticalStrut(8));
        card.add(lDesc);
        card.add(Box.createVerticalGlue());

        card.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                selectedDifficulty = diffKey;
                highlightDifficulty(diffKey);
            }
            @Override public void mouseEntered(MouseEvent e) {
                if (!diffKey.equals(selectedDifficulty)) {
                    card.setFillColor(UIUtils.BG_CARD);
                }
            }
            @Override public void mouseExited(MouseEvent e) {
                if (!diffKey.equals(selectedDifficulty)) {
                    card.setFillColor(UIUtils.BG_PANEL);
                }
            }
        });
        return card;
    }

    private void highlightDifficulty(String key) {
        Color active   = UIUtils.ACCENT_PRIMARY.darker();
        Color inactive = UIUtils.BG_PANEL;
        easyCard  .setFillColor("Easy"  .equals(key) ? active : inactive);
        mediumCard.setFillColor("Medium".equals(key) ? active : inactive);
        hardCard  .setFillColor("Hard"  .equals(key) ? active : inactive);
    }

    // ── Actions ───────────────────────────────────────────────────────────────
    private void handleStart() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter your name to continue.",
                "Name Required", JOptionPane.WARNING_MESSAGE);
            nameField.requestFocus();
            return;
        }
        mainMenu.launchGame(name, selectedDifficulty);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────
    private JLabel centeredLabel(String text, Font f, Color c) {
        JLabel lbl = new JLabel(text, SwingConstants.CENTER);
        lbl.setFont(f);
        lbl.setForeground(c);
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        return lbl;
    }
}
