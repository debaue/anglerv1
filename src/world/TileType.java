package world;

import java.awt.*;

public enum TileType {
//    EARTH(0, false, new Color(140,100,60)),
//    GRASS(1,false, new Color(80,160,60)),
//    WATER(2,true, new Color(30,100,160));

    GROUND(0, false, new int[]{12,15,8,9,0,11,14,7,13,4,1,10,3,2,5,6}),
    WATER(2, true,  new int[]{12,15,8,9,0,11,14,7,13,4,1,10,3,2,5,6}),
    GRASS(1, false, new int[]{12,15,8,9,0,11,14,7,13,4,1,10,3,2,5,6});


    public final int id;
    public final boolean fishable;
    public final int[] lookup;

    TileType(int id, boolean fishable, int[] lookup) {
        this.id = id;
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
