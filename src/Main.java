import game.GamePanel;

import javax.swing.*;

public class Main extends JFrame{
    public static void main(String[] args) {


        JFrame frame = new JFrame();
        GamePanel panel = new GamePanel();

        frame.add(panel);
        frame.setVisible(true);
        frame.pack();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}