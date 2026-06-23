package entities;

import java.time.LocalDateTime;

public class FishBookEntry {
    private final String fishName;
    private int caughtCount;

    private float heaviestWeight;
    private LocalDateTime heaviestDate;

    private float longestLength;
    private LocalDateTime longestDate;

    public FishBookEntry(String fishName) {
        this.fishName = fishName;
        this.caughtCount = 0;
        this.heaviestWeight = 0f;
        this.longestLength = 0f;
    }

    public void registerCatch(Fish fish) {
        caughtCount++;

        if (fish.weightKg > heaviestWeight) {
            heaviestWeight = fish.weightKg;
            heaviestDate = fish.caughtAt;
        }

        if (fish.lengthCm > longestLength) {
            longestLength = fish.lengthCm;
            longestDate = fish.caughtAt;
        }
    }

    public void restoreStats(int count, float heaviest, float longest) {
        this.caughtCount    = count;
        this.heaviestWeight = heaviest;
        this.longestLength  = longest;
    }

    public String getFishName()            { return fishName; }
    public int getCaughtCount()            { return caughtCount; }
    public float getHeaviestWeight()       { return heaviestWeight; }
    public LocalDateTime getHeaviestDate() { return heaviestDate; }
    public float getLongestLength()        { return longestLength; }
    public LocalDateTime getLongestDate()  { return longestDate; }
}
