package game;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import data.RodRegistry;
import entities.Player;
import util.InputHandler;
import util.SpriteLoader;
import world.Camera;
import world.TileMap;
import world.TileType;

public class GamePanel extends JPanel {

    public static final int WIDTH = 960;
    public static final int HEIGHT = 576;
    private TileMap tileMap;
    private Camera camera;
    private Player player;
    private Timer timer;
    private InputHandler input;


    public GamePanel() {
        SpriteLoader.init();
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
        player.update(delta, input);
        camera.update(player.getX(), player.getY(), tileMap);

    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.scale(
                (double) getWidth()  / WIDTH,
                (double) getHeight() / HEIGHT
        );

        g2.setColor(new Color(255, 255, 255, 60));
        drawTileLayer(g2, TileType.GRASS, SpriteLoader.getGrassTiles());
        drawTileLayer(g2, TileType.WATER, SpriteLoader.getWaterTiles());
        drawTileLayer(g2,TileType.GROUND,SpriteLoader.getGroundtiles());
       g2.drawImage(player.getCurrentFrame(), player.getX()- camera.getX(), player.getY()- camera.getY(), null);

    }

    private void drawTileLayer(Graphics2D g2, TileType type, BufferedImage[] tiles) {
        for(int r = 0; r <= tileMap.getRows(); r++) {
            for(int c = 0; c <= tileMap.getCols(); c++) {
                int screenX = c * 32 - camera.getX();
                int screenY = r * 32 - camera.getY();

                 if(screenX + 32 < 0 || screenX > WIDTH)  continue;
                if(screenY + 32 < 0 || screenY > HEIGHT) continue;

                int mask = tileMap.getDualMask(c, r, type);
                if(mask == 0) continue;
                int index = type.lookup[mask];
                g2.drawImage(tiles[index], screenX, screenY, null);
                g2.setColor(Color.RED);
                g2.setFont(new Font("Arial", Font.PLAIN, 8));
                g2.drawString(String.valueOf(mask), screenX + 2, screenY + 10);

            }
        }
    }



}
