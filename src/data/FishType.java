package data;

public class FishType {
    public final String name;
    public final float minKg;
    public final float maxKg;
    public final int basePrice;
    public final Rarity rarity;
    public final int spriteIndex; // Position im 4x4 Spritesheet (0-15)

    public FishType(String name, float minKg, float maxKg, int basePrice, Rarity rarity) {
        this.name = name;
        this.minKg = minKg;
        this.maxKg = maxKg;
        this.basePrice = basePrice;
        this.rarity = rarity;
        this.spriteIndex = -1;
    }

    public FishType(String name, float minKg, float maxKg, int basePrice, Rarity rarity, int spriteIndex) {
        this.name = name;
        this.minKg = minKg;
        this.maxKg = maxKg;
        this.basePrice = basePrice;
        this.rarity = rarity;
        this.spriteIndex = spriteIndex;
    }
}
