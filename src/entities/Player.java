package entities;

import util.InputHandler;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private int gold;
    private Rod equippedRod;
    private List<Fish> inventory = new ArrayList<>();
    private int maxSlots;
    private int x;
    private int y;
    private final int SPEED = 3;

    public Player(Rod startRod) {
        this.gold = 0;
        this.equippedRod = startRod;
        this.maxSlots = 10;
    }

    public boolean addFish(Fish fish) {
        if(inventory.size() >= maxSlots) {
            return false;
        }
        inventory.add(fish);
        return true;
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
    public void update(InputHandler input) {
        float dx = 0, dy = 0;

        if (input.up ) dy -= 1;
        if(input.down) dy += 1;
        if(input.left) dx -= 1;
        if(input.right) dx += 1;

        this.x += dx * SPEED;
        this.y += dy* SPEED;

    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}

