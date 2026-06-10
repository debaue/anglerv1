package data;

public class FishType {
    public final String name;
    public final float minKg;
    public final float maxKg;
    public final float minCm;
    public final float maxCm;
    public final int basePrice;
    public final Rarity rarity;
    public final int spriteIndex;

    public FishType(String name, float minKg, float maxKg, float minCm, float maxCm,
                    int basePrice, Rarity rarity, int spriteIndex) {
        this.name = name;
        this.minKg = minKg;
        this.maxKg = maxKg;
        this.minCm = minCm;
        this.maxCm = maxCm;
        this.basePrice = basePrice;
        this.rarity = rarity;
        this.spriteIndex = spriteIndex;
    }
}
