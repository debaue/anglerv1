package game.renderer;

import data.ShopItem;
import entities.Fish;
import entities.Player;
import game.Game;
import game.GamePanel;
import game.ShopSystem;

import java.awt.*;
import java.util.List;

public class ShopRenderer {

    private final Game game;

    public ShopRenderer(Game game) {
        this.game = game;
    }

    public void draw(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);

        int panelX = 40;
        int panelY = 60;
        int panelW = GamePanel.WIDTH - 80;
        int panelH = GamePanel.HEIGHT - 120;

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

        g2.setFont(new Font("Arial", Font.PLAIN, 12));
        g2.setColor(new Color(160, 160, 160));
        g2.drawString("W/S = wählen   SPACE = kaufen   ESC = schließen", panelX + 20, panelY + 78);

        int leftX   = panelX + 20;
        int itemY   = panelY + 100;
        int itemW   = (panelW / 2) - 40;
        int rowH    = 40;
        int listMaxH = panelH - 100 - 50;

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.drawString("Kaufen", leftX, itemY - 8);

        ShopSystem shop = game.getShopSystem();
        Player p = game.getPlayer();
        List<ShopItem> items = shop.getVisibleItems(p);
        shop.rebuildItemRects(leftX, itemY, itemW, rowH, items.size());

        Shape oldClip = g2.getClip();
        g2.setClip(panelX, itemY, panelW, listMaxH);
        for (int i = 0; i < items.size(); i++) {
            ShopItem item = items.get(i);
            int iy = itemY + i * rowH;

            boolean selected    = (i == shop.getSelectedIndex());
            boolean isBaitItem  = item.getType() == ShopItem.Type.BAIT;
            boolean equippedBait = isBaitItem && p.getEquippedBait() != null
                    && p.getEquippedBait().name.equals(item.getName())
                    && p.getBaitCount() > 0;
            String badge = equippedBait ? " x" + p.getBaitCount() : "";

            g2.setColor(selected ? new Color(80, 140, 60) : new Color(55, 70, 55));
            g2.fillRoundRect(leftX, iy, itemW, rowH - 4, 10, 10);

            g2.setColor(selected ? Color.WHITE : new Color(200, 200, 200));
            g2.setFont(new Font("Arial", Font.BOLD, 13));
            g2.drawString(item.getName() + badge, leftX + 10, iy + 16);
            g2.setFont(new Font("Arial", Font.PLAIN, 12));
            g2.setColor(new Color(255, 220, 80));
            g2.drawString(item.getPrice() + "G" + (isBaitItem ? "  (5x)" : ""), leftX + 10, iy + 30);
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
        g2.drawString("Inventar", rightX, itemY - 8);

        List<Fish> inv = game.getPlayer().getInventory();
        g2.setFont(new Font("Arial", Font.PLAIN, 13));
        for (int i = 0; i < inv.size(); i++) {
            Fish fish = inv.get(i);
            int iy = itemY + i * 22;
            g2.setColor(new Color(200, 220, 200));
            g2.drawString(fish.type.name + " – " + fish.weightKg + "kg = " + fish.price + "G", rightX, iy + 14);
        }
        if (inv.isEmpty()) {
            g2.setColor(new Color(150, 150, 150));
            g2.drawString("Keine Fische", rightX, itemY + 14);
        }

        int sellBtnY = panelY + panelH - 44;
        shop.setSellAllButton(new Rectangle(rightX, sellBtnY, rightW, 32));
        g2.setColor(new Color(160, 80, 40));
        g2.fillRoundRect(rightX, sellBtnY, rightW, 32, 8, 8);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.drawString("Alle verkaufen", rightX + 10, sellBtnY + 21);
    }
}
