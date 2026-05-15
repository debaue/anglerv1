package entities;

public class Rod {
    public final String name;
    public final float stability;
    public final float speedMalus;
    public final int price;


    public Rod(String name, float stability, float speedMalus, int price) {
        this.name = name;
        this.stability = stability;
        this.speedMalus = speedMalus;
        this.price = price;
    }
}
