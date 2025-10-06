import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import javax.swing.*;

public class PacMan extends JPanel implements ActionListener, KeyListener {

    class Block {
        int x;
        int y;
        int width;
        int height;
        Image image;

        int startX;
        int startY;
        char direction = 'U'; // U D L R
        int velocityX = 0;
        int velocityY = 0;


        Block(Image image, int x, int y, int width, int height) {
            this.image = image;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.startX = x;
            this.startY = y;
        }

        void updateDirection(char direction) {
            char prevDirection = this.direction;
            this.direction = direction;
            updateVelocity();
            this.x += this.velocityX;
            this.y += this.velocityY;

            for(Block wall : walls){
                if (collision(this, wall)){
                    this.x -= this.velocityX;
                    this.y -= this.velocityY;
                    this.direction = prevDirection;
                    updateVelocity();
                }
            }
        }

        void updateVelocity() {
            if (this.direction == 'U'){
                this.velocityX = 0;
                this.velocityY = -tileSize/4;
            }
            else if(this.direction == 'D'){
                this.velocityX = 0;
                this.velocityY = tileSize/4;
            }
            else if(this.direction == 'L'){
                this.velocityX = -tileSize/4;
                this.velocityY = 0 ;
            }
            else if(this.direction == 'R'){
                this.velocityX = tileSize/4;
                this.velocityY = 0 ;
            }
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

    // Game objects
    HashSet<Block> walls;
    HashSet<Block> foods;
    HashSet<Block> ghosts;
    Block pacman;

    // Repaint timer
    Timer gameLoop;

    public PacMan() { //constructor
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.BLACK);
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
        gameLoop = new Timer(50, this); // 20fps (1000/50)
        gameLoop.start();
    }

    /** Helper to load an image from classpath; throws clear error if missing. */
    private Image load(String absoluteResourcePath) {
        var url = PacMan.class.getResource(absoluteResourcePath);
        if (url == null) {
            throw new IllegalStateException("Missing resource on classpath: " + absoluteResourcePath);
        }
        return new ImageIcon(url).getImage();
    }

    public void loadMap() {
        walls = new HashSet<Block>();
        foods = new HashSet<Block>();
        ghosts = new HashSet<Block>();

        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < columnCount; c++) {
                String row = tileMap[r];
                char tileMapChar = row.charAt(c);

                int x = c*tileSize;
                int y = r*tileSize;

                if (tileMapChar == 'X') { //block wall
                    Block wall = new Block(wallImage, x, y, tileSize, tileSize);
                    walls.add(wall);
                }
                else if (tileMapChar == 'b') { //blue ghost
                    Block ghost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                }
                else if (tileMapChar == 'o') { //orange ghost
                    Block ghost = new Block(orangeGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                }
                else if (tileMapChar == 'p') { //pink ghost
                    Block ghost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                }
                else if (tileMapChar == 'r') { //red ghost
                    Block ghost = new Block(redGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                }
                else if (tileMapChar == 'P') { //pacman
                    pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);
                }
                else if (tileMapChar == ' ') { //food
                    Block food = new Block(null, x + 14, y + 14, 4, 4);
                    foods.add(food);
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
        g.drawImage(pacman.image, pacman.x, pacman.y, pacman.width, pacman.height, null);

        for(Block ghost : ghosts){
            g.drawImage(ghost.image, ghost.x, ghost.y, ghost.width, ghost.height, null);
        }

        for(Block wall : walls){
            g.drawImage(wall.image, wall.x, wall.y, wall.width, wall.height, null);
        }

        g.setColor(Color.WHITE);
        for(Block food : foods){
            g.fillRect(food.x, food.y, food.width, food.height);
        }
    }

    public void move(){
        pacman.x += pacman.velocityX;
        pacman.y += pacman.velocityY;

        for(Block wall : walls){
            if(collision(pacman, wall)){
                pacman.x -= pacman.velocityX;
                pacman.y -= pacman.velocityY;
                break;
            }
        }
    }

    public boolean collision(Block a, Block b) {
        return  a.x < b.x + b.width &&
                a.x + a.width > b.x &&
                a.y < b.y + b.height &&
                a.y + a.height > b.y;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
    }

    // KeyListener stubs (future movement)
    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyPressed(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_UP){
            pacman.updateDirection('U');
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            pacman.updateDirection('D');
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            pacman.updateDirection('L');
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            pacman.updateDirection('R');
        }

        if(pacman.direction == 'U'){
            pacman.image = pacmanUpImage;
        }
        else if(pacman.direction == 'D'){
            pacman.image = pacmanDownImage;
        }
        else if(pacman.direction == 'L'){
            pacman.image = pacmanLeftImage;
        }
        else if(pacman.direction == 'R'){
            pacman.image = pacmanRightImage;
        }
    }
}
