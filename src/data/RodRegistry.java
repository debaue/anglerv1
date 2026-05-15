package data;

import entities.Rod;

public class RodRegistry {
    public static final Rod[] ALL = {
            new Rod("Holzrute", 0.6f, 0.0f,0),
            new Rod("Stahlrute", 0.8f, 0.15f, 150),
            new Rod("Profi-Rute", 0.9f, 0.15f,350)
    };

    public static Rod getStarter() {
        return ALL[0];
    }
}
