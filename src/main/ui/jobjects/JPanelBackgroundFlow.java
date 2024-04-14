package ui.jobjects;

import javax.swing.*;
import java.awt.*;

// A JPanel with FlowLayout
public class JPanelBackgroundFlow extends JPanel {

    // EFFECTS: Constructs a JPanel with FlowLayout with given background color (hex code string)
    public JPanelBackgroundFlow(String colorString) {
        super(new FlowLayout());
        try {
            Color bgColor = Color.decode(colorString);
            this.setBackground(bgColor);
        } catch (NumberFormatException e) {
            this.setBackground(Color.GRAY); // Default color
        }
    }
}
