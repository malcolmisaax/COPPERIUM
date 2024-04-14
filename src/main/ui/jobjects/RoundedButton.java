package ui.jobjects;

import javax.swing.JButton;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// A rounded JButton

public class RoundedButton extends JButton {

    private Color originalBackgroundColor;
    private Color hoverBackgroundColor;


    // EFFECTS: Constructs a JButton with string for and color (hex code string)
    //          Makes it so the color of the button changes when the user is hovering over it
    public RoundedButton(String label, String color) {
        super(label);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setBorder(null);

        originalBackgroundColor = Color.decode(color);
        hoverBackgroundColor = lightenColor(originalBackgroundColor); // Lighten

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(hoverBackgroundColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(originalBackgroundColor);
            }
        });
    }

    // MODIFIES: This
    // EFFECTS: Creates the rounded button and its border, and colors it in
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setColor(getBackground());
        graphics.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);
        graphics.setColor(Color.BLACK);
        graphics.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);
        super.paintComponent(g);
    }

    // EFFECTS: Makes a color a few shades lighter
    private Color lightenColor(Color color) {
        int r = (int) Math.min(255, Math.max(0, color.getRed() + (color.getRed() * 0.15)));
        int g = (int) Math.min(255, Math.max(0, color.getGreen() + (color.getGreen() * 0.15)));
        int b = (int) Math.min(255, Math.max(0, color.getBlue() + (color.getBlue() * 0.15)));
        return new Color(r, g, b, color.getAlpha());
    }

}
