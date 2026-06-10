package data;

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
        return getRandom(0f);
    }

    public static FishType getRandom(float rarityBonus) {
        float totalWeight = 0;
        for (FishType ft : ALL) totalWeight += effectiveWeight(ft, rarityBonus);

        float roll = (float)(Math.random() * totalWeight);
        float acc  = 0;
        for (FishType ft : ALL) {
            acc += effectiveWeight(ft, rarityBonus);
            if (roll < acc) return ft;
        }
        return ALL[0];
    }

    private static float effectiveWeight(FishType ft, float rarityBonus) {
        float multiplier = switch (ft.rarity) {
            case COMMON    -> 1f;
            case UNCOMMON  -> 1f + rarityBonus;
            case RARE      -> 1f + rarityBonus * 2f;
            case LEGENDARY -> 1f + rarityBonus * 4f;
        };
        return ft.rarity.spawnHeight * multiplier;
    }
}
