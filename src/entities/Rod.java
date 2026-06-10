package entities;

public class Rod {
    public final String name;
    public final float stability;
    public final float speedMalus;
    public final int price;
    public final int tier;

    public Rod(String name, float stability, float speedMalus, int price, int tier) {
        this.name = name;
        this.stability = stability;
        this.speedMalus = speedMalus;
        this.price = price;
        this.tier = tier;
    }
}
