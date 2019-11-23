import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;

public class Snake_Game extends JPanel implements KeyListener, Runnable {
    private static final long serialVersionUID = 1L;

    public static final int height = 500;
    public static final int width = 500;
    public static final int gridSize = 50;
    public static final int tickSpeed = 100;

    public static int upKey = 38;
    public static int downKey = 40;
    public static int leftKey = 37;
    public static int rightKey = 39;
    public static boolean up = false;
    public static boolean down = false;
    public static boolean left = false;
    public static boolean right = false ;

    public static ArrayList<Point> body = new ArrayList<Point>();
    public static Point cursor = new Point(0, 0);
    public static Point lastPos = cursor;
    public static Point food = new Point((int) (Math.random() * (width / gridSize)) * gridSize, (int) (Math.random() * (height / gridSize)) * gridSize);

    public static boolean foodExists = false;

    public static int score = 0;
    public static boolean lose = false;

    public JFrame frame;

    public static void main(final String[] args) {
        new Snake_Game();
    }

    public Snake_Game() {
        frame = new JFrame("Snake");
        addKeyListener(this);
        frame.addKeyListener(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setContentPane(this);
        setPreferredSize(new Dimension(width, height));
        setSize(new Dimension(width, height));
        frame.pack();
        setFocusTraversalKeysEnabled(false);
        frame.setLocationRelativeTo(null);
        new Thread(this).start();
    }

    @Override
    public void paintComponent(final Graphics g) {
        doGame(g);
    }

    public void doGame(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.clearRect(0, 0, width, height);
        drawGrid(g);
        drawFood(g);
        g2d.setColor(Color.BLUE);
        g2d.fillRoundRect((int) cursor.getX(), (int) cursor.getY(), gridSize, gridSize, gridSize / 2, gridSize / 2);
        didLose(g, score);
    }

    public void didLose(final Graphics g, final int checkScore) {
        final Graphics2D g2d = (Graphics2D) g;
        if (cursor.getX() < 0 || cursor.getX() >= width || cursor.getY() < 0 || cursor.getY() >= height) {
            g2d.setColor(Color.BLACK);
            g2d.fillRect(0, 0, width, height);
            g2d.setColor(Color.RED);
            g2d.drawString("You dead. Score: " + score, width / 2, height / 2);
        }
    }

    public Point randomCellCoord() {
        final Point point = new Point(0, 0);
        int randIntX = ((int) (Math.random() * (width / gridSize)) * gridSize);
        int randIntY = ((int) (Math.random() * (height / gridSize)) * gridSize);
        boolean stop = false;
        for (int x = 0; x < 10 && stop == false; x++) {
            if (randIntX == (int) cursor.getX()) {
                randIntX = ((int) (Math.random() * (width / gridSize)) * gridSize);
            } else if (randIntY == (int) cursor.getY()) {
                randIntY = ((int) (Math.random() * (height / gridSize)) * gridSize);
            } else {
                stop = true;
            }
        }
        point.move(randIntX, randIntY);
        return point;
    }

    public void drawFood(final Graphics g) {
        final Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.RED);
        if (foodExists == false) {
            food.setLocation(randomCellCoord().getX(), randomCellCoord().getY());
            foodExists = true;
        } else {
            if (food.getX() == cursor.getX() && food.getY() == cursor.getY()) {
                score++;
                foodExists = false;
            }
        }

        drawCell(g, food, gridSize, Color.RED);
        g2d.setColor(Color.BLACK);
        g2d.drawString(Integer.toString(score), (int) food.getX() + gridSize / 2, (int) food.getY() + gridSize / 2);
    }

    public void drawGrid(final Graphics g) {
        final Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLACK);
        for (int x = 0; x <= width; x += gridSize) {
            g2d.drawLine(x, 0, x, height);
        }
        for (int y = 0; y <= height; y += gridSize) {
            g2d.drawLine(0, y, width, y);
        }
    }

    public void drawCell(final Graphics g, final Point point, final int factor, final Color color) {
        final Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(color);
        g2d.fillRoundRect((int) point.getX(), (int) point.getY(), gridSize, gridSize, factor, factor);

    }

    public void movePlayer() {
        if (up == true) {
            cursor.translate(0, -gridSize);
        } else if (down == true) {
            cursor.translate(0, gridSize);
        } else if (left == true) {
            cursor.translate(-gridSize, 0);
        } else if (right == true) {
            cursor.translate(gridSize, 0);
        } else {
            cursor.setLocation(0, 0);
        }
    }

    public void setAllFalse() {
        up = false;
        down = false;
        left = false;
        right = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int et = e.getKeyCode();
        if (et == upKey && !down) {
            setAllFalse();
            up = true;
        }
        if (et == downKey && !up) {
            setAllFalse();
            down = true;
        }
        if (et == leftKey && !right) {
            setAllFalse();
            left = true;
        }
        if (et == rightKey && !left) {
            setAllFalse();
            right = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void run() {
        while (true) {
            repaint();
            movePlayer();
            try {
                Thread.sleep(tickSpeed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}