package util;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;

/**
 * UIUtils.java
 * Central repository of shared colours, fonts, and factory helpers.
 * Ensures a consistent visual language across the entire application.
 */
public final class UIUtils {

    // ── Colour Palette ────────────────────────────────────────────────────────
    public static final Color BG_DEEP        = new Color(0x0F172A);
    public static final Color BG_SECONDARY   = new Color(0x111827);
    public static final Color BG_PANEL       = new Color(0x1E293B);
    public static final Color BG_CARD        = new Color(0x334155);
    public static final Color ACCENT_PRIMARY = new Color(0x8B5CF6);
    public static final Color ACCENT_SECONDARY = new Color(0x06B6D4);
    public static final Color SUCCESS        = new Color(0x22C55E);
    public static final Color WARNING        = new Color(0xF59E0B);
    public static final Color ERROR          = new Color(0xEF4444);
    public static final Color TEXT_PRIMARY   = new Color(0xF8FAFC);
    public static final Color TEXT_SECONDARY = new Color(0x94A3B8);
    public static final Color CARD_BACK      = new Color(0x2D3748);
    public static final Color CARD_FRONT     = new Color(0x4C1D95);
    public static final Color CARD_MATCHED   = new Color(0x14532D);

    // ── Fonts ─────────────────────────────────────────────────────────────────
    public static Font font(int style, float size) {
        return new Font("Segoe UI", style, (int) size);
    }

    public static Font titleFont()    { return font(Font.BOLD,  38f); }
    public static Font subtitleFont() { return font(Font.PLAIN, 16f); }
    public static Font headingFont()  { return font(Font.BOLD,  22f); }
    public static Font bodyFont()     { return font(Font.PLAIN, 14f); }
    public static Font smallFont()    { return font(Font.PLAIN, 12f); }
    public static Font labelFont()    { return font(Font.BOLD,  13f); }
    public static Font emojiFont()    { return new Font("Segoe UI Emoji", Font.PLAIN, 28); }
    public static Font emojiFontLarge() { return new Font("Segoe UI Emoji", Font.PLAIN, 36); }

    // ── Borders ───────────────────────────────────────────────────────────────
    public static Border emptyBorder(int v, int h) {
        return BorderFactory.createEmptyBorder(v, h, v, h);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    /**
     * Apply anti-aliasing and text anti-aliasing hints to a Graphics2D context.
     * Call at the start of every custom paintComponent.
     */
    public static void applyRenderingHints(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,      RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,         RenderingHints.VALUE_RENDER_QUALITY);
    }

    /**
     * Draw a vertical gradient rectangle covering the full component.
     */
    public static void paintGradientBackground(Graphics2D g2,
                                               int w, int h,
                                               Color top, Color bottom) {
        GradientPaint gp = new GradientPaint(0, 0, top, 0, h, bottom);
        g2.setPaint(gp);
        g2.fillRect(0, 0, w, h);
    }

    /**
     * Draw a rounded-rectangle fill with optional border.
     */
    public static void fillRoundRect(Graphics2D g2, Color fill, Color border,
                                     int x, int y, int w, int h, int arc) {
        g2.setColor(fill);
        g2.fillRoundRect(x, y, w, h, arc, arc);
        if (border != null) {
            g2.setColor(border);
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(x, y, w, h, arc, arc);
        }
    }

    /**
     * Set up the Look-and-Feel to Nimbus if available, otherwise fall back.
     * Also sets the default tooltip colours globally.
     */
    public static void setLookAndFeel() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {
            // Use default LAF – we rely on custom painting anyway
        }
        // Override tooltip colours
        UIManager.put("ToolTip.background", BG_PANEL);
        UIManager.put("ToolTip.foreground", TEXT_PRIMARY);
        UIManager.put("ToolTip.font",       bodyFont());
    }

    /** Private constructor – utility class, not instantiable. */
    private UIUtils() {}
}
