package com.company;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serial;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Title extends JPanel implements MouseListener, MouseMotionListener {
    @Serial
    private static final long serialVersionUID = 1L;
    private int mouseX, mouseY;
    private final Rectangle bounds;
    private boolean leftClick = false;
    private BufferedImage title, instructions, play;
    private final Window window;
    private final BufferedImage[] playButton = new BufferedImage[2];
    public Title(Window window) {
        try {
            title = ImageIO.read(Board.class.getResource("/textures/Title.png"));
            instructions = ImageIO.read(Board.class.getResource("/textures/arrow.png"));
            play = ImageIO.read(Board.class.getResource("/textures/play.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Timer timer = new Timer(1000 / 60, e -> repaint());
        timer.start();
        mouseX = 0;
        mouseY = 0;
        playButton[0] = play.getSubimage(0, 0, 100, 80);
        playButton[1] = play.getSubimage(100, 0, 100, 80);
        bounds = new Rectangle(Window.WIDTH/2 - 50, Window.HEIGHT/2 - 100, 100, 80);
        this.window = window;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (leftClick && bounds.contains(mouseX, mouseY)) {
            window.startTetris();
        }
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, Window.WIDTH, Window.HEIGHT);
        g.drawImage(title, Window.WIDTH/2 - title.getWidth()/2, Window.HEIGHT/2 - title.getHeight()/2 - 200, null);
        g.drawImage(instructions, Window.WIDTH/2 - instructions.getWidth()/2,
                Window.HEIGHT/2 - instructions.getHeight()/2 + 150, null);
        if (bounds.contains(mouseX, mouseY)) {
            g.drawImage(playButton[0], Window.WIDTH/2 - 50, Window.HEIGHT/2 - 100, null);
        } else {
            g.drawImage(playButton[1], Window.WIDTH/2 - 50, Window.HEIGHT/2 - 100, null);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent mouse) {
        if (mouse.getButton() == MouseEvent.BUTTON1) {
            leftClick = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent mouse) {
        if (mouse.getButton() == MouseEvent.BUTTON1) {
            leftClick = false;
        }
    }

    @Override
    public void mouseEntered(MouseEvent mouse) {
    }

    @Override
    public void mouseExited(MouseEvent mouse) {
    }

    @Override
    public void mouseDragged(MouseEvent mouse) {
        mouseX = mouse.getX();
        mouseY = mouse.getY();
    }

    @Override
    public void mouseMoved(MouseEvent mouse) {
        mouseX = mouse.getX();
        mouseY = mouse.getY();
    }
}
