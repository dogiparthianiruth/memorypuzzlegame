package ui;

import model.Score;
import util.ScoreManager;
import util.UIUtils;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 * HighScorePanel.java
 * Displays the top-10 leaderboard with a styled JTable.
 *
 * OOP: Composition (contains JTable, ScoreManager), Encapsulation.
 */
public class HighScorePanel extends JPanel {

    private final MainMenu      mainMenu;
    private final ScoreManager  scoreManager;
    private       JTable        table;
    private       DefaultTableModel tableModel;

    private static final String[] COLUMNS = {"Rank", "Player", "Difficulty", "Score", "Moves", "Time"};

    public HighScorePanel(MainMenu mainMenu, ScoreManager scoreManager) {
        super(new BorderLayout());
        this.mainMenu     = mainMenu;
        this.scoreManager = scoreManager;
        setOpaque(false);
        buildUI();
    }

    public void refresh() {
        populateTable();
    }

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

        JLabel icon  = centeredLabel("🏆", new Font("Segoe UI Emoji", Font.PLAIN, 40), UIUtils.WARNING);
        JLabel title = centeredLabel("HIGH SCORES", UIUtils.font(Font.BOLD, 28f), UIUtils.TEXT_PRIMARY);
        JLabel sub   = centeredLabel("Top 10 all-time best scores",
            UIUtils.font(Font.ITALIC, 13f), UIUtils.TEXT_SECONDARY);

        header.add(icon);
        header.add(Box.createVerticalStrut(6));
        header.add(title);
        header.add(Box.createVerticalStrut(4));
        header.add(sub);

        // Table
        tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        styleTable();

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setOpaque(false);
        scroll.getViewport().setBackground(UIUtils.BG_PANEL);
        scroll.setBorder(BorderFactory.createLineBorder(UIUtils.BG_CARD, 1));

        JPanel tableWrapper = new JPanel(new BorderLayout());
        tableWrapper.setOpaque(false);
        tableWrapper.setBorder(new EmptyBorder(0, 40, 20, 40));
        tableWrapper.add(scroll, BorderLayout.CENTER);

        // Buttons
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 16));
        btnRow.setOpaque(false);

        RoundedButton backBtn = new RoundedButton("← BACK", UIUtils.BG_PANEL, UIUtils.BG_CARD);
        backBtn.addActionListener(e -> mainMenu.showMainMenu());

        btnRow.add(backBtn);

        bg.add(header,       BorderLayout.NORTH);
        bg.add(tableWrapper, BorderLayout.CENTER);
        bg.add(btnRow,       BorderLayout.SOUTH);
        add(bg, BorderLayout.CENTER);

        populateTable();
    }

    private void styleTable() {
        table.setBackground(UIUtils.BG_PANEL);
        table.setForeground(UIUtils.TEXT_PRIMARY);
        table.setFont(UIUtils.bodyFont());
        table.setRowHeight(38);
        table.setGridColor(UIUtils.BG_CARD);
        table.setSelectionBackground(UIUtils.ACCENT_PRIMARY.darker());
        table.setSelectionForeground(UIUtils.TEXT_PRIMARY);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setFocusable(false);
        table.setRowSelectionAllowed(false);

        JTableHeader hdr = table.getTableHeader();
        hdr.setBackground(UIUtils.BG_SECONDARY);
        hdr.setForeground(UIUtils.ACCENT_SECONDARY);
        hdr.setFont(UIUtils.labelFont());
        hdr.setReorderingAllowed(false);
        hdr.setResizingAllowed(false);

        // Centre-align all columns
        DefaultTableCellRenderer centre = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean sel,
                                                           boolean foc, int r, int c) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(t, v, sel, foc, r, c);
                lbl.setHorizontalAlignment(SwingConstants.CENTER);
                lbl.setBackground(r % 2 == 0 ? UIUtils.BG_PANEL : UIUtils.BG_SECONDARY);
                lbl.setForeground(c == 0 ? UIUtils.ACCENT_SECONDARY :
                                  c == 3 ? UIUtils.SUCCESS           : UIUtils.TEXT_PRIMARY);
                return lbl;
            }
        };
        for (int i = 0; i < COLUMNS.length; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centre);
        }

        // Column widths
        int[] widths = {50, 160, 100, 90, 70, 80};
        for (int i = 0; i < widths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }
    }

    private void populateTable() {
        tableModel.setRowCount(0);
        List<Score> scores = scoreManager.loadScores();
        if (scores.isEmpty()) {
            tableModel.addRow(new Object[]{"—", "No scores yet", "—", "—", "—", "—"});
            return;
        }
        int rank = 1;
        for (Score s : scores) {
            tableModel.addRow(new Object[]{
                "#" + rank++,
                s.getPlayerName(),
                s.getDifficulty(),
                s.getScore(),
                s.getMoves(),
                s.getTime()
            });
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
