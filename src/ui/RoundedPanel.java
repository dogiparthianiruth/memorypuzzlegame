package ui;

import util.UIUtils;
import java.awt.*;
import javax.swing.JPanel;

/**
 * RoundedPanel.java
 * A JPanel that paints a rounded-rectangle background with optional gradient.
 *
 * OOP: Inheritance (extends JPanel), method overriding (paintComponent).
 */
public class RoundedPanel extends JPanel {

    private Color  topColor;
    private Color  bottomColor;
    private int    arc;
    private boolean useGradient;

    public RoundedPanel(Color fill, int arc) {
        this(fill, fill, arc, false);
    }

    public RoundedPanel(Color top, Color bottom, int arc, boolean gradient) {
        super();
        this.topColor    = top;
        this.bottomColor = bottom;
        this.arc         = arc;
        this.useGradient = gradient;
        setOpaque(false);
    }

    public void setFillColor(Color c) {
        topColor    = c;
        bottomColor = c;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        UIUtils.applyRenderingHints(g2);

        int w = getWidth();
        int h = getHeight();

        if (useGradient) {
            GradientPaint gp = new GradientPaint(0, 0, topColor, 0, h, bottomColor);
            g2.setPaint(gp);
        } else {
            g2.setColor(topColor);
        }
        g2.fillRoundRect(0, 0, w, h, arc, arc);

        g2.dispose();
        super.paintComponent(g);
    }
}
