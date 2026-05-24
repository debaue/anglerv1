package data;

public class FishType {
    public final String name;
    public final float minKg;
    public final float maxKg;
    public final int basePrice;
    public final Rarity rarity;

    public FishType(String name, float minKg, float maxKg, int basePrice, Rarity rarity) {
        this.name = name;
        this.minKg = minKg;
        this.maxKg = maxKg;
        this.basePrice = basePrice;
        this.rarity = rarity;
    }
}
