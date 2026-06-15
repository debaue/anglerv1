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
    public boolean interactPressed;
    public boolean fishBookPressed;
    public int mouseX, mouseY;
    public boolean mouseClicked;
    public boolean spacePressed;
    public boolean enterPressed;
    public boolean backspacePressed;
    public char lastTyped = 0;

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
            case KeyEvent.VK_E -> interactPressed = true;
            case KeyEvent.VK_B -> fishBookPressed = true;
            case KeyEvent.VK_ENTER -> enterPressed = true;
            case KeyEvent.VK_BACK_SPACE -> backspacePressed = true;
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
            case KeyEvent.VK_E -> interactPressed = false;
            case KeyEvent.VK_B -> fishBookPressed = false;
            case KeyEvent.VK_ENTER -> enterPressed = false;
            case KeyEvent.VK_BACK_SPACE -> backspacePressed = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
        if (c != KeyEvent.CHAR_UNDEFINED && c >= 32 && c < 127) {
            lastTyped = c;
        }
    }

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
