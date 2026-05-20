package entities;

import java.awt.*;

public class HitBox {
    public int x, y;
    public final int width, height;

    public HitBox( int width, int height) {
        this.width   = width;
        this.height  = height;
    }

    public void update(int entityX, int entityY) {
        this.x = entityX;
        this.y = entityY;
    }

    public void draw(Graphics2D g2, int cameraX, int cameraY) {
        g2.setColor(new Color(255, 0, 0, 120));
        g2.drawRect(x - cameraX, y - cameraY, width, height);
    }
}
