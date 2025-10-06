import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Create the main frame
                JFrame frame = new JFrame("Pac Man");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setResizable(false);

                // Create a panel that will contain the other panels (the "cards")
                CardLayout cardLayout = new CardLayout();
                JPanel mainPanel = new JPanel(cardLayout);

                // Create instances of our two panels
                StartMenuPanel startMenu = new StartMenuPanel();
                PacMan pacmanGame = new PacMan();

                // Add the panels to the main container panel with names
                mainPanel.add(startMenu, "MENU");
                mainPanel.add(pacmanGame, "GAME");

                // Define what happens when the start button is clicked
                startMenu.addStartButtonListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Switch from the "MENU" card to the "GAME" card
                        cardLayout.show(mainPanel, "GAME");
                        // Start the game logic
                        pacmanGame.startGame();
                    }
                });

                // Add the main panel to the frame and display it
                frame.add(mainPanel);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
}