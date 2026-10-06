package ui;

import util.UIUtils;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

/**
 * RoundedButton.java
 * A fully custom-painted button with rounded corners, hover, and press effects.
 *
 * OOP: Inheritance (extends JButton), composition (mouse listener),
 *      method overriding (paintComponent).
 */
public class RoundedButton extends JButton {

    private Color normalColor;
    private Color hoverColor;
    private Color pressColor;
    private Color textColor;
    private boolean hovered  = false;
    private boolean pressed  = false;
    private int arc;

    public RoundedButton(String text, Color normalColor, Color hoverColor) {
        this(text, normalColor, hoverColor, UIUtils.TEXT_PRIMARY, 14);
    }

    public RoundedButton(String text, Color normalColor, Color hoverColor,
                         Color textColor, int arc) {
        super(text);
        this.normalColor = normalColor;
        this.hoverColor  = hoverColor;
        this.pressColor  = hoverColor.darker();
        this.textColor   = textColor;
        this.arc         = arc;

        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);
        setForeground(textColor);
        setFont(UIUtils.font(Font.BOLD, 14f));
        setBorder(new EmptyBorder(12, 28, 12, 28));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e)  { hovered = true;  repaint(); }
            @Override public void mouseExited(MouseEvent e)   { hovered = false; pressed = false; repaint(); }
            @Override public void mousePressed(MouseEvent e)  { pressed = true;  repaint(); }
            @Override public void mouseReleased(MouseEvent e) { pressed = false; repaint(); }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        UIUtils.applyRenderingHints(g2);

        Color bg = pressed ? pressColor : (hovered ? hoverColor : normalColor);
        g2.setColor(bg);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);

        // Subtle border highlight
        g2.setColor(bg.brighter());
        g2.setStroke(new BasicStroke(1f));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc, arc);

        g2.dispose();
        super.paintComponent(g);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        setForeground(enabled ? textColor : UIUtils.TEXT_SECONDARY);
        repaint();
    }
}
