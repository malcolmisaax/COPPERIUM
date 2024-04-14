package ui.jobjects;

import javax.swing.*;
import java.awt.*;

// A JPanel with a BorderLayout
public class JPanelBackgroundBorder extends JPanel {

    // EFFECTS: Constructs a JPanel with BorderLayout with given background color (hex code string)

    public JPanelBackgroundBorder(String colorString) {
        super(new BorderLayout());
        try {
            Color bgColor = Color.decode(colorString);
            this.setBackground(bgColor);
        } catch (NumberFormatException e) {
            this.setBackground(Color.GRAY); // Default color
        }
    }
}
