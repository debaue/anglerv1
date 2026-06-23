package entities;

import data.BaitRegistry;
import util.AnimationController;
import util.InputHandler;
import world.FishingZone;
import world.TileMap;
import world.TileType;
import world.ZoneRegistry;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Player {

    private String name = "Angler";
    private int gold;
    private Rod equippedRod;
    private Bait equippedBait;
    private int baitCount;
    private List<Fish> inventory = new ArrayList<>();
    private int maxSlots;
    private final Map<String, FishBookEntry> fishBook = new LinkedHashMap<>();
    private final Set<String> unlockedZones = new HashSet<>();

    private final int SPEED = 3;
    private AnimationController animator;
    private int direction = 0;

    private int x = 100;
    private int y = 100;

    private final HitBox hitBox;
    private final HitBox fishingHitBox;

    private final InputHandler input;
    private final TileMap map;

    public Player(Rod startRod, InputHandler input, TileMap map) {
        this.gold = 0;
        this.equippedRod = startRod;
        this.equippedBait = BaitRegistry.getStarter();
        this.baitCount = -1;
        this.unlockedZones.add(ZoneRegistry.STARTTEICH.name);
        this.maxSlots = 10;
        this.animator = new AnimationController();

        this.hitBox = new HitBox(32, 32);
        this.fishingHitBox = new HitBox(64, 64);

        this.input = input;
        this.map = map;

        updateHitBoxes();
    }

    private void updateHitBoxes() {
        hitBox.update(x, y);
        fishingHitBox.update(x - 16, y - 16);
    }

    public void update(float delta) {
        float dx = 0;
        float dy = 0;

        if (input.up) dy -= 1;
        if (input.down) dy += 1;
        if (input.left) dx -= 1;
        if (input.right) dx += 1;

        boolean isWalking = dx != 0 || dy != 0;

        if (dy < 0) direction = 2;
        if (dy > 0) direction = 3;
        if (dx < 0) direction = 0;
        if (dx > 0) direction = 1;

        dx *= SPEED;
        dy *= SPEED;

        hitBox.update((int) (x + dx), y);
        if (!isBlockedByMap(hitBox)) {
            x += (int) dx;
        }

        hitBox.update(x, (int) (y + dy));
        if (!isBlockedByMap(hitBox)) {
            y += (int) dy;
        }

        x = Math.max(0, Math.min(x, map.getCols() * TileMap.TILE_SIZE - hitBox.width));
        y = Math.max(0, Math.min(y, map.getRows() * TileMap.TILE_SIZE - hitBox.height));

        updateHitBoxes();

        AnimationController.AnimState state =
                isWalking ? AnimationController.AnimState.WALK : AnimationController.AnimState.IDLE;
        animator.update(delta, state, direction);
    }

    private boolean isBlockedByMap(HitBox box) {
        int tx1 = box.x / TileMap.TILE_SIZE;
        int ty1 = box.y / TileMap.TILE_SIZE;
        int tx2 = (box.x + box.width - 1) / TileMap.TILE_SIZE;
        int ty2 = (box.y + box.height - 1) / TileMap.TILE_SIZE;

        return isBlocked(tx1, ty1) || isBlocked(tx2, ty1)
                || isBlocked(tx1, ty2) || isBlocked(tx2, ty2);
    }

    private boolean isBlocked(int tileX, int tileY) {
        TileType t = map.getTile(tileX, tileY);
        return t == null || t.blocked;
    }

    public boolean isNearWater() {
        int tx1 = fishingHitBox.x / TileMap.TILE_SIZE;
        int ty1 = fishingHitBox.y / TileMap.TILE_SIZE;
        int tx2 = (fishingHitBox.x + fishingHitBox.width - 1) / TileMap.TILE_SIZE;
        int ty2 = (fishingHitBox.y + fishingHitBox.height - 1) / TileMap.TILE_SIZE;

        for (int tx = tx1; tx <= tx2; tx++) {
            for (int ty = ty1; ty <= ty2; ty++) {
                if (map.getTile(tx, ty) == TileType.WATER) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean addFish(Fish fish) {
        fishBook.computeIfAbsent(fish.type.name, FishBookEntry::new).registerCatch(fish);
        if (inventory.size() >= maxSlots) {
            return false;
        }
        inventory.add(fish);
        return true;
    }

    public int sellAllFish() {
        int total = 0;
        for (Fish fish : inventory) {
            total += fish.price;
        }
        inventory.clear();
        gold += total;
        return total;
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

    public Rod getEquippedRod() {
        return equippedRod;
    }

    public void setEquippedRod(Rod rod) {
        this.equippedRod = rod;
    }

    public Bait getEquippedBait() {
        return equippedBait;
    }

    public void setEquippedBait(Bait bait) {
        this.equippedBait = bait;
    }

    public void addBait(Bait bait, int count) {
        this.equippedBait = bait;
        this.baitCount = count;
    }

    public void stackBait(Bait bait, int count) {
        if (equippedBait != null && equippedBait.name.equals(bait.name) && baitCount > 0) {
            baitCount += count;
        } else {
            equippedBait = bait;
            baitCount = count;
        }
    }

    public void addSlots(int n) { maxSlots += n; }
    public void setMaxSlots(int n) { maxSlots = n; }

    public int getBaitCount() {
        return baitCount;
    }

    public void consumeBait() {
        if (baitCount < 0) return;
        baitCount--;
        if (baitCount <= 0) {
            equippedBait = BaitRegistry.getStarter();
            baitCount = -1;
        }
    }

    public BufferedImage getCurrentFrame() {
        return animator.getCurrentFrame();
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public HitBox getHitBox() {
        return hitBox;
    }

    public Map<String, FishBookEntry> getFishBook() {
        return fishBook;
    }

    public boolean isZoneUnlocked(FishingZone zone) {
        return unlockedZones.contains(zone.name);
    }

    public void unlockZone(FishingZone zone) {
        unlockedZones.add(zone.name);
    }

    public FishingZone getActiveZone() {
        if (isZoneUnlocked(ZoneRegistry.SUEDSEE)) return ZoneRegistry.SUEDSEE;
        if (isZoneUnlocked(ZoneRegistry.OSTSEE))  return ZoneRegistry.OSTSEE;
        return ZoneRegistry.STARTTEICH;
    }
}