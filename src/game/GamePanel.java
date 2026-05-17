package game;

import javax.swing.*;
import java.awt.*;

import data.RodRegistry;
import entities.Player;
import util.InputHandler;
import world.Camera;
import world.TileMap;

public class GamePanel extends JPanel {

    public static final int WIDTH = 960;
    public static final int HEIGHT = 576;
    private TileMap tileMap;
    private Camera camera;
    private Player player;
    private Timer timer;
    private InputHandler input;


    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);

        tileMap = new TileMap();
        tileMap.loadFromFile("src/world/map01.txt");
        player = new Player(RodRegistry.getStarter());
        camera = new Camera(WIDTH,HEIGHT);
        input = new InputHandler();
        addKeyListener(input);

        timer = new Timer(16, e-> {
            update(0.016f);
            repaint();
        });
        timer.start();

    }

    private void update(float delta) {
        player.update(input);
        camera.update(player.getX(), player.getY(), tileMap);

    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        for(int r = 0; r< tileMap.getRows(); r++) {
            for(int c = 0 ; c< tileMap.getCols(); c++) {
                int screenX = c* TileMap.TILE_SIZE - camera.getX();
                int screenY = r * TileMap.TILE_SIZE - camera.getY();
                g2.setColor(tileMap.getTile(c,r).color);
                g2.fillRect(screenX, screenY, TileMap.TILE_SIZE, TileMap.TILE_SIZE);
            }
        }

        g2.setColor(Color.YELLOW);
        g2.fillOval(player.getX() - camera.getX(), player.getY() - camera.getY(), 16, 16);

    }



}
