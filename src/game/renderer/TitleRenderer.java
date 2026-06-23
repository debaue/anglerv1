package game.renderer;

import game.Game;
import game.GamePanel;

import java.awt.*;

public class TitleRenderer {

    private final Game game;

    public TitleRenderer(Game game) {
        this.game = game;
    }

    public void draw(Graphics2D g2) {
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);

        g2.setFont(new Font("Monospaced", Font.BOLD, 48));
        g2.setColor(Color.WHITE);
        String title = "ANGLER";
        int tw = g2.getFontMetrics().stringWidth(title);
        g2.drawString(title, GamePanel.WIDTH / 2 - tw / 2, 120);

        g2.setFont(new Font("Monospaced", Font.PLAIN, 16));
        g2.setColor(Color.GRAY);
        String sub = "Ein Angelspiel";
        g2.drawString(sub, GamePanel.WIDTH / 2 - g2.getFontMetrics().stringWidth(sub) / 2, 150);

        g2.setFont(new Font("Monospaced", Font.PLAIN, 16));
        g2.setColor(Color.LIGHT_GRAY);
        g2.drawString("Name:", GamePanel.WIDTH / 2 - 120, 230);

        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(GamePanel.WIDTH / 2 - 120, 240, 240, 30);
        g2.setColor(Color.WHITE);
        g2.drawRect(GamePanel.WIDTH / 2 - 120, 240, 240, 30);
        g2.setFont(new Font("Monospaced", Font.PLAIN, 16));
        g2.drawString(game.getNameInput() + "_", GamePanel.WIDTH / 2 - 110, 261);

        boolean nameOk         = !game.getNameInput().trim().isEmpty();
        boolean saveExists     = game.getSaveExists();
        boolean checking       = game.isCheckingName();
        boolean serverReachable = game.isServerReachable();

        g2.setColor(nameOk ? new Color(60, 160, 60) : Color.DARK_GRAY);
        g2.fillRect(GamePanel.WIDTH / 2 - 120, 300, 240, 36);
        g2.setColor(Color.WHITE);
        g2.drawRect(GamePanel.WIDTH / 2 - 120, 300, 240, 36);
        g2.setFont(new Font("Monospaced", Font.BOLD, 16));
        String ng = "Neues Spiel";
        g2.setColor(nameOk ? Color.WHITE : Color.GRAY);
        g2.drawString(ng, GamePanel.WIDTH / 2 - g2.getFontMetrics().stringWidth(ng) / 2, 323);

        g2.setColor(saveExists ? new Color(30, 100, 160) : Color.DARK_GRAY);
        g2.fillRect(GamePanel.WIDTH / 2 - 120, 356, 240, 36);
        g2.setColor(saveExists ? new Color(100, 180, 255) : Color.GRAY);
        g2.drawRect(GamePanel.WIDTH / 2 - 120, 356, 240, 36);
        g2.setFont(new Font("Monospaced", Font.BOLD, 16));
        String cont = "Weiterspielen";
        g2.setColor(saveExists ? Color.WHITE : Color.GRAY);
        g2.drawString(cont, GamePanel.WIDTH / 2 - g2.getFontMetrics().stringWidth(cont) / 2, 379);

        g2.setFont(new Font("Monospaced", Font.ITALIC, 12));
        String saveInfo;
        if (!serverReachable && nameOk && !checking) {
            g2.setColor(new Color(220, 80, 80));
            saveInfo = "Kein Internet - Spielstand nicht abrufbar";
        } else if (checking) {
            g2.setColor(Color.GRAY);
            saveInfo = "wird gesucht...";
        } else if (saveExists) {
            g2.setColor(new Color(100, 200, 100));
            saveInfo = "Spielstand gefunden!";
        } else {
            g2.setColor(Color.GRAY);
            saveInfo = "(kein Spielstand)";
        }
        g2.drawString(saveInfo, GamePanel.WIDTH / 2 - g2.getFontMetrics().stringWidth(saveInfo) / 2, 410);
    }
}
