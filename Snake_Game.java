import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;

public class Snake_Game extends JPanel implements KeyListener, Runnable {
    private static final long serialVersionUID = 1L;

    public static final int height = 600;
    public static final int width = 600;
    public static final int gridSize = 50;

    public static int upKey = 38;
    public static int downKey = 40;
    public static int leftKey = 37;
    public static int rightKey = 39;
    public static boolean up = false;
    public static boolean down = false;
    public static boolean left = false;
    public static boolean right = false;

    public static Point cursor = new Point(0,0);
    public static Point food = new Point(
        (int)(Math.random() * (width / gridSize)) * gridSize,
        (int)(Math.random() * (height / gridSize)) * gridSize
    );

    public static boolean foodExists = true;

    public static int score = 0;

    public JFrame frame;

    public static void main(String[] args) {
        new Snake_Game();
    }

    public Snake_Game() {
        frame = new JFrame("Snake");
        addKeyListener(this);
        frame.addKeyListener(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);
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
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.clearRect(0,0, width, height);
        drawGrid(g);
        drawFood(g);
        g2d.setColor(Color.GREEN);
        g2d.fillRoundRect((int)cursor.getX(), (int)cursor.getY(), gridSize, gridSize, gridSize / 2, gridSize / 2);
        
    }

    public Point randomCellCoord() {
        Point point = new Point(0,0);
        int randIntX = ((int)(Math.random() * (width / gridSize)) * gridSize);
        int randIntY = ((int)(Math.random() * (height / gridSize)) * gridSize);
        boolean stop = false;
        for (int x = 0; x < 10 && stop == false; x++) {
            if (randIntX == (int)cursor.getX()) {
                randIntX = ((int)(Math.random() * (width / gridSize)) * gridSize);
            } else if (randIntY == (int)cursor.getY()) {
                randIntY = ((int)(Math.random() * (height / gridSize)) * gridSize);
            } else {
                stop = true;
            }
        }
        point.move(randIntX,randIntY);
        return point;
    }

    public void drawFood(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.RED);
        if(foodExists == false) {
            food.setLocation(randomCellCoord().getX(), randomCellCoord().getY());
            foodExists = true;
            repaint((int)food.getX(), (int)food.getY(), gridSize, gridSize);
        } else {
            if(food.getX() == cursor.getX() && food.getY() == cursor.getY()) {
                score++;
                foodExists = false;
                repaint((int)food.getX(), (int)food.getY(), gridSize, gridSize);
            }
        }
        
        g2d.fillOval((int)food.getX(), (int)food.getY(), gridSize, gridSize);
        g2d.setColor(Color.BLACK);
        g2d.drawString(Integer.toString(score), (int)food.getX() + gridSize / 2, (int)food.getY() + gridSize / 2);
    }



    public void drawGrid(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLACK);
        for (int x = 0; x <= width; x += gridSize) {
            g2d.drawLine(x, 0, x, height);
        }
        for (int y = 0; y <= height; y += gridSize) {
            g2d.drawLine(0, y, width, y);
        }
    }

    public void drawHead(Graphics g, int factor, boolean directionUp) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.GREEN);
        if(directionUp == true) {
            cursor.translate(0, gridSize * factor);
            g2d.fillRoundRect((int)cursor.getX(), (int)cursor.getY(), gridSize, gridSize, gridSize / 2, gridSize / 2);
        } else {
            cursor.translate(gridSize * factor, 0);
            g2d.fillRoundRect((int)cursor.getX(), (int)cursor.getY(), gridSize, gridSize, gridSize / 2, gridSize / 2);
        }
        
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyPressed(KeyEvent e) {
        repaint((int)cursor.getX(), (int)cursor.getY(), gridSize, gridSize);
        if (e.getKeyCode() == upKey) {
            up = true;
            cursor.translate(0, -gridSize);
        }
        if (e.getKeyCode() == downKey) {
            down = true;
            cursor.translate(0, gridSize);
        }
        if (e.getKeyCode() == leftKey) {
            left = true;
            cursor.translate(-gridSize, 0);
        }
        if (e.getKeyCode() == rightKey) {
            right = true;
            cursor.translate(gridSize, 0);
        }
        repaint((int)cursor.getX(), (int)cursor.getY(), gridSize, gridSize);
    }
    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void run() {}
}