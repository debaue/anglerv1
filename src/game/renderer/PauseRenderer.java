package game.renderer;

import game.Game;
import game.GamePanel;
import util.MusicPlayer;

import java.awt.*;

public class PauseRenderer {

    private final Game game;

    public PauseRenderer(Game game) {
        this.game = game;
    }

    public void draw(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0, 160));
        g2.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);

        int panelW = 300, panelH = 240;
        int panelX = GamePanel.WIDTH / 2 - panelW / 2;
        int panelY = GamePanel.HEIGHT / 2 - panelH / 2;

        g2.setColor(new Color(15, 25, 40));
        g2.fillRoundRect(panelX, panelY, panelW, panelH, 16, 16);
        g2.setColor(new Color(60, 100, 140));
        g2.drawRoundRect(panelX, panelY, panelW, panelH, 16, 16);

        g2.setFont(new Font("Arial", Font.BOLD, 26));
        g2.setColor(new Color(200, 230, 255));
        String title = "PAUSE";
        g2.drawString(title, panelX + (panelW - g2.getFontMetrics().stringWidth(title)) / 2, panelY + 48);

        drawButton(g2, panelX + 30, panelY + 80, panelW - 60, 44, "Fortsetzen", new Color(40, 100, 60));
        drawButton(g2, panelX + 30, panelY + 140, panelW - 60, 44, "Speichern & Beenden", new Color(120, 40, 40));

        boolean musikAn = MusicPlayer.isRunning();
        int cbX = panelX + 30, cbY = panelY + 195, cbW = panelW - 60, cbH = 30;
        g2.setFont(new Font("Arial", Font.BOLD, 13));
        g2.setColor(musikAn ? new Color(120, 200, 120) : new Color(120, 120, 130));
        String musikLabel = musikAn ? "  Musik: AN" : "  Musik: AUS";
        g2.drawString(musikLabel, cbX + (cbW - g2.getFontMetrics().stringWidth(musikLabel)) / 2, cbY + 20);

        g2.setFont(new Font("Arial", Font.PLAIN, 12));
        g2.setColor(new Color(80, 100, 120));
        String hint = "ESC = Fortsetzen";
        g2.drawString(hint, panelX + (panelW - g2.getFontMetrics().stringWidth(hint)) / 2, panelY + panelH - 8);

        if (!game.isServerReachable()) {
            g2.setFont(new Font("Arial", Font.BOLD, 11));
            g2.setColor(new Color(220, 80, 80));
            String warn = "Kein Internet - nicht gespeichert!";
            g2.drawString(warn, GamePanel.WIDTH / 2 - g2.getFontMetrics().stringWidth(warn) / 2, panelY + panelH + 20);
        }
    }

    private void drawButton(Graphics2D g2, int x, int y, int w, int h, String label, Color color) {
        g2.setColor(color);
        g2.fillRoundRect(x, y, w, h, 10, 10);
        g2.setColor(color.brighter());
        g2.drawRoundRect(x, y, w, h, 10, 10);
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.setColor(Color.WHITE);
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(label, x + (w - fm.stringWidth(label)) / 2, y + h / 2 + fm.getAscent() / 2 - 2);
    }
}
