package game.renderer;

import game.Game;
import game.GamePanel;
import data.FishRegistry;
import entities.FishBookEntry;
import util.SpriteLoader;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;

public class FishBookRenderer {

    private final Game game;

    public FishBookRenderer(Game game) {
        this.game = game;
    }

    public void draw(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);

        int panelX = 40, panelY = 40;
        int panelW = GamePanel.WIDTH - 80, panelH = GamePanel.HEIGHT - 80;

        g2.setColor(new Color(30, 50, 35, 245));
        g2.fillRoundRect(panelX, panelY, panelW, panelH, 20, 20);
        g2.setColor(new Color(80, 160, 90));
        g2.drawRoundRect(panelX, panelY, panelW, panelH, 20, 20);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 24));
        g2.drawString("Fischbuch", panelX + 20, panelY + 35);

        Map<String, FishBookEntry> book = game.getPlayer().getFishBook();

        if (book.isEmpty()) {
            g2.setColor(new Color(160, 160, 160));
            g2.setFont(new Font("Arial", Font.ITALIC, 16));
            g2.drawString("Noch keine Fische gefangen.", panelX + 20, panelY + 80);
        } else {
            int colW = (panelW - 40) / 2, rowH = 52;
            int startY = panelY + 60;
            int i = 0;
            for (FishBookEntry entry : book.values()) {
                int col = i % 2, row = i / 2;
                int ex = panelX + 20 + col * (colW + 10);
                int ey = startY + row * rowH;
                if (ey + rowH > panelY + panelH - 30) break;

                boolean isNew = entry.getCaughtCount() == 1;
                g2.setColor(isNew ? new Color(60, 120, 70) : new Color(45, 65, 50));
                g2.fillRoundRect(ex, ey, colW - 10, rowH - 6, 10, 10);

                BufferedImage sprite = SpriteLoader.getFishSprite(
                        FishRegistry.findByName(entry.getFishName()) != null
                        ? FishRegistry.findByName(entry.getFishName()).spriteIndex : -1);
                if (sprite != null) {
                    int imgSize = rowH - 10;
                    g2.setColor(Color.WHITE);
                    g2.fillRect(ex + 4, ey + 2, imgSize, imgSize);
                    g2.drawImage(sprite, ex + 4, ey + 2, imgSize, imgSize, null);
                }

                int tx = ex + rowH + 2;
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 13));
                g2.drawString(entry.getFishName(), tx, ey + 16);
                g2.setFont(new Font("Arial", Font.PLAIN, 11));
                g2.setColor(new Color(200, 220, 200));
                g2.drawString("Gefangen: " + entry.getCaughtCount() + "x", tx, ey + 29);
                g2.drawString(String.format("Schwerste: %.2fkg   Längste: %.1fcm",
                        entry.getHeaviestWeight(), entry.getLongestLength()), tx, ey + 42);
                i++;
            }
        }

        g2.setColor(new Color(160, 160, 160));
        g2.setFont(new Font("Arial", Font.PLAIN, 13));
        g2.drawString("[B] / ESC = schließen", panelX + 20, panelY + panelH - 10);
    }
}
