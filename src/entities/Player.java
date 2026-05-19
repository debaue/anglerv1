package entities;

import util.AnimationController;
import util.InputHandler;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Player {

    private int gold;
    private Rod equippedRod;
    private List<Fish> inventory = new ArrayList<>();
    private int maxSlots;
    private int x = 100;
    private int y = 100;
    private final int SPEED = 3;
    private AnimationController animator;
    private int direction = 0;

    public Player(Rod startRod) {
        this.gold = 0;
        this.equippedRod = startRod;
        this.maxSlots = 10;
        this.animator = new AnimationController();

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
    public void update(float delta, InputHandler input) {
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


        this.x += dx * SPEED;
        this.y += dy* SPEED;

        AnimationController.AnimState state = isWalking ? AnimationController.AnimState.WALK : AnimationController.AnimState.IDLE;
        animator.update(delta,state,direction);

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
}

