package data;

public class FishRegistry {
    public static final FishType[] ALL = {
            new FishType("Karpfen", 1.0f, 8.0f, 5, Rarity.COMMON),
            new FishType("Barsch", 0.3f, 2.0f, 8, Rarity.COMMON),
            new FishType("Zander",     0.5f, 6.0f,  20,  Rarity.UNCOMMON),
            new FishType("Wels",       5.0f, 30.0f, 45,  Rarity.RARE)
    };

    public static FishType getRandom() {
        int totalWeight = 0;
        for(FishType ft : ALL) { totalWeight += ft.rarity.spawnHoehe;}
        int roll = (int) (Math.random() * totalWeight);
        int num = 0;
        for(FishType ft:  ALL) {
            num += ft.rarity.spawnHoehe;
            if(roll <num) return ft;
        }
        return ALL[0];
    }
}
