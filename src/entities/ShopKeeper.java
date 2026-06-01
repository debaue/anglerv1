package entities;

public class ShopKeeper {

    private int x;
    private int y;
    private HitBox hitbox;

    public ShopKeeper(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.hitbox = new HitBox(width, height);
        this.hitbox.update(x, y);
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public HitBox getHitBox() { return hitbox; }
}
