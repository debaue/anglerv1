package world;

public class ZoneRegistry {

    public static final FishingZone STARTTEICH = new FishingZone("Startteich", 1.6f, 0.8f, 0.4f, 0.1f);
    public static final FishingZone OSTSEE     = new FishingZone("Ostsee",     0.8f, 1.5f, 1.0f, 0.3f);
    public static final FishingZone SUEDSEE    = new FishingZone("Südsee",     0.6f, 1.0f, 1.8f, 0.7f);

    public static final FishingZone[] ALL = { STARTTEICH, OSTSEE, SUEDSEE };

}
