package game.renderer;

import game.Game;
import game.GamePanel;
import entities.Fish;
import util.SpriteLoader;

import java.awt.*;

public class InventoryRenderer {

    private final Game game;

    public InventoryRenderer(Game game) {
        this.game = game;
    }

    public void draw(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0, 170));
        g2.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);

        int panelX = 180, panelY = 100, panelW = 600, panelH = 360;
        g2.setColor(new Color(50, 60, 70, 230));
        g2.fillRoundRect(panelX, panelY, panelW, panelH, 20, 20);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 24));
        g2.drawString("Inventory", panelX + 20, panelY + 35);

        g2.setFont(new Font("Arial", Font.PLAIN, 16));
        g2.drawString("Slots: " + game.getPlayer().getInventory().size() + "/" + game.getPlayer().getMaxSlots(),
                panelX + 20, panelY + 60);

        int slotSize = 64, gap = 12;
        int startX = panelX + 20, startY = panelY + 90;

        for (int i = 0; i < game.getPlayer().getMaxSlots(); i++) {
            int col = i % 6, row = i / 6;
            int x = startX + col * (slotSize + gap);
            int y = startY + row * (slotSize + gap);

            g2.setColor(Color.WHITE);
            g2.fillRect(x, y, slotSize, slotSize);
            g2.setColor(new Color(160, 160, 160));
            g2.drawRect(x, y, slotSize, slotSize);

            if (i < game.getPlayer().getInventory().size()) {
                Fish fish = game.getPlayer().getInventory().get(i);
                java.awt.image.BufferedImage fishImg = SpriteLoader.getFishSprite(fish.type.spriteIndex);
                if (fishImg != null) {
                    g2.drawImage(fishImg, x + 2, y + 2, slotSize - 4, slotSize - 4, null);
                }
                g2.setColor(new Color(0, 0, 0, 140));
                g2.fillRect(x, y + slotSize - 18, slotSize, 18);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 9));
                g2.drawString(fish.type.name, x + 3, y + slotSize - 5);
            }
        }
    }
}
