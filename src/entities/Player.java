package entities;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private int gold;
    private Rod equippedRod;
    private List<Fish> inventory = new ArrayList<>();
    private int maxSlots;
    private int x;
    private int y;

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

    public int sellAll() {
        int total = 0;
        for(Fish f : inventory) {
            total += f.price;
        }
        inventory.clear();
        return total;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}

