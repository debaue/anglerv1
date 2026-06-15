package world;

public class FishingZone {
    public final String name;
    public final int minCol, minRow, maxCol, maxRow;
    public final float commonMul;
    public final float uncommonMul;
    public final float rareMul;
    public final float legendaryMul;

    public FishingZone(String name, int minCol, int minRow, int maxCol, int maxRow,
                       float commonMul, float uncommonMul, float rareMul, float legendaryMul) {
        this.name        = name;
        this.minCol      = minCol;
        this.minRow      = minRow;
        this.maxCol      = maxCol;
        this.maxRow      = maxRow;
        this.commonMul   = commonMul;
        this.uncommonMul = uncommonMul;
        this.rareMul     = rareMul;
        this.legendaryMul = legendaryMul;
    }

    public boolean containsTile(int col, int row) {
        return col >= minCol && col <= maxCol && row >= minRow && row <= maxRow;
    }
}
