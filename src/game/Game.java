package game;

import data.RodRegistry;
import entities.Fish;
import entities.HitBox;
import entities.Player;
import entities.ShopKeeper;
import util.InputHandler;
import util.SpriteLoader;
import world.Camera;
import world.FishingZone;
import world.TileMap;
import world.ZoneRegistry;

public class Game {

    public enum GameState {
        EXPLORING,
        INVENTORY, FISHING_MENU, SHOP, FISH_BOOK
    }

    private GameState state = GameState.EXPLORING;
    private Player player;
    private TileMap tileMap;
    private Camera camera;
    private InputHandler input;
    private FishingSystem fishingSystem;
    private ShopKeeper shopKeeper;
    private ShopSystem shopSystem;

    private String lockedZoneMessage = null;
    private float lockedZoneTimer = 0f;

    private final StringBuilder nameInput = new StringBuilder();
    private int titleSelection = 0; // 0 = Neues Spiel, 1 = Weiterspielen
    public static final boolean SAVE_EXISTS = false; // wird true wenn JSON fertig ist

    public Game(InputHandler input) {
        SpriteLoader.init();
        this.input = input;

        tileMap = new TileMap();
        tileMap.loadFromFile("src/world/map01.txt");
        camera = new Camera(GamePanel.WIDTH, GamePanel.HEIGHT, tileMap);
        fishingSystem = new FishingSystem(input);
        shopKeeper = new ShopKeeper(500, 300);
        shopSystem = new ShopSystem();
        player = new Player(RodRegistry.getStarter(), input, tileMap);
    }
    public void update(float delta) {
        switch(state) {
            case EXPLORING -> {
                player.update(delta);
                camera.update(player.getX(), player.getY());

                if(lockedZoneTimer > 0f) {
                    lockedZoneTimer -= delta;
                    if(lockedZoneTimer <= 0f) lockedZoneMessage = null;
                }

                if(input.fishing && player.isNearWater()) {
                    state = GameState.FISHING_MENU;
                    input.fishing = false;
                }
                if(input.inventoryPressed) {
                    state = GameState.INVENTORY;
                    input.inventoryPressed = false;
                }
                if(input.fishBookPressed) {
                    state = GameState.FISH_BOOK;
                    input.fishBookPressed = false;
                }

                if(input.interactPressed && isNearShopKeeper()) {
                    state = GameState.SHOP;
                    input.interactPressed = false;
                }
            }
            case INVENTORY -> {
                if (input.inventoryPressed || input.escape) {
                    state = GameState.EXPLORING;
                    input.inventoryPressed = false;
                    input.escape = false;
                }
            }
            case FISH_BOOK -> {
                if (input.fishBookPressed || input.escape) {
                    state = GameState.EXPLORING;
                    input.fishBookPressed = false;
                    input.escape = false;
                }
            }

            case SHOP -> {
                shopSystem.update(player, input);
                if(input.escape) {
                    state = GameState.EXPLORING;
                    input.escape = false;
                }
            }

            case FISHING_MENU -> {
                fishingSystem.update(delta, player);

                if(fishingSystem.getState() == FishingSystem.FishState.CASTING
                        && fishingSystem.isSuccess()) {
                    Fish caught = fishingSystem.collectCaughtFish();
                    if(caught != null) player.addFish(caught);
                    fishingSystem.resetSuccess();
                    state = GameState.EXPLORING;
                }

                if(input.escape) {
                    if(fishingSystem.isSuccess()) {
                        Fish caught = fishingSystem.collectCaughtFish();
                        if(caught != null) player.addFish(caught);
                    }
                    fishingSystem.fullReset();
                    state = GameState.EXPLORING;
                    input.escape = false;
                }
            }
        }
    }

    private boolean isNearShopKeeper() {
        HitBox pk = shopKeeper.getHitBox();
        HitBox plr = player.getHitBox();
        int range = 48;
        return plr.x < pk.x + pk.width  + range &&
               plr.x + plr.width  > pk.x - range &&
               plr.y < pk.y + pk.height + range &&
               plr.y + plr.height > pk.y - range;
    }

    public String           getLockedZoneMessage() { return lockedZoneTimer > 0f ? lockedZoneMessage : null; }
    public String           getNameInput()         { return nameInput.toString(); }
    public int              getTitleSelection()    { return titleSelection; }

    public GameState        getState()       { return state; }
    public Player           getPlayer()      { return player; }
    public Camera           getCamera()      { return camera; }
    public TileMap          getTileMap()     { return tileMap; }
    public FishingSystem    getFishing()     { return fishingSystem; }
    public ShopKeeper getShopKeeper() { return shopKeeper; }
    public ShopSystem       getShopSystem()  { return shopSystem; }
}
