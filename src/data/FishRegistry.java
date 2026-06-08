package data;

public class FishRegistry {
    public static final FishType[] ALL = {

        new FishType("Karpfen",     1.0f,  8.0f,   5,  Rarity.COMMON,     0),
        new FishType("Barsch",      0.3f,  2.0f,   8,  Rarity.COMMON,     1),
        new FishType("Forelle",     0.2f,  1.5f,   6,  Rarity.COMMON,     2),
        new FishType("Rotauge",     0.1f,  0.8f,   4,  Rarity.COMMON,     3),

         new FishType("Brachse",     0.5f,  3.0f,   7,  Rarity.COMMON,     4),
        new FishType("Döbel",       0.3f,  2.0f,   5,  Rarity.COMMON,     5),
        new FishType("Zander",      0.5f,  6.0f,  20,  Rarity.UNCOMMON,   6),
        new FishType("Hecht",       1.0f,  8.0f,  25,  Rarity.UNCOMMON,   7),

            new FishType("Aal",         0.5f,  3.0f,  18,  Rarity.UNCOMMON,   8),
        new FishType("Schleie",     0.5f,  4.0f,  15,  Rarity.UNCOMMON,   9),
        new FishType("Wels",        5.0f, 30.0f,  45,  Rarity.RARE,      10),
        new FishType("Lachs",       2.0f, 12.0f,  55,  Rarity.RARE,      11),
        new FishType("Bachforelle", 1.0f,  5.0f,  40,  Rarity.RARE,      12),
        new FishType("Stör",       20.0f,100.0f, 120,  Rarity.LEGENDARY, 13),
        new FishType("Goldkarpfen", 5.0f, 15.0f, 200,  Rarity.LEGENDARY, 14),
        new FishType("Riesenwels", 50.0f,200.0f, 350,  Rarity.LEGENDARY, 15),
    };

    public static FishType findByName(String name) {
        for (FishType ft : ALL) {
            if (ft.name.equals(name)) return ft;
        }
        return null;
    }

    public static FishType getRandom() {
        int totalWeight = 0;
        for(FishType ft : ALL) { totalWeight += ft.rarity.spawnHeight;}
        int roll = (int) (Math.random() * totalWeight);
        int num = 0;
        for(FishType ft:  ALL) {
            num += ft.rarity.spawnHeight;
            if(roll <num) return ft;
        }
        return ALL[0];
    }
}
