package world;

import java.awt.*;

public enum TileType {
    GROUND(0,false, false, new int[]{12,15,8,9,0,11,14,7,13,4,1,10,3,2,5,6}),
    WATER(2, true,true,  new int[]{12,15,8,9,0,11,14,7,13,4,1,10,3,2,5,6}),
    GRASS(1,false, false, new int[]{12,15,8,9,0,11,14,7,13,4,1,10,3,2,5,6});


    public final int id;
    public final boolean fishable;
    public final int[] lookup;
    public final boolean blocked;

    TileType(int id,boolean blocked, boolean fishable, int[] lookup) {
        this.id = id;
        this.blocked = blocked;
        this.fishable = fishable;
        this.lookup = lookup;
    }

    public static TileType fromId(int id) {
        for(TileType t:values()) {
            if(t.id == id) return t;
        }
        return GROUND;
    }
}
