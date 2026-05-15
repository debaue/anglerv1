package entities;

import java.util.ArrayList;
import java.util.List;

public class Player {

    public int gold;
    public Rod equippedRod;
    public List<Fish> inventory = new ArrayList<>();
    public int maxSlots;

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

    public int sellAll() {
        int total = 0;
        for(Fish f : inventory) {
            total += f.price;
        }
        inventory.clear();
        return total;
    }



}

