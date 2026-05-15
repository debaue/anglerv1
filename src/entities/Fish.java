package entities;

import data.FishType;

public class Fish {
    public final FishType type;
    public final float weightKg;
    public final int price;

    public Fish(FishType type, float weightKg) {
        this.type = type;
        this.weightKg = weightKg;
        this.price = calcPrice();
    }

    private int calcPrice() {
        return Math.round(weightKg*type.basePrice*type.rarity.preisMultiplikator);
    }

    @Override
    public String toString() {
        return type.name + " (" + weightKg + "kg) = " + price + "G";
    }
}
