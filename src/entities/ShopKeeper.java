package entities;

public class ShopKeeper {

    public static final int SPRITE_W = 48;
    public static final int SPRITE_H = 64;

    // HitBox ist kleiner als der Sprite und sitzt am unteren Drittel (Füße)
    private static final int HB_W = 32;
    private static final int HB_H = 24;

    private final int x;
    private final int y;
    private final HitBox hitbox;

    public ShopKeeper(int x, int y) {
        this.x = x;
        this.y = y;

        int hbX = x + (SPRITE_W - HB_W) / 2;
        int hbY = y + SPRITE_H - HB_H;
        this.hitbox = new HitBox(HB_W, HB_H);
        this.hitbox.update(hbX, hbY);
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public HitBox getHitBox() { return hitbox; }
}
