package entities;

import data.FishType;

import java.time.LocalDateTime;

public class Fish {
    public final FishType type;
    public final float weightKg;
    public final float lengthCm;
    public final int price;
    public final LocalDateTime caughtAt;

    public Fish(FishType type, float weightKg, float lengthCm, LocalDateTime caughtAt) {
        this.type = type;
        this.weightKg = weightKg;
        this.lengthCm = lengthCm;
        this.caughtAt = caughtAt;
        this.price = calcPrice();
    }

    private int calcPrice() {
        return Math.round(weightKg * type.basePrice * type.rarity.preisMultiplier);
    }

    @Override
    public String toString() {
        return type.name + " (" + weightKg + "kg, " + lengthCm + "cm) = " + price + "G";
    }
}
