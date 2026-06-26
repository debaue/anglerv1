package world;

public class FishingZone {
    public final String name;
    public final float commonMul;
    public final float uncommonMul;
    public final float rareMul;
    public final float legendaryMul;

    public FishingZone(String name, float commonMul, float uncommonMul, float rareMul, float legendaryMul) {
        this.name         = name;
        this.commonMul    = commonMul;
        this.uncommonMul  = uncommonMul;
        this.rareMul      = rareMul;
        this.legendaryMul = legendaryMul;
    }
}
