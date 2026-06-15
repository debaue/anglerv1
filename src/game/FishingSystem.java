package game;

import data.FishRegistry;
import data.FishType;
import entities.Fish;
import entities.Player;
import util.InputHandler;
import world.FishingZone;
import world.TileMap;
import world.ZoneRegistry;

import java.time.LocalDateTime;
import java.util.Random;

public class FishingSystem {

    private final InputHandler input;
    private final Random random = new Random();

    public enum FishState {
        CASTING,
        WAITING,
        BITING,
        MINIGAME,
        RESULT
    }
    private FishState state = FishState.CASTING;
    private int bobberX, bobberY;

    private float waitTimer;
    private float waitTarget;

    private float biteTimer;
    private static final float BITE_WINDOW = 1.5f;

    private float markerX;
    private float markerSpeed;
    private int markerDir = 1;
    private float greenStart;
    private float greenWidth;
    private int hits;
    private int misses;
    private static final int MAX_HITS = 3;
    private static final int MAX_MISSES = 2;
    private static final int BAR_WIDTH = 400;

    private FishType    caughtType;
    private Fish        lastCaughtFish;
    private FishingZone currentZone = ZoneRegistry.STARTTEICH;
    private boolean     success;
    private float       resultTimer;

    public FishingSystem(InputHandler input) {
        this.input = input;
    }

    public void update(float delta, Player player) {
        switch(state) {
            case CASTING -> {
                if(input.mouseClicked) {
                    int mx = input.mouseX;
                    int my = input.mouseY;
                    int waterLeft   = 40;
                    int waterTop    = 20;
                    int waterRight  = GamePanel.WIDTH  - 40;
                    int waterBottom = GamePanel.HEIGHT - 190;

                    if(mx >= waterLeft && mx <= waterRight &&
                            my >= waterTop  && my <= waterBottom) {
                        bobberX = mx;
                        bobberY = my;
                        waitTimer  = 0f;
                        waitTarget = 2f + random.nextFloat() * 3f;
                        state = FishState.WAITING;
                    }
                    input.mouseClicked = false;
                }
            }


            case WAITING -> {
                waitTimer += delta;
                if(waitTimer >= waitTarget) {
                    biteTimer = 0f;
                    state = FishState.BITING;
                }
            }
            case BITING -> {
                biteTimer += delta;
                if(input.spacePressed) {
                    input.spacePressed = false;
                    player.consumeBait();
                    startMinigame(player);
                } else if(biteTimer >= BITE_WINDOW) {
                    waitTimer  = 0f;
                    waitTarget = 2f + random.nextFloat() * 3f;
                    state = FishState.WAITING;
                }
            }

            case MINIGAME -> {
                markerX += markerSpeed * markerDir * delta;
                if(markerX >= BAR_WIDTH) { markerX = BAR_WIDTH; markerDir = -1; }
                if(markerX <= 0)         { markerX = 0;         markerDir =  1; }

                if(input.spacePressed) {
                    input.spacePressed = false;
                    if(markerX >= greenStart && markerX <= greenStart + greenWidth) {
                        hits++;
                        if(hits >= MAX_HITS) {
                            success = true;
                            resultTimer = 0f;
                            lastCaughtFish = generateFish();
                            state = FishState.RESULT;
                        }
                    } else {
                        misses++;
                        if(misses >= MAX_MISSES) {
                            success = false;
                            resultTimer = 0f;
                            state = FishState.RESULT;
                        }
                    }
                }
            }


            case RESULT -> {
                resultTimer += delta;
                if(resultTimer >= 2.5f) {
                    reset();
                }
            }
        }
    }

    private void startMinigame(Player player) {
        hits      = 0;
        misses    = 0;
        markerX   = 0;
        markerDir = 1;

        int col = player.getX() / TileMap.TILE_SIZE;
        int row = player.getY() / TileMap.TILE_SIZE;
        currentZone = ZoneRegistry.getZoneAtTile(col, row);

        float baitBonus = player.getEquippedBait() != null ? player.getEquippedBait().rarityBonus : 0f;
        caughtType = FishRegistry.getRandom(currentZone, baitBonus);

        float baseGreen;
        float baseSpeed;
        switch(caughtType.rarity) {
            case COMMON    -> { baseGreen = 120f; baseSpeed = 200f; }
            case UNCOMMON  -> { baseGreen = 90f;  baseSpeed = 260f; }
            case RARE      -> { baseGreen = 60f;  baseSpeed = 300f; }
            default        -> { baseGreen = 35f;  baseSpeed = 400f; }
        }

        float stability  = player.getEquippedRod() != null ? player.getEquippedRod().stability  : 0.6f;
        float speedMalus = player.getEquippedRod() != null ? player.getEquippedRod().speedMalus : 0f;

        greenWidth  = baseGreen * stability;
        markerSpeed = baseSpeed * (1f - speedMalus);

        greenStart = (BAR_WIDTH / 2f) - (greenWidth / 2f);
        state = FishState.MINIGAME;
    }

    private void reset() {
        state     = FishState.CASTING;
        hits      = 0;
        misses    = 0;
        markerX   = 0;
    }

    public void resetSuccess() {
        success = false;
        caughtType = null;
    }

    private Fish generateFish() {
        if (caughtType == null) return null;
        float sizeRatio = random.nextFloat();
        float wRatio = Math.clamp(sizeRatio + (random.nextFloat() - 0.5f) * 0.15f, 0f, 1f);
        float lRatio = Math.clamp(sizeRatio + (random.nextFloat() - 0.5f) * 0.15f, 0f, 1f);
        float weight   = caughtType.minKg + wRatio * (caughtType.maxKg - caughtType.minKg);
        float lengthCm = caughtType.minCm + lRatio * (caughtType.maxCm - caughtType.minCm);
        return new Fish(caughtType, weight, lengthCm, LocalDateTime.now());
    }

    public Fish collectCaughtFish() {
        Fish f = lastCaughtFish;
        lastCaughtFish = null;
        return f;
    }

    public void fullReset() {
        state          = FishState.CASTING;
        hits           = 0;
        misses         = 0;
        markerX        = 0;
        markerDir      = 1;
        success        = false;
        caughtType     = null;
        lastCaughtFish = null;
        waitTimer      = 0f;
        biteTimer      = 0f;
        bobberX        = 0;
        bobberY        = 0;
    }

    public FishState    getState()          { return state; }
    public int          getBobberX()        { return bobberX; }
    public int          getBobberY()        { return bobberY; }
    public float        getBiteTimer()      { return biteTimer; }
    public float        getMarkerX()        { return markerX; }
    public float        getGreenStart()     { return greenStart; }
    public float        getGreenWidth()     { return greenWidth; }
    public int          getHits()           { return hits; }
    public int          getMisses()         { return misses; }
    public boolean      isSuccess()         { return success; }
    public int          getBarWidth()       { return BAR_WIDTH; }
    public float        getResultTimer()    { return resultTimer; }
    public Fish         getLastCaughtFish() { return lastCaughtFish; }
    public FishingZone  getCurrentZone()    { return currentZone; }
}

