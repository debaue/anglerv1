package data;

import world.FishingZone;
import world.ZoneRegistry;

public class FishRegistry {
    public static final FishType[] ALL = {
        new FishType("Karpfen",      1.0f,   8.0f,  30f,  80f,    5, Rarity.COMMON,      0),
        new FishType("Barsch",       0.3f,   2.0f,  15f,  40f,    8, Rarity.COMMON,      1),
        new FishType("Forelle",      0.2f,   1.5f,  20f,  50f,    6, Rarity.COMMON,      2),
        new FishType("Rotauge",      0.1f,   0.8f,  10f,  25f,    4, Rarity.COMMON,      3),
        new FishType("Brachse",      0.5f,   3.0f,  20f,  55f,    7, Rarity.COMMON,      4),
        new FishType("Döbel",        0.3f,   2.0f,  20f,  50f,    5, Rarity.COMMON,      5),
        new FishType("Zander",       0.5f,   6.0f,  30f,  80f,   20, Rarity.UNCOMMON,    6),
        new FishType("Hecht",        1.0f,   8.0f,  40f, 110f,   25, Rarity.UNCOMMON,    7),
        new FishType("Aal",          0.5f,   3.0f,  40f,  90f,   18, Rarity.UNCOMMON,    8),
        new FishType("Schleie",      0.5f,   4.0f,  20f,  60f,   15, Rarity.UNCOMMON,    9),
        new FishType("Wels",         5.0f,  30.0f,  60f, 160f,   45, Rarity.RARE,       10),
        new FishType("Lachs",        2.0f,  12.0f,  50f, 120f,   55, Rarity.RARE,       11),
        new FishType("Bachforelle",  1.0f,   5.0f,  25f,  65f,   40, Rarity.RARE,       12),
        new FishType("Stör",        20.0f, 100.0f, 100f, 300f,  120, Rarity.LEGENDARY,  13),
        new FishType("Goldkarpfen",  5.0f,  15.0f,  40f,  90f,  200, Rarity.LEGENDARY,  14),
        new FishType("Riesenwels",  50.0f, 200.0f, 150f, 350f,  350, Rarity.LEGENDARY,  15),
    };

    public static FishType findByName(String name) {
        for (FishType ft : ALL) {
            if (ft.name.equals(name)) return ft;
        }
        return null;
    }

    public static FishType getRandom() {
        return getRandom(ZoneRegistry.STARTTEICH, 0f);
    }

    public static FishType getRandom(FishingZone zone, float baitBonus) {
        float total = 0;
        for (FishType ft : ALL) total += effectiveWeight(ft, zone, baitBonus);

        float roll = (float)(Math.random() * total);
        float acc  = 0;
        for (FishType ft : ALL) {
            acc += effectiveWeight(ft, zone, baitBonus);
            if (roll < acc) return ft;
        }
        return ALL[0];
    }

    private static float effectiveWeight(FishType ft, FishingZone zone, float baitBonus) {
        float zoneMul = switch (ft.rarity) {
            case COMMON    -> zone.commonMul;
            case UNCOMMON  -> zone.uncommonMul * (1f + baitBonus);
            case RARE      -> zone.rareMul     * (1f + baitBonus * 2f);
            case LEGENDARY -> zone.legendaryMul * (1f + baitBonus * 4f);
        };
        return Math.max(0.1f, ft.rarity.spawnHeight * zoneMul);
    }
}
