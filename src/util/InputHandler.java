package util;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class InputHandler implements KeyListener {

    public boolean up, down, left, right;
    public boolean fishing;
    public boolean escape;

    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_W -> up    = true;
            case KeyEvent.VK_S -> down  = true;
            case KeyEvent.VK_A -> left  = true;
            case KeyEvent.VK_D -> right = true;

            case KeyEvent.VK_F -> fishing = true;
            case KeyEvent.VK_ESCAPE ->  escape = true;
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
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}
