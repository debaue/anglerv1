package world;

import java.awt.*;

public enum TileType {
    EARTH(0, false, new Color(140,100,60)),
    GRASS(1,false, new Color(80,160,60)),
    WATER(2,true, new Color(30,100,160));

    public final int id;
    public final boolean fishable;
    public final Color color;

    TileType(int id, boolean fishable, Color color) {
        this.id = id;
        this.fishable = fishable;
        this.color = color;
    }

    public static TileType fromId(int id) {
        for(TileType t:values()) {
            if(t.id == id) return t;
        }
        return EARTH;
    }
}
