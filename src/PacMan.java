import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import javax.swing.*;

/** Basic Pac-Man board that draws Pac-Man and ghosts. */
public class PacMan extends JPanel implements ActionListener, KeyListener {

    /** Small renderable block (sprite + rect). */
    class Block {
        int x, y, width, height;
        Image image;

        Block(Image image, int x, int y, int width, int height) {
            this.image = image;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
    }

    // Board constants
    private final int rowCount = 21;
    private final int columnCount = 19;
    private final int tileSize = 32;
    private final int boardWidth  = columnCount * tileSize;
    private final int boardHeight = rowCount * tileSize;

    // Sprites
    private Image wallImage;
    private Image blueGhostImage;
    private Image orangeGhostImage;
    private Image pinkGhostImage;
    private Image redGhostImage;

    private Image pacmanUpImage;
    private Image pacmanDownImage;
    private Image pacmanLeftImage;
    private Image pacmanRightImage;

    // Map legend:
    // X = wall, O = skip, P = pacman, ' ' = food
    // b/o/p/r = ghosts
    private final String[] tileMap = {
            "XXXXXXXXXXXXXXXXXXX",
            "X        X        X",
            "X XX XXX X XXX XX X",
            "X                 X",
            "X XX X XXXXX X XX X",
            "X    X       X    X",
            "XXXX XXXX XXXX XXXX",
            "OOOX X       X XOOO",
            "XXXX X XXrXX X XXXX",
            "O       bpo       O",
            "XXXX X XXXXX X XXXX",
            "OOOX X       X XOOO",
            "XXXX X XXXXX X XXXX",
            "X        X        X",
            "X XX XXX X XXX XX X",
            "X  X     P     X  X",
            "XX X X XXXXX X X XX",
            "X    X   X   X    X",
            "X XXXXXX X XXXXXX X",
            "X                 X",
            "XXXXXXXXXXXXXXXXXXX"
    };

    // Game objects (this stage: pacman + ghosts only)
    private HashSet<Block> ghosts;
    private Block pacman;

    // Repaint timer
    private Timer timer;

    public PacMan() {
        // Panel setup
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.BLACK);
        setDoubleBuffered(true);

        // Keyboard focus
        addKeyListener(this);
        setFocusable(true);

        // Load sprites from classpath root. IMPORTANT: images must be on classpath.
        wallImage        = load("/wall.png");
        blueGhostImage   = load("/blueGhost.png");
        orangeGhostImage = load("/orangeGhost.png");
        pinkGhostImage   = load("/pinkGhost.png");
        redGhostImage    = load("/redGhost.png");

        pacmanUpImage    = load("/pacmanUp.png");
        pacmanDownImage  = load("/pacmanDown.png");
        pacmanLeftImage  = load("/pacmanLeft.png");
        pacmanRightImage = load("/pacmanRight.png");

        // Build objects from map
        loadMap();

        // 10 FPS is fine for now; adjust later for movement/animation
        timer = new Timer(100, this);
        timer.start();
    }

    /** Helper to load an image from classpath; throws clear error if missing. */
    private Image load(String absoluteResourcePath) {
        var url = PacMan.class.getResource(absoluteResourcePath);
        if (url == null) {
            throw new IllegalStateException("Missing resource on classpath: " + absoluteResourcePath);
        }
        return new ImageIcon(url).getImage();
    }

    /** Parse the tile map and create actors. */
    private void loadMap() {
        ghosts = new HashSet<>();
        for (int r = 0; r < rowCount; r++) {
            String row = tileMap[r];
            for (int c = 0; c < columnCount; c++) {
                char ch = row.charAt(c);
                int x = c * tileSize;
                int y = r * tileSize;

                switch (ch) {
                    case 'b' -> ghosts.add(new Block(blueGhostImage,   x, y, tileSize, tileSize));
                    case 'o' -> ghosts.add(new Block(orangeGhostImage, x, y, tileSize, tileSize));
                    case 'p' -> ghosts.add(new Block(pinkGhostImage,   x, y, tileSize, tileSize));
                    case 'r' -> ghosts.add(new Block(redGhostImage,    x, y, tileSize, tileSize));
                    case 'P' -> pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);
                    default  -> {} // ignore for this stage
                }
            }
        }
    }

    /** Request focus when the component is added to a container. */
    @Override
    public void addNotify() {
        super.addNotify();
        requestFocusInWindow();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    /** Draw only pacman and ghosts (no walls/food yet). */
    private void draw(Graphics g) {
        if (pacman != null) {
            g.drawImage(pacman.image, pacman.x, pacman.y, pacman.width, pacman.height, null);
        }
        if (ghosts != null) {
            for (Block gh : ghosts) {
                g.drawImage(gh.image, gh.x, gh.y, gh.width, gh.height, null);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    // KeyListener stubs (future movement)
    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyPressed(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}
}
