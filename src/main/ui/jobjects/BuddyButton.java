package ui.jobjects;

import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// creates a rounded button with image and text
public class BuddyButton extends JButton {

    private Color originalBackgroundColor;
    private Color hoverBackgroundColor;
    private ImageIcon icon;

    // creates a rounded button with given image in the center and given text at bottom
    public BuddyButton(String label, String color, ImageIcon icon) {
        super(label);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setBorder(null);
        this.icon = icon; // Set the icon

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

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        graphics.setColor(getBackground());
        graphics.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);
        graphics.setColor(Color.BLACK);
        graphics.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);

        if (icon != null) {
            int x = (getWidth() - icon.getIconWidth()) / 2;
            int y = (getHeight() - icon.getIconHeight()) / 2 - 10; // Adjust Y position for text
            graphics.drawImage(icon.getImage(), x, y, this);
        }

        graphics.setFont(new Font("SansSerif", Font.ITALIC, 11));
        FontMetrics fm = graphics.getFontMetrics();
        int stringWidth = fm.stringWidth(getText());
        int stringAscent = fm.getAscent();
        int x = (getWidth() - stringWidth) / 2;
        int y = getHeight() - stringAscent / 2 - 5;
        graphics.drawString(getText(), x, y);

    }

    // EFFECTS: Makes a color a few shades lighter
    private Color lightenColor(Color color) {
        int r = (int) Math.min(255, Math.max(0, color.getRed() + (color.getRed() * 0.15)));
        int g = (int) Math.min(255, Math.max(0, color.getGreen() + (color.getGreen() * 0.15)));
        int b = (int) Math.min(255, Math.max(0, color.getBlue() + (color.getBlue() * 0.15)));
        return new Color(r, g, b, color.getAlpha());
    }

}
