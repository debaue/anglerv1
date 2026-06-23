package game.renderer;

import game.Game;
import game.GamePanel;
import entities.ShopKeeper;
import util.SpriteLoader;
import world.TileType;

import java.awt.*;
import java.awt.image.BufferedImage;

public class WorldRenderer {

    private final Game game;

    public WorldRenderer(Game game) {
        this.game = game;
    }

    public void draw(Graphics2D g2) {
        drawTileLayer(g2, TileType.WATER, SpriteLoader.getWaterTiles());
        drawTileLayer(g2, TileType.GRASS, SpriteLoader.getGrassTiles());
        drawTileLayer(g2, TileType.GROUND, SpriteLoader.getGroundtiles());
        drawShopKeeper(g2);
        drawPlayer(g2);
    }

    private void drawTileLayer(Graphics2D g2, TileType type, BufferedImage[] tiles) {
        for (int r = 0; r <= game.getTileMap().getRows(); r++) {
            for (int c = 0; c <= game.getTileMap().getCols(); c++) {
                int screenX = c * 32 - game.getCamera().getX();
                int screenY = r * 32 - game.getCamera().getY();
                if (screenX + 32 < 0 || screenX > GamePanel.WIDTH)  continue;
                if (screenY + 32 < 0 || screenY > GamePanel.HEIGHT) continue;
                int mask = game.getTileMap().getDualMask(c, r, type);
                if (mask == 0) continue;
                int index = type.lookup[mask];
                g2.drawImage(tiles[index], screenX, screenY, null);
            }
        }
    }

    private void drawShopKeeper(Graphics2D g2) {
        ShopKeeper sk = game.getShopKeeper();
        int sx = sk.getX() - game.getCamera().getX();
        int sy = sk.getY() - game.getCamera().getY();
        g2.drawImage(SpriteLoader.getShopKeeper(), sx, sy, ShopKeeper.SPRITE_W, ShopKeeper.SPRITE_H, null);
    }

    private void drawPlayer(Graphics2D g2) {
        g2.drawImage(
                game.getPlayer().getCurrentFrame(),
                game.getPlayer().getX() - game.getCamera().getX(),
                game.getPlayer().getY() - game.getCamera().getY(),
                null
        );
    }
}
