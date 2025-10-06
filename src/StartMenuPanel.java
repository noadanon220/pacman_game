import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

public class StartMenuPanel extends JPanel {

    private JButton startButton;

    public StartMenuPanel() {
        // Set the layout and background
        setLayout(new GridBagLayout());
        setBackground(Color.BLACK);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(20, 0, 20, 0); // Add some padding

        // --- START: Logo Resizing Logic ---

        // 1. Load the original, large image icon
        ImageIcon originalLogoIcon = new ImageIcon(getClass().getResource("./pacman_logo.png"));

        // 2. Get the Image object from the icon
        Image originalImage = originalLogoIcon.getImage();

        // 3. Define the new width you want for the logo (e.g., 400 pixels)
        int desiredWidth = 400;

        // 4. Create a scaled version of the image.
        // Using -1 for height will maintain the original aspect ratio.
        Image scaledImage = originalImage.getScaledInstance(desiredWidth, -1, Image.SCALE_SMOOTH);

        // 5. Create a new ImageIcon from the smaller, scaled image
        ImageIcon scaledLogoIcon = new ImageIcon(scaledImage);

        // 6. Create the label with the new, smaller icon
        JLabel logoLabel = new JLabel(scaledLogoIcon);

        // --- END: Logo Resizing Logic ---

        add(logoLabel, gbc);

        // Create and add the start button
        startButton = new JButton("START GAME");
        startButton.setFont(new Font("Arial", Font.BOLD, 24));
        startButton.setBackground(Color.YELLOW);
        startButton.setForeground(Color.BLACK);
        startButton.setFocusPainted(false);
        add(startButton, gbc);
    }

    // This method allows the main App to add the click listener to the button
    public void addStartButtonListener(ActionListener listener) {
        startButton.addActionListener(listener);
    }
}