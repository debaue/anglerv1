package util;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputHandler implements KeyListener {

    public boolean up, down, left, right;
    public boolean fishing;
    public boolean escape;
    public boolean inventoryPressed;

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
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}
