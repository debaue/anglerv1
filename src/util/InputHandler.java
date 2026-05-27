package util;

import game.GamePanel;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class InputHandler implements KeyListener, MouseListener {

    public boolean up, down, left, right;
    public boolean fishing;
    public boolean escape;
    public boolean inventoryPressed;
    public int mouseX, mouseY;
    public boolean mouseClicked;
    public boolean spacePressed;

    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_W -> up    = true;
            case KeyEvent.VK_S -> down  = true;
            case KeyEvent.VK_A -> left  = true;
            case KeyEvent.VK_D -> right = true;

            case KeyEvent.VK_F -> fishing = true;
            case KeyEvent.VK_ESCAPE ->  escape = true;
            case KeyEvent.VK_I -> inventoryPressed = true;
            case KeyEvent.VK_SPACE -> spacePressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_W -> up    = false;
            case KeyEvent.VK_S -> down  = false;
            case KeyEvent.VK_A -> left  = false;
            case KeyEvent.VK_D -> right = false;
            case KeyEvent.VK_F -> fishing = false;
            case KeyEvent.VK_ESCAPE -> escape = false;
            case KeyEvent.VK_I -> inventoryPressed = false;
            case KeyEvent.VK_SPACE -> spacePressed = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        Component source = (Component) e.getSource();
        mouseX = e.getX() * GamePanel.WIDTH  / source.getWidth();
        mouseY = e.getY() * GamePanel.HEIGHT / source.getHeight();
        mouseClicked = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseClicked = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
