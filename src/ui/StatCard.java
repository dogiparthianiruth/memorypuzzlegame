package ui;

import util.UIUtils;
import java.awt.*;
import javax.swing.*;

/**
 * StatCard.java
 * A rounded card widget that shows a label + large value, used in the game HUD.
 *
 * OOP: Inheritance (extends RoundedPanel), Composition (contains JLabels).
 */
public class StatCard extends RoundedPanel {

    private final JLabel valueLabel;
    private final JLabel titleLabel;

    public StatCard(String title, String initialValue, Color accentColor) {
        super(UIUtils.BG_PANEL, 16);
        setLayout(new BorderLayout(4, 4));
        setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));

        titleLabel = new JLabel(title.toUpperCase());
        titleLabel.setFont(UIUtils.smallFont());
        titleLabel.setForeground(accentColor);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        valueLabel = new JLabel(initialValue);
        valueLabel.setFont(UIUtils.font(Font.BOLD, 26f));
        valueLabel.setForeground(UIUtils.TEXT_PRIMARY);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);

        add(titleLabel, BorderLayout.NORTH);
        add(valueLabel, BorderLayout.CENTER);
    }

    /** Update the displayed value. */
    public void setValue(String value) {
        valueLabel.setText(value);
        repaint();
    }

    public String getValue() {
        return valueLabel.getText();
    }
}
