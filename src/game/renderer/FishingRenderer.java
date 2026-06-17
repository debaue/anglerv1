package game.renderer;

import entities.Fish;
import game.FishingSystem;
import game.Game;
import game.GamePanel;
import util.SpriteLoader;

import java.awt.*;
import java.awt.image.BufferedImage;

public class FishingRenderer {

    private final Game game;

    public FishingRenderer(Game game) {
        this.game = game;
    }

    public void draw(Graphics2D g2) {
        g2.drawImage(SpriteLoader.getFishingEarthBg(), 0, 0, GamePanel.WIDTH, GamePanel.HEIGHT, null);
        g2.drawImage(SpriteLoader.getFishingBg(), 40, 20, GamePanel.WIDTH - 80, GamePanel.HEIGHT - 200, null);

        FishingSystem.FishState fishState = game.getFishing().getState();

        if (fishState == FishingSystem.FishState.CASTING) {
            drawCasting(g2);
            return;
        }

        int bx = game.getFishing().getBobberX();
        int by = game.getFishing().getBobberY();

        if (fishState == FishingSystem.FishState.WAITING) {
            g2.drawImage(SpriteLoader.getBobberNormal(), bx - 16, by - 16, 32, 32, null);
            return;
        }

        if (fishState == FishingSystem.FishState.BITING) {
            drawBiting(g2, bx, by);
            return;
        }

        if (fishState == FishingSystem.FishState.MINIGAME) {
            g2.drawImage(SpriteLoader.getBobberBiting(), bx - 16, by - 16, 32, 32, null);
            drawMinigame(g2);
        }

        if (fishState == FishingSystem.FishState.RESULT) {
            drawResult(g2);
        }
    }

    private void drawCasting(Graphics2D g2) {
        String zoneName = game.getFishing().getCurrentZone().name;
        g2.setColor(new Color(180, 220, 255));
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.drawString("Zone: " + zoneName, 50, GamePanel.HEIGHT - 160);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Monospaced", Font.BOLD, 22));
        String txt = "Klick ins Wasser zum Werfen!";
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(txt, GamePanel.WIDTH / 2 - fm.stringWidth(txt) / 2, GamePanel.HEIGHT - 140);
    }

    private void drawBiting(Graphics2D g2, int bx, int by) {
        g2.drawImage(SpriteLoader.getBobberBiting(), bx - 16, by - 16, 32, 32, null);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Monospaced", Font.BOLD, 28));
        String txt = "SPACE!";
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(txt, GamePanel.WIDTH / 2 - fm.stringWidth(txt) / 2, GamePanel.HEIGHT / 2);
    }

    private void drawMinigame(Graphics2D g2) {
        int barW = game.getFishing().getBarWidth();
        int barX = (GamePanel.WIDTH - barW) / 2;
        int barY = GamePanel.HEIGHT - 120;
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

    private void drawResult(Graphics2D g2) {
        if (game.getFishing().isSuccess()) {
            Fish f = game.getFishing().getLastCaughtFish();
            float t = Math.min(game.getFishing().getResultTimer() * 3.5f, 1f);
            float scale = 0.3f + t * 1.7f;

            int cx = GamePanel.WIDTH / 2;
            int cy = GamePanel.HEIGHT / 2 - 20;
            int r  = (int) (70 * scale);

            g2.setColor(Color.WHITE);
            g2.fillOval(cx - r, cy - r, r * 2, r * 2);

            if (f != null) {
                BufferedImage sprite = SpriteLoader.getFishSprite(f.type.spriteIndex);
                if (sprite != null) {
                    int imgSize = (int) (90 * scale);
                    g2.drawImage(sprite, cx - imgSize / 2, cy - imgSize / 2, imgSize, imgSize, null);
                }
                int textY = cy + r + 22;
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 20));
                FontMetrics fm = g2.getFontMetrics();
                String name = f.type.name;
                g2.drawString(name, cx - fm.stringWidth(name) / 2, textY);
                g2.setFont(new Font("Arial", Font.PLAIN, 16));
                String stats = String.format("%.2f kg  |  %.1f cm  |  %d G", f.weightKg, f.lengthCm, f.price);
                fm = g2.getFontMetrics();
                g2.drawString(stats, cx - fm.stringWidth(stats) / 2, textY + 22);
            }
        } else {
            g2.setColor(Color.RED);
            g2.setFont(new Font("Monospaced", Font.BOLD, 32));
            String txt = "Entwischt!";
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(txt, GamePanel.WIDTH / 2 - fm.stringWidth(txt) / 2, GamePanel.HEIGHT / 2);
        }
    }
}
