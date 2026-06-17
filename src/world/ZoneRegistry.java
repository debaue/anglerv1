package world;

public class ZoneRegistry {

    public static final FishingZone STARTTEICH = new FishingZone(
            "Startteich",
            5, 4, 20, 15,
            1.6f, 0.8f, 0.4f, 0.1f
    );

    public static final FishingZone OSTSEE = new FishingZone(
            "Ostsee",
            31, 11, 53, 24,
            0.8f, 1.5f, 1.0f, 0.3f
    );

    public static final FishingZone SUEDSEE = new FishingZone(
            "Südsee",
            16, 27, 43, 35,
            0.6f, 1.0f, 1.8f, 0.7f
    );

    public static final FishingZone[] ALL = { STARTTEICH, OSTSEE, SUEDSEE };

    public static FishingZone getZoneAtTile(int col, int row) {
        for (int r = 0; r <= 4; r++) {
            for (int dc = -r; dc <= r; dc++) {
                for (int dr = -r; dr <= r; dr++) {
                    if (Math.abs(dc) != r && Math.abs(dr) != r) continue;
                    for (FishingZone z : ALL) {
                        if (z.containsTile(col + dc, row + dr)) return z;
                    }
                }
            }
        }
        return STARTTEICH;
    }
}
