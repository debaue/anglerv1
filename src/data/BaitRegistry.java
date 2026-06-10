package data;

import entities.Bait;

public class BaitRegistry {
    public static final Bait[] ALL = {
            new Bait("Standard-Koeder",  0.00f,   0),
            new Bait("Wurm-Koeder",      0.10f, 100),
            new Bait("Premium-Koeder",   0.20f, 200),
            new Bait("Legend-Koeder",    0.40f, 400)
    };

    public static Bait getStarter() {
        return ALL[0];
    }
}
