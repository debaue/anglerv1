package entities;

import util.AnimationController;
import util.InputHandler;
import world.TileMap;
import world.TileType;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Player {

    private int gold;
    private Rod equippedRod;
    private List<Fish> inventory = new ArrayList<>();
    private int maxSlots;
    private final int SPEED = 3;
    private AnimationController animator;
    private int direction = 0;
    private int x = 100;
    private int y = 100;
    private HitBox hitBox;
    private HitBox fishingHitBox;
    private InputHandler input;
    private TileMap map;



    public Player(Rod startRod,InputHandler input, TileMap map) {
        this.gold = 0;
        this.equippedRod = startRod;
        this.maxSlots = 10;
        this.animator = new AnimationController();
        this.hitBox = new HitBox(32,32);
        this.fishingHitBox = new HitBox(64,64);
        this.input = input;
        this.map = map;

    }

    public boolean addFish(Fish fish) {
        if(inventory.size() >= maxSlots) {
            return false;
        }
        inventory.add(fish);
        return true;
    }

    public List<Fish> getInventory() {
        return inventory;
    }

    public int getMaxSlots() {
        return maxSlots;
    }

    public int getGold() {
        return gold;
    }

    public void addGold(int gold) {
        this.gold += gold;
    }

//    public int sellAll() {
//        int total = 0;
//        for(Fish f : inventory) {
//            total += f.price;
//        }
//        inventory.clear();
//        return total;
//    }
//
        public void update(float delta ) {
        float dx = 0, dy = 0;


        if (input.up ) dy -= 1;
        if(input.down) dy += 1;
        if(input.left) dx -= 1;
        if(input.right) dx += 1;
        boolean isWalking = dx != 0 || dy != 0;

        if(dy < 0) direction = 2;
        if(dy > 0) direction = 3;
        if(dx < 0) direction = 0;
        if(dx > 0) direction = 1;
        dx *= SPEED;
        dy *= SPEED;

        hitBox.update((int)(x + dx), y);
        int tx1 = hitBox.x / TileMap.TILE_SIZE;
        int ty1 = hitBox.y / TileMap.TILE_SIZE;
        int tx2 = (hitBox.x + hitBox.width  - 1) / TileMap.TILE_SIZE;
        int ty2 = (hitBox.y + hitBox.height - 1) / TileMap.TILE_SIZE;

        fishingHitBox.update(x-16, y-16);

        if(!isBlocked(map,tx1,ty1) && !isBlocked(map,tx2,ty1) &&
                !isBlocked(map,tx1,ty2) && !isBlocked(map,tx2,ty2)) {
            x += (int)dx;
        }

        hitBox.update(x, (int)(y + dy));
        tx1 = hitBox.x / TileMap.TILE_SIZE;
        ty1 = hitBox.y / TileMap.TILE_SIZE;
        tx2 = (hitBox.x + hitBox.width  - 1) / TileMap.TILE_SIZE;
        ty2 = (hitBox.y + hitBox.height - 1) / TileMap.TILE_SIZE;

        if(!isBlocked(map,tx1,ty1) && !isBlocked(map,tx2,ty1) &&
                !isBlocked(map,tx1,ty2) && !isBlocked(map,tx2,ty2)) {
            y += (int)dy;
        }

        hitBox.update(x, y);



        AnimationController.AnimState state = isWalking ? AnimationController.AnimState.WALK : AnimationController.AnimState.IDLE;
        animator.update(delta,state,direction);

    }

    private boolean isBlocked(TileMap map, int tileX, int tileY) {
        TileType t = map.getTile(tileX, tileY);
        return t == null || t.blocked;
    }
    public boolean isNearWater() {
        int tx1 = fishingHitBox.x / TileMap.TILE_SIZE;
        int ty1 = fishingHitBox.y / TileMap.TILE_SIZE;
        int tx2 = (fishingHitBox.x + fishingHitBox.width  - 1) / TileMap.TILE_SIZE;
        int ty2 = (fishingHitBox.y + fishingHitBox.height - 1) / TileMap.TILE_SIZE;

        for(int tx = tx1; tx <= tx2; tx++)
            for(int ty = ty1; ty <= ty2; ty++)
                if(map.getTile(tx, ty) == TileType.WATER) return true;
        return false;
    }

    public BufferedImage getCurrentFrame() {
        return animator.getCurrentFrame();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public HitBox getHitBox() {
        return hitBox;
    }
}

