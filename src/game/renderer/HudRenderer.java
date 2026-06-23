package game.renderer;

import game.Game;
import game.GamePanel;
import entities.Player;

import java.awt.*;

public class HudRenderer {

    private final Game game;

    public HudRenderer(Game game) {
        this.game = game;
    }

    public void draw(Graphics2D g2) {
        Player p = game.getPlayer();
        int pad = 10;

        g2.setColor(new Color(0, 0, 0, 160));
        g2.fillRoundRect(pad, pad, 200, 48, 12, 12);
        g2.setColor(new Color(255, 210, 50));
        g2.setFont(new Font("Arial", Font.BOLD, 18));
        g2.drawString("Gold: " + p.getGold() + "G", pad + 12, pad + 28);
        g2.setColor(new Color(180, 220, 255));
        g2.setFont(new Font("Arial", Font.PLAIN, 13));
        g2.drawString("Fische: " + p.getInventory().size() + "/" + p.getMaxSlots(), pad + 12, pad + 44);

        String rodName      = p.getEquippedRod()  != null ? p.getEquippedRod().name  : "Keine Rute";
        String baitName     = p.getEquippedBait() != null ? p.getEquippedBait().name : "Kein Köder";
        String baitCountStr = p.getBaitCount() < 0 ? "∞" : "x" + p.getBaitCount();
        boolean hasUpgradedBait = p.getBaitCount() > 0;

        int rodPanelW = 210, rodPanelH = 68;
        int rodPanelX = GamePanel.WIDTH - rodPanelW - pad;
        g2.setColor(new Color(0, 0, 0, 160));
        g2.fillRoundRect(rodPanelX, pad, rodPanelW, rodPanelH, 12, 12);

        g2.setColor(new Color(180, 140, 80));
        g2.setFont(new Font("Arial", Font.BOLD, 12));
        g2.drawString("Rute:", rodPanelX + 10, pad + 18);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.PLAIN, 13));
        g2.drawString(rodName, rodPanelX + 55, pad + 18);

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

        if (game.getState() == Game.GameState.EXPLORING) {
            String hint = null;
            if (p.isNearWater())          hint = "[F] Angeln";
            if (game.isNearShopKeeper())  hint = "[E] Shop öffnen";
            if (hint != null) {
                g2.setFont(new Font("Arial", Font.BOLD, 16));
                int hw = g2.getFontMetrics().stringWidth(hint) + 24;
                int hx = (GamePanel.WIDTH - hw) / 2;
                int hy = GamePanel.HEIGHT - 40;
                g2.setColor(new Color(0, 0, 0, 180));
                g2.fillRoundRect(hx, hy, hw, 28, 10, 10);
                g2.setColor(Color.WHITE);
                g2.drawString(hint, hx + 12, hy + 20);
            }
        }

        String lockedMsg = game.getLockedZoneMessage();
        if (lockedMsg != null) {
            g2.setFont(new Font("Arial", Font.BOLD, 15));
            int lw = g2.getFontMetrics().stringWidth(lockedMsg) + 28;
            int lx = (GamePanel.WIDTH - lw) / 2;
            int ly = GamePanel.HEIGHT - 80;
            g2.setColor(new Color(160, 30, 30, 210));
            g2.fillRoundRect(lx, ly, lw, 30, 10, 10);
            g2.setColor(Color.WHITE);
            g2.drawString(lockedMsg, lx + 14, ly + 21);
        }

        g2.setColor(new Color(0, 0, 0, 130));
        g2.fillRoundRect(GamePanel.WIDTH - 90, GamePanel.HEIGHT - 36, 80, 26, 8, 8);
        g2.setColor(new Color(200, 200, 200));
        g2.setFont(new Font("Arial", Font.PLAIN, 12));
        g2.drawString("[I] Inventar", GamePanel.WIDTH - 83, GamePanel.HEIGHT - 18);

        g2.setColor(new Color(0, 0, 0, 130));
        g2.fillRoundRect(GamePanel.WIDTH - 90, GamePanel.HEIGHT - 68, 80, 26, 8, 8);
        g2.setColor(new Color(180, 220, 180));
        g2.setFont(new Font("Arial", Font.PLAIN, 12));
        g2.drawString("[B] Fischbuch", GamePanel.WIDTH - 88, GamePanel.HEIGHT - 50);
    }
}
