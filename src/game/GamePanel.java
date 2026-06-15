package game;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import data.FishRegistry;
import entities.Fish;
import entities.FishBookEntry;
import entities.HitBox;
import entities.Player;
import entities.ShopKeeper;
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

        drawShopKeeper(g2);

        g2.drawImage(
                game.getPlayer().getCurrentFrame(),
                game.getPlayer().getX() - game.getCamera().getX(),
                game.getPlayer().getY() - game.getCamera().getY(),
                null
        );

        drawHud(g2);

        switch (game.getState()) {
            case FISHING_MENU -> drawFishingScreen(g2);
            case INVENTORY    -> drawInventory(g2);
            case SHOP         -> drawShop(g2);
            case FISH_BOOK    -> drawFishBook(g2);
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
        g2.drawImage(SpriteLoader.getFishingEarthBg(), 0, 0, WIDTH, HEIGHT, null);

        g2.drawImage(SpriteLoader.getFishingBg(), 40, 20, WIDTH-80, HEIGHT-200, null);

        FishingSystem.FishState fishState = game.getFishing().getState();

        if(fishState == FishingSystem.FishState.CASTING) {
            String zoneName = game.getFishing().getCurrentZone().name;
            g2.setColor(new Color(180, 220, 255));
            g2.setFont(new Font("Arial", Font.BOLD, 14));
            g2.drawString("Zone: " + zoneName, 50, HEIGHT - 160);

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
            g2.drawImage(SpriteLoader.getBobberBiting(),
                    bx - 16, by - 16, 32, 32, null);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Monospaced", Font.BOLD, 28));
            String txt = "SPACE!";
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(txt, WIDTH/2 - fm.stringWidth(txt)/2, HEIGHT/2);
            return;
        }

        if(fishState == FishingSystem.FishState.WAITING) {
            g2.drawImage(SpriteLoader.getBobberNormal(),
                    bx - 16, by - 16, 32, 32, null);
            return;
        }

        if(fishState == FishingSystem.FishState.MINIGAME) {
            g2.drawImage(SpriteLoader.getBobberBiting(),
                    bx - 16, by - 16, 32, 32, null);

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
            if(game.getFishing().isSuccess()) {
                Fish f = game.getFishing().getLastCaughtFish();
                float t = Math.min(game.getFishing().getResultTimer() * 3.5f, 1f);
                float scale = 0.3f + t * 1.7f;

                int cx = WIDTH / 2;
                int cy = HEIGHT / 2 - 20;
                int r  = (int)(70 * scale);

                g2.setColor(Color.WHITE);
                g2.fillOval(cx - r, cy - r, r * 2, r * 2);

                if(f != null) {
                    java.awt.image.BufferedImage sprite = SpriteLoader.getFishSprite(f.type.spriteIndex);
                    if(sprite != null) {
                        int imgSize = (int)(90 * scale);
                        g2.drawImage(sprite, cx - imgSize/2, cy - imgSize/2, imgSize, imgSize, null);
                    }
                    int textY = cy + r + 22;
                    g2.setColor(Color.WHITE);
                    g2.setFont(new Font("Arial", Font.BOLD, 20));
                    FontMetrics fm = g2.getFontMetrics();
                    String name = f.type.name;
                    g2.drawString(name, cx - fm.stringWidth(name)/2, textY);
                    g2.setFont(new Font("Arial", Font.PLAIN, 16));
                    String stats = String.format("%.2f kg  |  %.1f cm  |  %d G", f.weightKg, f.lengthCm, f.price);
                    fm = g2.getFontMetrics();
                    g2.drawString(stats, cx - fm.stringWidth(stats)/2, textY + 22);
                }
            } else {
                g2.setColor(Color.RED);
                g2.setFont(new Font("Monospaced", Font.BOLD, 32));
                String txt = "Entwischt!";
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(txt, WIDTH/2 - fm.stringWidth(txt)/2, HEIGHT/2);
            }
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

            g2.setColor(Color.WHITE);
            g2.fillRect(x, y, slotSize, slotSize);
            g2.setColor(new Color(160, 160, 160));
            g2.drawRect(x, y, slotSize, slotSize);

            if (i < game.getPlayer().getInventory().size()) {
                Fish fish = game.getPlayer().getInventory().get(i);
                java.awt.image.BufferedImage fishImg = util.SpriteLoader.getFishSprite(fish.type.spriteIndex);
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

    private void drawHud(Graphics2D g2) {
        Player p = game.getPlayer();
        int pad = 10;
        int panelH = 48;

        g2.setColor(new Color(0, 0, 0, 160));
        g2.fillRoundRect(pad, pad, 200, panelH, 12, 12);

        g2.setColor(new Color(255, 210, 50));
        g2.setFont(new Font("Arial", Font.BOLD, 18));
        g2.drawString("Gold: " + p.getGold() + "G", pad + 12, pad + 28);

        g2.setColor(new Color(180, 220, 255));
        g2.setFont(new Font("Arial", Font.PLAIN, 13));
        g2.drawString("Fische: " + p.getInventory().size() + "/" + p.getMaxSlots(),
                pad + 12, pad + 44);
        String rodName  = p.getEquippedRod()  != null ? p.getEquippedRod().name  : "Keine Rute";
        String baitName = p.getEquippedBait() != null ? p.getEquippedBait().name : "Kein Köder";
        String baitCountStr = p.getBaitCount() < 0 ? "∞" : "x" + p.getBaitCount();

        int rodPanelW = 210;
        int rodPanelH = 68;
        int rodPanelX = WIDTH - rodPanelW - pad;

        g2.setColor(new Color(0, 0, 0, 160));
        g2.fillRoundRect(rodPanelX, pad, rodPanelW, rodPanelH, 12, 12);

        g2.setColor(new Color(180, 140, 80));
        g2.setFont(new Font("Arial", Font.BOLD, 12));
        g2.drawString("Rute:", rodPanelX + 10, pad + 18);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.PLAIN, 13));
        g2.drawString(rodName, rodPanelX + 55, pad + 18);

        boolean hasUpgradedBait = p.getBaitCount() > 0;
        g2.setColor(hasUpgradedBait ? new Color(120, 220, 120) : new Color(160, 160, 160));
        g2.setFont(new Font("Arial", Font.BOLD, 12));
        g2.drawString("Köder:", rodPanelX + 10, pad + 38);
        g2.setColor(hasUpgradedBait ? new Color(180, 255, 180) : new Color(200, 200, 200));
        g2.setFont(new Font("Arial", Font.PLAIN, 13));
        g2.drawString(baitName + " " + baitCountStr, rodPanelX + 60, pad + 38);

        g2.setColor(new Color(100, 100, 100));
        g2.setFont(new Font("Arial", Font.PLAIN, 10));
        g2.drawString(hasUpgradedBait ? "+" + (int)(p.getEquippedBait().rarityBonus * 100) + "% seltene Fische" : "kein Bonus",
                rodPanelX + 10, pad + 56);

        String hint = null;
        if (game.getState() == Game.GameState.EXPLORING) {
            if (p.isNearWater()) hint = "[F] Angeln";
            if (isNearShopKeeperPublic()) hint = "[E] Shop öffnen";
        }
        if (hint != null) {
            g2.setFont(new Font("Arial", Font.BOLD, 16));
            int hw = g2.getFontMetrics().stringWidth(hint) + 24;
            int hx = (WIDTH - hw) / 2;
            int hy = HEIGHT - 40;
            g2.setColor(new Color(0, 0, 0, 180));
            g2.fillRoundRect(hx, hy, hw, 28, 10, 10);
            g2.setColor(Color.WHITE);
            g2.drawString(hint, hx + 12, hy + 20);
        }

        String lockedMsg = game.getLockedZoneMessage();
        if (lockedMsg != null) {
            g2.setFont(new Font("Arial", Font.BOLD, 15));
            int lw = g2.getFontMetrics().stringWidth(lockedMsg) + 28;
            int lx = (WIDTH - lw) / 2;
            int ly = HEIGHT - 80;
            g2.setColor(new Color(160, 30, 30, 210));
            g2.fillRoundRect(lx, ly, lw, 30, 10, 10);
            g2.setColor(Color.WHITE);
            g2.drawString(lockedMsg, lx + 14, ly + 21);
        }
        g2.setColor(new Color(0, 0, 0, 130));
        g2.fillRoundRect(WIDTH - 90, HEIGHT - 36, 80, 26, 8, 8);
        g2.setColor(new Color(200, 200, 200));
        g2.setFont(new Font("Arial", Font.PLAIN, 12));
        g2.drawString("[I] Inventar", WIDTH - 83, HEIGHT - 18);

        g2.setColor(new Color(0, 0, 0, 130));
        g2.fillRoundRect(WIDTH - 90, HEIGHT - 68, 80, 26, 8, 8);
        g2.setColor(new Color(180, 220, 180));
        g2.setFont(new Font("Arial", Font.PLAIN, 12));
        g2.drawString("[B] Fischbuch", WIDTH - 88, HEIGHT - 50);
    }

    private boolean isNearShopKeeperPublic() {
        HitBox pk  = game.getShopKeeper().getHitBox();
        HitBox plr = game.getPlayer().getHitBox();
        int range = 48;
        return plr.x < pk.x + pk.width  + range &&
               plr.x + plr.width  > pk.x - range &&
               plr.y < pk.y + pk.height + range &&
               plr.y + plr.height > pk.y - range;
    }

    private void drawShopKeeper(Graphics2D g2) {
        ShopKeeper sk = game.getShopKeeper();
        int sx = sk.getX() - game.getCamera().getX();
        int sy = sk.getY() - game.getCamera().getY();
        int sw = ShopKeeper.SPRITE_W;
        int sh = ShopKeeper.SPRITE_H;

        BufferedImage sprite = SpriteLoader.getShopKeeper();
        g2.drawImage(sprite, sx, sy, sw, sh, null);
    }

    private void drawShop(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(0, 0, WIDTH, HEIGHT);

        int panelX = 40;
        int panelY = 60;
        int panelW = WIDTH - 80;
        int panelH = HEIGHT - 120;

        g2.setColor(new Color(40, 50, 40, 240));
        g2.fillRoundRect(panelX, panelY, panelW, panelH, 20, 20);
        g2.setColor(new Color(100, 160, 80));
        g2.drawRoundRect(panelX, panelY, panelW, panelH, 20, 20);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 26));
        g2.drawString("Shop", panelX + 20, panelY + 36);

        g2.setFont(new Font("Arial", Font.PLAIN, 16));
        g2.setColor(new Color(255, 220, 80));
        g2.drawString("Gold: " + game.getPlayer().getGold() + "G", panelX + 20, panelY + 60);

        int leftX = panelX + 20;
        int itemY  = panelY + 90;
        int itemW  = (panelW / 2) - 40;
        int rowH   = 40;
        int listMaxH = panelH - 90 - 50; // leave room for buy button

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.drawString("Kaufen (SPACE / Klick)", leftX, itemY - 10);

        ShopSystem shop = game.getShopSystem();
        Player p2 = game.getPlayer();
        java.util.List<data.ShopItem> items = shop.getVisibleItems(p2);
        shop.rebuildItemRects(leftX, itemY, itemW, rowH, items.size());

        Shape oldClip = g2.getClip();
        g2.setClip(panelX, itemY, panelW, listMaxH);
        for (int i = 0; i < items.size(); i++) {
            data.ShopItem item = items.get(i);
            int iy = itemY + i * rowH;

            boolean selected = (i == shop.getSelectedIndex());
            boolean isBaitItem = item.getType() == data.ShopItem.Type.BAIT;
            boolean equippedBait = isBaitItem && p2.getEquippedBait() != null
                    && p2.getEquippedBait().name.equals(item.getName())
                    && p2.getBaitCount() > 0;
            String badge = equippedBait ? " x" + p2.getBaitCount() : "";

            g2.setColor(selected ? new Color(80, 140, 60) : new Color(55, 70, 55));
            g2.fillRoundRect(leftX, iy, itemW, rowH - 4, 10, 10);

            g2.setColor(selected ? Color.WHITE : new Color(200, 200, 200));
            g2.setFont(new Font("Arial", Font.BOLD, 13));
            g2.drawString(item.getName() + badge, leftX + 10, iy + 16);
            g2.setFont(new Font("Arial", Font.PLAIN, 12));
            g2.setColor(new Color(255, 220, 80));
            String priceLabel = item.getPrice() + "G" + (isBaitItem ? "  (5x)" : "");
            g2.drawString(priceLabel, leftX + 10, iy + 30);
        }
        g2.setClip(oldClip);

        int btnX = leftX;
        int btnY = panelY + panelH - 44;
        shop.setBuyButton(new Rectangle(btnX, btnY, itemW, 32));
        g2.setColor(new Color(60, 140, 60));
        g2.fillRoundRect(btnX, btnY, itemW, 32, 8, 8);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.drawString("Kaufen [SPACE]", btnX + 12, btnY + 21);

        int rightX = panelX + panelW / 2 + 20;
        int rightW = panelW / 2 - 40;

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.drawString("Inventar", rightX, itemY - 10);

        java.util.List<Fish> inv = game.getPlayer().getInventory();
        g2.setFont(new Font("Arial", Font.PLAIN, 13));
        for (int i = 0; i < inv.size(); i++) {
            Fish fish = inv.get(i);
            int iy = itemY + i * 22;
            g2.setColor(new Color(200, 220, 200));
            g2.drawString(fish.type.name + " – " + fish.weightKg + "kg = " + fish.price + "G",
                    rightX, iy + 14);
        }
        if (inv.isEmpty()) {
            g2.setColor(new Color(150, 150, 150));
            g2.drawString("Keine Fische", rightX, itemY + 14);
        }

        int sellBtnY = panelY + panelH - 50;
        shop.setSellAllButton(new Rectangle(rightX, sellBtnY, rightW, 32));
        g2.setColor(new Color(160, 80, 40));
        g2.fillRoundRect(rightX, sellBtnY, rightW, 32, 8, 8);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.drawString("Alle verkaufen", rightX + 10, sellBtnY + 21);

        g2.setColor(new Color(160, 160, 160));
        g2.setFont(new Font("Arial", Font.PLAIN, 13));
        g2.drawString("W/S = wählen   SPACE = kaufen   ESC = schließen",
                panelX + 20, panelY + panelH - 52);
    }

    private void drawFishBook(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(0, 0, WIDTH, HEIGHT);

        int panelX = 40;
        int panelY = 40;
        int panelW = WIDTH - 80;
        int panelH = HEIGHT - 80;

        g2.setColor(new Color(30, 50, 35, 245));
        g2.fillRoundRect(panelX, panelY, panelW, panelH, 20, 20);
        g2.setColor(new Color(80, 160, 90));
        g2.drawRoundRect(panelX, panelY, panelW, panelH, 20, 20);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 24));
        g2.drawString("Fischbuch", panelX + 20, panelY + 35);

        java.util.Map<String, FishBookEntry> book = game.getPlayer().getFishBook();

        if (book.isEmpty()) {
            g2.setColor(new Color(160, 160, 160));
            g2.setFont(new Font("Arial", Font.ITALIC, 16));
            g2.drawString("Noch keine Fische gefangen.", panelX + 20, panelY + 80);
        } else {
            int colW  = (panelW - 40) / 2;
            int rowH  = 52;
            int startY = panelY + 60;
            int i = 0;
            for (FishBookEntry entry : book.values()) {
                int col = i % 2;
                int row = i / 2;
                int ex  = panelX + 20 + col * (colW + 10);
                int ey  = startY + row * rowH;

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
                g2.drawString(
                        String.format("Schwerste: %.2fkg   Längste: %.1fcm",
                                entry.getHeaviestWeight(), entry.getLongestLength()),
                        tx, ey + 42);
                i++;
            }
        }

        g2.setColor(new Color(160, 160, 160));
        g2.setFont(new Font("Arial", Font.PLAIN, 13));
        g2.drawString("[B] / ESC = schließen", panelX + 20, panelY + panelH - 10);
    }

}
