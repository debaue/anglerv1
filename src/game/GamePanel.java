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
        addMouseListener(input);

        game = new Game(input);

        timer = new Timer(16, e -> {
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
            case FISHING_MENU -> drawFishingScreen(g2);
            case INVENTORY -> drawInventory(g2);
        }
    }

    private void drawTileLayer(Graphics2D g2, TileType type, BufferedImage[] tiles) {
        for (int r = 0; r <= game.getTileMap().getRows(); r++) {
            for (int c = 0; c <= game.getTileMap().getCols(); c++) {
                int screenX = c * 32 - game.getCamera().getX();
                int screenY = r * 32 - game.getCamera().getY();

                if (screenX + 32 < 0 || screenX > WIDTH) continue;
                if (screenY + 32 < 0 || screenY > HEIGHT) continue;
                int mask = game.getTileMap().getDualMask(c, r, type);
                if (mask == 0) continue;
                int index = type.lookup[mask];
                g2.drawImage(tiles[index], screenX, screenY, null);
            }
        }
    }


    private void drawFishingScreen(Graphics2D g2) {
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, WIDTH, HEIGHT);

        g2.setColor(new Color(100, 180, 240));
        g2.fillRoundRect(40, 20, WIDTH - 80, HEIGHT - 200, 60, 60);

        FishingSystem.FishState fishState = game.getFishing().getState();

        if(fishState == FishingSystem.FishState.CASTING) {
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Monospaced", Font.BOLD, 22));
            String txt = "Klick ins Wasser zum Werfen!";
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(txt, WIDTH/2 - fm.stringWidth(txt)/2, HEIGHT - 140);
            return;
        }

        int bx = game.getFishing().getBobberX();
        int by = game.getFishing().getBobberY();

        if(fishState == FishingSystem.FishState.BITING) {
            if((int)(game.getFishing().getBiteTimer() * 4) % 2 == 0) {
                g2.setColor(Color.YELLOW);
                g2.fillOval(bx - 18, by - 18, 36, 36);
            }
            g2.setColor(Color.RED);
            g2.fillOval(bx - 12, by - 12, 24, 24);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Monospaced", Font.BOLD, 28));
            String txt = "SPACE!";
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(txt, WIDTH/2 - fm.stringWidth(txt)/2, HEIGHT/2);
            return;
        }

        if(fishState == FishingSystem.FishState.WAITING) {
            g2.setColor(Color.RED);
            g2.fillOval(bx - 12, by - 12, 24, 24);
            return;
        }

        if(fishState == FishingSystem.FishState.MINIGAME) {
            g2.setColor(Color.RED);
            g2.fillOval(bx - 12, by - 12, 24, 24);

            int barY = HEIGHT - 120;
            int barW = game.getFishing().getBarWidth();
            int barX = (WIDTH - barW) / 2;
            int barH = 50;

            g2.setColor(new Color(255, 180, 200));
            g2.fillRect(barX, barY, barW, barH);

            int greenX = barX + (int) game.getFishing().getGreenStart();
            int greenW = (int) game.getFishing().getGreenWidth();
            g2.setColor(new Color(50, 180, 50));
            g2.fillRect(greenX, barY, greenW, barH);

            int markerX = barX + (int) game.getFishing().getMarkerX();
            g2.setColor(new Color(40, 20, 10));
            g2.fillRect(markerX - 10, barY - 5, 20, barH + 10);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Monospaced", Font.BOLD, 16));
            g2.drawString("Treffer: " + game.getFishing().getHits() + "/3", barX, barY - 10);
            g2.drawString("Fehler: " + game.getFishing().getMisses() + "/2", barX + barW - 100, barY - 10);
        }

        if(fishState == FishingSystem.FishState.RESULT) {
            g2.setColor(game.getFishing().isSuccess() ? Color.GREEN : Color.RED);
            g2.setFont(new Font("Monospaced", Font.BOLD, 32));
            String txt = game.getFishing().isSuccess() ? "Gefangen!" : "Entwischt!";
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(txt, WIDTH/2 - fm.stringWidth(txt)/2, HEIGHT/2);
        }
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
