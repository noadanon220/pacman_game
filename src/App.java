import javax.swing.JFrame;

public class App {
    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame("Pac Man");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        PacMan pacmanGame = new PacMan();
        frame.add(pacmanGame);

        // pack() must be called after adding components
        frame.pack();

        // This line is crucial for keyboard input and can help with drawing
        pacmanGame.requestFocus();

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}