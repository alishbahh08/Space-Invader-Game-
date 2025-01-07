/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package space_game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class SpaceInvaders extends JPanel implements ActionListener, KeyListener {

    // Game constants
    private final int WIDTH = 800;
    private final int HEIGHT = 600;
    private final int PLAYER_WIDTH = 50;
    private final int PLAYER_HEIGHT = 30;
    private final int ENEMY_WIDTH = 40;
    private final int ENEMY_HEIGHT = 20;
    private final int BULLET_WIDTH = 5;
    private final int BULLET_HEIGHT = 10;

    // Game objects
    private Timer timer;
    private int playerX = WIDTH / 2 - PLAYER_WIDTH / 2;
    private boolean moveLeft = false, moveRight = false;
    private ArrayList<Rectangle> bullets = new ArrayList<>();
    private ArrayList<Rectangle> enemies = new ArrayList<>();

    // Game state
    private int score = 0;
    private int level = 1;
    private int enemySpeed = 1;

    public SpaceInvaders() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        timer = new Timer(15, this);
        timer.start();
        spawnEnemies();
    }

    private void spawnEnemies() {
        enemies.clear();
        Random random = new Random();
        for (int i = 0; i < 5 + level; i++) {
            int x = random.nextInt(WIDTH - ENEMY_WIDTH);
            int y = random.nextInt(HEIGHT / 3);
            enemies.add(new Rectangle(x, y, ENEMY_WIDTH, ENEMY_HEIGHT));
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw player
        g.setColor(Color.BLUE);
        g.fillRect(playerX, HEIGHT - PLAYER_HEIGHT - 10, PLAYER_WIDTH, PLAYER_HEIGHT);

        // Draw bullets
        g.setColor(Color.YELLOW);
        for (Rectangle bullet : bullets) {
            g.fillRect(bullet.x, bullet.y, BULLET_WIDTH, BULLET_HEIGHT);
        }

        // Draw enemies
        g.setColor(Color.RED);
        for (Rectangle enemy : enemies) {
            g.fillRect(enemy.x, enemy.y, ENEMY_WIDTH, ENEMY_HEIGHT);
        }

        // Draw score and level
        g.setColor(Color.WHITE);
        g.drawString("Score: " + score, 10, 20);
        g.drawString("Level: " + level, WIDTH - 80, 20);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Move player
        if (moveLeft && playerX > 0) playerX -= 5;
        if (moveRight && playerX < WIDTH - PLAYER_WIDTH) playerX += 5;

        // Move bullets
        Iterator<Rectangle> bulletIterator = bullets.iterator();
        while (bulletIterator.hasNext()) {
            Rectangle bullet = bulletIterator.next();
            bullet.y -= 10;
            if (bullet.y < 0) {
                bulletIterator.remove();
            }
        }

        // Move enemies
        Iterator<Rectangle> enemyIterator = enemies.iterator();
        while (enemyIterator.hasNext()) {
            Rectangle enemy = enemyIterator.next();
            enemy.y += enemySpeed;
            if (enemy.y > HEIGHT) {
                // Game over condition (enemy reaches bottom)
                timer.stop();
                JOptionPane.showMessageDialog(this, "Game Over! Your score: " + score);
                System.exit(0);
            }
        }

        // Check for collisions
        for (Iterator<Rectangle> bIter = bullets.iterator(); bIter.hasNext(); ) {
            Rectangle bullet = bIter.next();
            for (Iterator<Rectangle> eIter = enemies.iterator(); eIter.hasNext(); ) {
                Rectangle enemy = eIter.next();
                if (bullet.intersects(enemy)) {
                    bIter.remove();
                    eIter.remove();
                    score += 10;
                    break;
                }
            }
        }

        // Level up
        if (enemies.isEmpty()) {
            level++;
            enemySpeed++;
            spawnEnemies();
        }

        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            moveLeft = true;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            moveRight = true;
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            // Fire bullet
            bullets.add(new Rectangle(playerX + PLAYER_WIDTH / 2 - BULLET_WIDTH / 2, HEIGHT - PLAYER_HEIGHT - 20, BULLET_WIDTH, BULLET_HEIGHT));
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            moveLeft = false;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            moveRight = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Space Invaders");
        SpaceInvaders game = new SpaceInvaders();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
