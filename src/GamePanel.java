import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600; // 600x600 pixels
    static final int SCREEN_HEIGHT = 600; // 600x600 pixels
    static final int UNIT_SIZE = 25; // 25x25 pixels
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE; // number of body parts of snake
    static final int delay = 75; // milliseconds
    static final int[] x = new int[GAME_UNITS]; // array of x and y coordinates
    static final int[] y = new int[GAME_UNITS]; // array of x and y coordinates
    int bodyParts = 6; // number of body parts of snake initially
    int applesEaten; // number of apples eaten
    int appleX; // x coordinate of apple
    int appleY; // y coordinate of apple
    char direction = 'R'; // direction of snake right
    boolean running = false; // game is not running
    Timer timer; // timer
    Random random; // random number generator

    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT)); // set size of panel
        this.setBackground(Color.black); // set background color
        this.setFocusable(true); // set focusable
        // add key listener, MyKeyAdapter is a class that extends KeyAdapter
        // and overrides the keyPressed method
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {

        newApple();
        running = true;
        // create timer with delay of 75 milliseconds, we use this class as the source
        // of the timer
        timer = new Timer(delay, this);
        timer.start();
    }

    public void paint(Graphics g) {
        super.paintComponent(g); // call paintComponent method
        draw(g); // draw the game
    }

    public void draw(Graphics g) {
        if (running) {
            /*
             * for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
             * g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT); // draw
             * horizontal lines
             * g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE); // draw vertical
             * lines
             * }
             */
            g.setColor(Color.red); // set color
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE); // draw apple at appleX and appleY coordinates
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    // g.setColor(new Color(45, 180, 0));
                    // random color
                    g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            // Score AppleEaten
            g.setColor(Color.red);
            g.setFont(new Font("Ink Free", Font.BOLD, 40));
            // FontMetrics is a class that provides information about the font
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2,
                    g.getFont().getSize());
        } else {
            gameOver(g);
        }
    }

    public void newApple() {
        appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;

    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        switch (direction) { // move snake
            case 'U':
                y[0] = y[0] - UNIT_SIZE; // subtract UNIT_SIZE from y coordinate
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE; // add UNIT_SIZE to y coordinate
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE; // subtract UNIT_SIZE from x coordinate
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE; // add UNIT_SIZE to x coordinate
                break;
        }
    }

    public void checkApple() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++; // add body part
            applesEaten++; // add 1 to applesEaten
            newApple(); // create new apple
        }
    }

    public void checkCollisions() {
        // check if head collides with body
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) { // if snake collides with itself
                running = false;
            }
        }

        // check if head touches left border
        if (x[0] < 0) { // x[0] is the x coordinate of the head
            running = false;
        }

        // check if head touches right border
        if (x[0] > SCREEN_WIDTH) {
            running = false;
        }

        // check if head touches top border
        if (y[0] < 0) { // y[0] is the y coordinate of the head
            running = false;
        }

        // check if head touches bottom border
        if (y[0] > SCREEN_HEIGHT) {
            running = false;
        }

        if (!running) {
            timer.stop();
        }
    }

    public void gameOver(Graphics g) {
        // Score AppleEaten
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        // FontMetrics is a class that provides information about the font
        FontMetrics metric = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metric.stringWidth("Score: " + applesEaten)) / 2,
                g.getFont().getSize());

        // Game OverText
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        // FontMetrics is a class that provides information about the font
        FontMetrics metrics = getFontMetrics(g.getFont());
        // drawingString is a methodthatdraws a string on the screen
        g.drawString("Game Over", (SCREEN_WIDTH - metrics.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}
