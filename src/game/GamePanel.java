package game;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import entities.Fish;
import util.InputHandler;
import util.SpriteLoader;
import world.TileType;

public class GamePanel extends JPanel {

    public static final int WIDTH = 960;
    public static final int HEIGHT = 576;
    private final Game game;
    private final Timer timer;


    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);

        InputHandler input = new InputHandler();
        addKeyListener(input);

        game = new Game(input);

        timer = new Timer(16, e-> {
            game.update(0.016f);
            repaint();
        });
        timer.start();

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.scale(
                (double) getWidth() / WIDTH,
                (double) getHeight() / HEIGHT
        );

        drawTileLayer(g2, TileType.WATER, SpriteLoader.getWaterTiles());
        drawTileLayer(g2, TileType.GRASS, SpriteLoader.getGrassTiles());
        drawTileLayer(g2, TileType.GROUND, SpriteLoader.getGroundtiles());

        g2.drawImage(
                game.getPlayer().getCurrentFrame(),
                game.getPlayer().getX() - game.getCamera().getX(),
                game.getPlayer().getY() - game.getCamera().getY(),
                null
        );

        switch (game.getState()) {
            case FISHING_MENU -> drawFishingMenu(g2);
            case INVENTORY -> drawInventory(g2);
        }
    }

    private void drawTileLayer(Graphics2D g2, TileType type, BufferedImage[] tiles) {
        for(int r = 0; r <= game.getTileMap().getRows(); r++) {
            for(int c = 0; c <= game.getTileMap().getCols(); c++) {
                int screenX = c * 32 - game.getCamera().getX();
                int screenY = r * 32 - game.getCamera().getY();

                 if(screenX + 32 < 0 || screenX > WIDTH)  continue;
                if(screenY + 32 < 0 || screenY > HEIGHT) continue;
                int mask = game.getTileMap().getDualMask(c, r, type);
                if(mask == 0) continue;
                int index = type.lookup[mask];
                g2.drawImage(tiles[index], screenX, screenY, null);
            }
        }
    }


    private void drawFishingMenu(Graphics2D g2) {

        g2.setColor(new Color(0, 0, 0, 180)); // ← höher = dunkler
        g2.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);

        g2.setColor(new Color(40, 60, 40, 220));
        g2.fillRoundRect(300, 180, 360, 200, 16, 16);
    }

    private void drawInventory(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0, 170));
        g2.fillRect(0, 0, WIDTH, HEIGHT);

        int panelX = 180;
        int panelY = 100;
        int panelW = 600;
        int panelH = 360;

        g2.setColor(new Color(50, 60, 70, 230));
        g2.fillRoundRect(panelX, panelY, panelW, panelH, 20, 20);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 24));
        g2.drawString("Inventory", panelX + 20, panelY + 35);

        g2.setFont(new Font("Arial", Font.PLAIN, 16));
        g2.drawString(
                "Slots: " + game.getPlayer().getInventory().size() + "/" + game.getPlayer().getMaxSlots(),
                panelX + 20,
                panelY + 60
        );

        int slotSize = 64;
        int gap = 12;
        int startX = panelX + 20;
        int startY = panelY + 90;

        for (int i = 0; i < game.getPlayer().getMaxSlots(); i++) {
            int col = i % 6;
            int row = i / 6;
            int x = startX + col * (slotSize + gap);
            int y = startY + row * (slotSize + gap);

            g2.setColor(new Color(90, 100, 110));
            g2.fillRect(x, y, slotSize, slotSize);
            g2.setColor(Color.WHITE);
            g2.drawRect(x, y, slotSize, slotSize);

            if (i < game.getPlayer().getInventory().size()) {
                Fish fish = game.getPlayer().getInventory().get(i);
                g2.drawString(fish.type.name, x + 6, y + 20);
            }
        }
    }

}
