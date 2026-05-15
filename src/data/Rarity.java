package data;

public enum Rarity {
    COMMON(1.0f, 60, 1.8f),
    UNCOMMON(1.8f, 25, 1.0f),
    RARE(3.5f, 12, 0.6f),
    LEGENDARY(8.0f, 3, 0.25f);


    public final float preisMultiplikator;
    public final int spawnHoehe;
    public final float biteWindow;

    Rarity(float preisMultiplikator, int spawnHoehe, float biteWindow) {
        this.preisMultiplikator = preisMultiplikator;
        this.spawnHoehe = spawnHoehe;
        this.biteWindow = biteWindow;
    }




}
