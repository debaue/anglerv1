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
        entities.Player p = game.getPlayer();
        int pad = 10;
        int panelH = 48;

        // === OBEN LINKS: Gold + Fische ===
        g2.setColor(new Color(0, 0, 0, 160));
        g2.fillRoundRect(pad, pad, 200, panelH, 12, 12);

        // Gold
        g2.setColor(new Color(255, 210, 50));
        g2.setFont(new Font("Arial", Font.BOLD, 18));
        g2.drawString("Gold: " + p.getGold() + "G", pad + 12, pad + 28);

        // Fisch-Slots
        g2.setColor(new Color(180, 220, 255));
        g2.setFont(new Font("Arial", Font.PLAIN, 13));
        g2.drawString("Fische: " + p.getInventory().size() + "/" + p.getMaxSlots(),
                pad + 12, pad + 44);

        // === OBEN RECHTS: Ausgerüstete Rute ===
        String rodName = p.getEquippedRod() != null ? p.getEquippedRod().name : "Keine Rute";
        int rodPanelW = 180;
        int rodPanelX = WIDTH - rodPanelW - pad;

        g2.setColor(new Color(0, 0, 0, 160));
        g2.fillRoundRect(rodPanelX, pad, rodPanelW, panelH, 12, 12);

        g2.setColor(new Color(180, 140, 80));
        g2.setFont(new Font("Arial", Font.BOLD, 13));
        g2.drawString("Rute", rodPanelX + 12, pad + 20);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.PLAIN, 14));
        g2.drawString(rodName, rodPanelX + 12, pad + 40);

        String hint = null;
        if (game.getState() == Game.GameState.EXPLORING) {
            if (p.isNearWater())               hint = "[F] Angeln";
            if (isNearShopKeeperPublic())      hint = "[E] Shop öffnen";
        }
        if (hint != null) {
            FontMetrics fm = g2.getFontMetrics();
            g2.setFont(new Font("Arial", Font.BOLD, 16));
            int hw = g2.getFontMetrics().stringWidth(hint) + 24;
            int hx = (WIDTH - hw) / 2;
            int hy = HEIGHT - 40;
            g2.setColor(new Color(0, 0, 0, 180));
            g2.fillRoundRect(hx, hy, hw, 28, 10, 10);
            g2.setColor(Color.WHITE);
            g2.drawString(hint, hx + 12, hy + 20);
        }
        g2.setColor(new Color(0, 0, 0, 130));
        g2.fillRoundRect(WIDTH - 90, HEIGHT - 36, 80, 26, 8, 8);
        g2.setColor(new Color(200, 200, 200));
        g2.setFont(new Font("Arial", Font.PLAIN, 12));
        g2.drawString("[I] Inventar", WIDTH - 83, HEIGHT - 18);
    }

    private boolean isNearShopKeeperPublic() {
        entities.HitBox pk  = game.getShopKeeper().getHitBox();
        entities.HitBox plr = game.getPlayer().getHitBox();
        int range = 48;
        return plr.x < pk.x + pk.width  + range &&
               plr.x + plr.width  > pk.x - range &&
               plr.y < pk.y + pk.height + range &&
               plr.y + plr.height > pk.y - range;
    }

    private void drawShopKeeper(Graphics2D g2) {
        entities.ShopKeeper sk = game.getShopKeeper();
        int sx = sk.getX() - game.getCamera().getX();
        int sy = sk.getY() - game.getCamera().getY();
        int sw = entities.ShopKeeper.SPRITE_W;
        int sh = entities.ShopKeeper.SPRITE_H;

        java.awt.image.BufferedImage sprite = util.SpriteLoader.getShopKeeper();
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
        int rowH   = 48;

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.drawString("Kaufen (SPACE / Klick)", leftX, itemY - 10);

        ShopSystem shop = game.getShopSystem();
        shop.rebuildItemRects(leftX, itemY, itemW, rowH);

        java.util.List<data.ShopItem> items = shop.getItems();
        for (int i = 0; i < items.size(); i++) {
            data.ShopItem item = items.get(i);
            int iy = itemY + i * rowH;

            boolean selected = (i == shop.getSelectedIndex());
            boolean equipped = game.getPlayer().getEquippedRod() != null &&
                    game.getPlayer().getEquippedRod().name.equals(item.getName());

            g2.setColor(selected ? new Color(80, 140, 60) : new Color(55, 70, 55));
            g2.fillRoundRect(leftX, iy, itemW, rowH - 6, 10, 10);

            g2.setColor(selected ? Color.WHITE : new Color(200, 200, 200));
            g2.setFont(new Font("Arial", Font.BOLD, 15));
            g2.drawString(item.getName() + (equipped ? " ✓" : ""), leftX + 10, iy + 20);
            g2.setFont(new Font("Arial", Font.PLAIN, 13));
            g2.setColor(new Color(255, 220, 80));
            g2.drawString(item.getPrice() + "G", leftX + 10, iy + 36);
        }
        int btnX = leftX;
        int btnY = itemY + items.size() * rowH + 10;
        shop.setBuyButton(new java.awt.Rectangle(btnX, btnY, itemW, 32));
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

        java.util.List<entities.Fish> inv = game.getPlayer().getInventory();
        g2.setFont(new Font("Arial", Font.PLAIN, 13));
        for (int i = 0; i < inv.size(); i++) {
            entities.Fish fish = inv.get(i);
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
        shop.setSellAllButton(new java.awt.Rectangle(rightX, sellBtnY, rightW, 32));
        g2.setColor(new Color(160, 80, 40));
        g2.fillRoundRect(rightX, sellBtnY, rightW, 32, 8, 8);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.drawString("Alle verkaufen", rightX + 10, sellBtnY + 21);

        g2.setColor(new Color(160, 160, 160));
        g2.setFont(new Font("Arial", Font.PLAIN, 13));
        g2.drawString("W/S = wählen   SPACE = kaufen   ESC = schließen",
                panelX + 20, panelY + panelH - 10);
    }
}
