package com.company;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.Serial;
import java.util.Arrays;

import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements KeyListener, MouseListener, MouseMotionListener{
    @Serial
    private static final long serialVersionUID = 1L;
    private final BufferedImage blocks;
    private final BufferedImage background;
    private final BufferedImage pause;
    private final BufferedImage refresh;
    private final int boardHeight = 20, boardWidth = 10;
    private final int blockSize = 30;
    private final int[][] board = new int[boardHeight][boardWidth];
    private final Shape[] shapes = new Shape[7];
    private static Shape currentShape, nextShape;
    private final Timer looper;
    private int mouseX, mouseY;
    private boolean leftClick = false;
    private final Rectangle stopBounds;
    private final Rectangle refreshBounds;
    private boolean gamePaused = false;
    private boolean gameOver = false;

    private final Timer buttonLapse = new Timer(300, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            buttonLapse.stop();
        }
    });
    private int score = 0;
    public Board() {
        blocks = ImageLoader.loadImage("/textures/tiles.png");
        background = ImageLoader.loadImage("/textures/background.png");
        pause = ImageLoader.loadImage("/textures/pause.png");
        refresh = ImageLoader.loadImage("/textures/refresh.png");
        mouseX = 0;
        mouseY = 0;
        stopBounds = new Rectangle(350, 500, pause.getWidth(), pause.getHeight() + pause.getHeight()/2);
        refreshBounds = new Rectangle(350, 500 - refresh.getHeight() - 20,refresh.getWidth(),
                refresh.getHeight() + refresh.getHeight()/2);
        int FPS = 60;
        int delay = 1000 / FPS;
        looper = new Timer(delay, new GameLooper());
        shapes[0] = new Shape(new int[][] {
                {1, 1, 1, 1}               // I shape;
        }, blocks.getSubimage(0, 0, blockSize, blockSize), this, 1);
        shapes[1] = new Shape(new int[][] {
                {1, 1, 1},
                {0, 1, 0},                 // T shape;
        }, blocks.getSubimage(blockSize, 0, blockSize, blockSize), this, 2);
        shapes[2] = new Shape(new int[][] {
                {1, 1, 1},
                {1, 0, 0},                 // L shape;
        }, blocks.getSubimage(blockSize*2, 0, blockSize, blockSize), this, 3);
        shapes[3] = new Shape(new int[][] {
                {1, 1, 1},
                {0, 0, 1},                 // J shape;
        }, blocks.getSubimage(blockSize*3, 0, blockSize, blockSize), this, 4);
        shapes[4] = new Shape(new int[][] {
                {0, 1, 1},
                {1, 1, 0},                 // S shape;
        }, blocks.getSubimage(blockSize*4, 0, blockSize, blockSize), this, 5);
        shapes[5] = new Shape(new int[][] {
                {1, 1, 0},
                {0, 1, 1},                 // Z shape;
        }, blocks.getSubimage(blockSize*5, 0, blockSize, blockSize), this, 6);
        shapes[6] = new Shape(new int[][] {
                {1, 1},
                {1, 1},                    // O shape;
        }, blocks.getSubimage(blockSize*6, 0, blockSize, blockSize), this, 7);
    }

    private void update() {
        if (stopBounds.contains(mouseX, mouseY) && leftClick && !buttonLapse.isRunning() && !gameOver) {
            buttonLapse.start();
            gamePaused = !gamePaused;
        }
        if (refreshBounds.contains(mouseX, mouseY) && leftClick) {
            startGame();
        }
        if (gamePaused || gameOver) {
            return;
        }
        currentShape.update();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, null);
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col ++) {
                if (board[row][col] != 0) {
                    g.drawImage(blocks.getSubimage((board[row][col] - 1)*blockSize,
                            0, blockSize, blockSize), col*blockSize, row*blockSize, null);
                }
            }
        }
        for (int row = 0; row < nextShape.getCoords().length; row ++) {
            for (int col = 0; col < nextShape.getCoords()[0].length; col ++) {
                if (nextShape.getCoords()[row][col] != 0) {
                    g.drawImage(nextShape.getBlock(), col*30 + 320, row*30 + 50, null);
                }
            }
        }
        currentShape.render(g);
        if (stopBounds.contains(mouseX, mouseY)) {
            g.drawImage(pause.getScaledInstance(pause.getWidth() + 3, pause.getHeight() + 3, BufferedImage.SCALE_DEFAULT)
                    , stopBounds.x + 3, stopBounds.y + 3, null);
        } else {
            g.drawImage(pause, stopBounds.x, stopBounds.y, null);
        }
        if (refreshBounds.contains(mouseX, mouseY)) {
            g.drawImage(refresh.getScaledInstance(refresh.getWidth() + 3, refresh.getHeight() + 3,
                    BufferedImage.SCALE_DEFAULT), refreshBounds.x + 3, refreshBounds.y + 3, null);
        } else {
            g.drawImage(refresh, refreshBounds.x, refreshBounds.y, null);
        }
        if (gamePaused) {
            String gamePausedString = "GAME PAUSED";
            g.setColor(Color.BLUE);
            g.setFont(new Font("Georgia", Font.BOLD, 30));
            g.drawString(gamePausedString, 35, Window.HEIGHT/2);
        }
        if (gameOver) {
            String gameOverString = "GAME OVER";
            g.setColor(Color.BLUE);
            g.setFont(new Font("Georgia", Font.BOLD, 30));
            g.drawString(gameOverString, 50, Window.HEIGHT/2);
        }
        g.setColor(Color.WHITE);
        g.setFont(new Font("Georgia", Font.BOLD, 20));
        g.drawString("SCORE", Window.WIDTH - 125, Window.HEIGHT/2);
        g.drawString(score+"", Window.WIDTH - 125, Window.HEIGHT/2 + 30);
        Graphics2D g2d = (Graphics2D)g;
        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(new Color(0, 0, 0, 100));
        for (int i = 0; i <= boardHeight; i++) {
            g2d.drawLine(0, i*blockSize, boardWidth*blockSize, i*blockSize);
        }
        for (int j = 0; j <= boardWidth; j++) {
            g2d.drawLine(j*blockSize, 0, j*blockSize, boardHeight*30);
        }
    }

    public void setNextShape() {
        int index = (int)(Math.random()*shapes.length);
        nextShape = new Shape(shapes[index].getCoords(), shapes[index].getBlock(), this, shapes[index].getColor());
    }

    public void setCurrentShape() {
        currentShape = nextShape;
        setNextShape();
        for (int row = 0; row < currentShape.getCoords().length; row ++) {
            for (int col = 0; col < currentShape.getCoords()[0].length; col ++) {
                if (currentShape.getCoords()[row][col] != 0) {
                    if (board[currentShape.getY() + row][currentShape.getX() + col] != 0) {
                        gameOver = true;
                    }
                }
            }
        }
    }

    public int[][] getBoard() {
        return board;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_UP)
            currentShape.rotateShape();
        if(e.getKeyCode() == KeyEvent.VK_RIGHT)
            currentShape.setDeltaX(1);
        if(e.getKeyCode() == KeyEvent.VK_LEFT)
            currentShape.setDeltaX(-1);
        if(e.getKeyCode() == KeyEvent.VK_DOWN)
            currentShape.speedUp();
    }
    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_DOWN)
            currentShape.speedDown();
    }
    @Override
    public void keyTyped(KeyEvent e) { }

    public void startGame() {
        stopGame();
        setNextShape();
        setCurrentShape();
        gameOver = false;
        looper.start();
    }

    public void stopGame(){
        score = 0;
        for (int[] ints : board) {
            Arrays.fill(ints, 0);
        }
        looper.stop();
    }

    class GameLooper implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            update();
            repaint();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1) {
            leftClick = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1) {
            leftClick = false;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    public void addScore(){
        score+=5;
    }
}
