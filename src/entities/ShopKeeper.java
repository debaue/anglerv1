package entities;

public class ShopKeeper {

    private int x;
    private int y;
    private HitBox hitbox;

    public ShopKeeper() {
        this.x = 400;
        this.y = 200;
        this.hitbox = new HitBox(32,32);
    }
}
