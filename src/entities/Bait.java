package entities;

public class Bait {
    public final String name;
    public final float rarityBonus;
    public final int price;

    public Bait(String name, float rarityBonus, int price) {
        this.name = name;
        this.rarityBonus = rarityBonus;
        this.price = price;
    }
}
