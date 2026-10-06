package ui;

import model.Card;
import util.UIUtils;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

/**
 * CardButton.java
 * Custom-painted game card that visually reflects Card model state.
 *
 * States:
 *  HIDDEN   – dark back, "?" glyph, hover highlight
 *  REVEALED – accent colour, shows symbol
 *  MATCHED  – green tint, symbol, disabled
 *
 * OOP: Inheritance (extends JButton), Composition (holds Card reference),
 *      method overriding (paintComponent).
 */
public class CardButton extends JButton {

    // ── Flip animation ────────────────────────────────────────────────────────
    private static final int FLIP_STEPS    = 8;
    private static final int FLIP_DELAY_MS = 18;

    private final Card  card;
    private boolean     hovered   = false;

    // Flip-animation state (0.0 = fully hidden, 1.0 = fully revealed)
    private float  flipProgress  = 0f;
    private javax.swing.Timer flipTimer;
    private boolean flippingToRevealed = false;

    private int cardSize;

    // ── Constructor ───────────────────────────────────────────────────────────
    public CardButton(Card card, int cardSize) {
        super();
        this.card     = card;
        this.cardSize = cardSize;

        setPreferredSize(new Dimension(cardSize, cardSize));
        setMinimumSize  (new Dimension(cardSize, cardSize));
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);
        setBorder(new EmptyBorder(4, 4, 4, 4));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Initialise flip progress based on initial card state
        flipProgress = (card.isRevealed() || card.isMatched()) ? 1f : 0f;

        addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { hovered = true;  repaint(); }
            @Override public void mouseExited(MouseEvent e)  { hovered = false; repaint(); }
        });
    }

    // ── Card accessor ─────────────────────────────────────────────────────────
    public Card getCard() { return card; }

    // ── Sync visual state ─────────────────────────────────────────────────────

    /**
     * Trigger flip animation to reflect the card's current model state.
     * Called by GameBoard after the model is updated.
     */
    public void syncState(boolean animate) {
        boolean shouldBeRevealed = card.isRevealed() || card.isMatched();

        if (!animate) {
            flipProgress = shouldBeRevealed ? 1f : 0f;
            repaint();
            return;
        }

        flippingToRevealed = shouldBeRevealed;
        if (flipTimer != null && flipTimer.isRunning()) flipTimer.stop();

        flipTimer = new javax.swing.Timer(FLIP_DELAY_MS, null);
        flipTimer.addActionListener(e -> {
            if (flippingToRevealed) {
                flipProgress = Math.min(1f, flipProgress + 1f / FLIP_STEPS);
            } else {
                flipProgress = Math.max(0f, flipProgress - 1f / FLIP_STEPS);
            }
            repaint();
            if (flipProgress >= 1f || flipProgress <= 0f) {
                flipTimer.stop();
            }
        });
        flipTimer.start();
    }

    // ── Painting ──────────────────────────────────────────────────────────────

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        UIUtils.applyRenderingHints(g2);

        int w   = getWidth();
        int h   = getHeight();
        int arc = 14;

        // Determine visual face based on flip progress
        boolean showFront = flipProgress > 0.5f;

        // Horizontal squish for flip illusion: scale width by |cos(progress*π)|
        // Maps 0→1 as: full wide → squeezed at 0.5 → full wide
        double angle     = flipProgress * Math.PI;
        double scaleX    = Math.abs(Math.cos(angle));
        int    drawW     = Math.max(4, (int)(w * scaleX));
        int    drawX     = (w - drawW) / 2;

        // Choose colour
        Color  bgColor;
        if (card.isMatched()) {
            bgColor = UIUtils.CARD_MATCHED;
        } else if (showFront) {
            bgColor = UIUtils.CARD_FRONT;
        } else {
            bgColor = hovered ? UIUtils.BG_CARD.brighter() : UIUtils.CARD_BACK;
        }

        // Background
        g2.setColor(bgColor);
        g2.fillRoundRect(drawX, 2, drawW - 2, h - 4, arc, arc);

        // Border
        Color borderColor = card.isMatched() ? UIUtils.SUCCESS :
                            (showFront       ? UIUtils.ACCENT_PRIMARY : UIUtils.BG_PANEL);
        g2.setColor(borderColor);
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(drawX, 2, drawW - 3, h - 5, arc, arc);

        // Symbol / glyph (only draw when face is wide enough to be readable)
        if (drawW > w * 0.45) {
            String glyph;
            Color  glyphColor;
            Font   glyphFont;

            if (showFront) {
                glyph      = card.getSymbol();
                glyphColor = UIUtils.TEXT_PRIMARY;
                glyphFont  = derivedEmojiFont();
            } else {
                glyph      = "✦";
                glyphColor = UIUtils.TEXT_SECONDARY;
                glyphFont  = UIUtils.font(Font.BOLD, cardSize * 0.28f);
            }

            g2.setFont(glyphFont);
            g2.setColor(glyphColor);
            FontMetrics fm = g2.getFontMetrics();
            int tx = drawX + (drawW - fm.stringWidth(glyph)) / 2;
            int ty = (h - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(glyph, tx, ty);
        }

        // Hover ring on hidden cards
        if (hovered && !card.isMatched() && !showFront) {
            g2.setColor(UIUtils.ACCENT_PRIMARY.brighter());
            g2.setStroke(new BasicStroke(2f));
            g2.drawRoundRect(drawX, 2, drawW - 3, h - 5, arc, arc);
        }

        g2.dispose();
    }

    private Font derivedEmojiFont() {
        float size = cardSize * 0.35f;
        return new Font("Segoe UI Emoji", Font.PLAIN, (int) size);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(cardSize, cardSize);
    }
}
