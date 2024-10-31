package co.edu.uptc.utilities;

import java.awt.Dimension;
import javax.swing.JButton;
import co.edu.uptc.views.GlobalView;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class DesignButton extends JButton {

    private int radius; 
    private boolean hasBackground; 

    public DesignButton(String text, boolean hasBackground) {
        super(text);
        this.radius = 30;
        this.hasBackground = hasBackground; 
        setOpaque(false); 
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setFont(GlobalView.ALL_TEXT_FONT);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (hasBackground) {
            g2d.setColor(getBackground());
            g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
        }

        super.paintComponent(g2d);

        g2d.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension size = super.getPreferredSize();
        size.width = Math.max(size.width, size.height); 
        return size;
    }
}