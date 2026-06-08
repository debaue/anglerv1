package game;

import data.RodRegistry;
import entities.Fish;
import entities.Player;
import util.InputHandler;
import util.SpriteLoader;
import world.Camera;
import world.TileMap;

public class Game {

    public enum GameState {
        EXPLORING,
        INVENTORY, FISHING_MENU, SHOP
    }

    private GameState state = GameState.EXPLORING;
    private Player player;
    private TileMap tileMap;
    private Camera camera;
    private InputHandler input;
    private FishingSystem fishingSystem;
    private entities.ShopKeeper shopKeeper;
    private ShopSystem shopSystem;

    public Game(InputHandler input) {
        SpriteLoader.init();
        this.input = input;

        tileMap = new TileMap();
        tileMap.loadFromFile("src/world/map01.txt");
        player = new Player(RodRegistry.getStarter(),input,tileMap);
        camera = new Camera(GamePanel.WIDTH,GamePanel.HEIGHT, tileMap);
        fishingSystem = new FishingSystem(input);

        shopKeeper = new entities.ShopKeeper(500, 300);
        shopSystem = new ShopSystem();
    }
    public void update(float delta) {
        switch(state) {
            case EXPLORING -> {
                player.update(delta);
                camera.update(player.getX(), player.getY());

                if(input.fishing && player.isNearWater()) {
                    state = GameState.FISHING_MENU;
                    input.fishing = false;
                }
                if(input.inventoryPressed) {
                    state = GameState.INVENTORY;
                    input.inventoryPressed = false;
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

            case SHOP -> {
                shopSystem.update(player, input);
                if(input.escape) {
                    state = GameState.EXPLORING;
                    input.escape = false;
                }
            }

            case FISHING_MENU -> {
                fishingSystem.update(delta);

                if(fishingSystem.getState() == FishingSystem.FishState.CASTING
                        && fishingSystem.isSuccess()) {
                    Fish caught = fishingSystem.collectCaughtFish();
                    System.out.println("Caught: " + (caught != null ? caught.type.name : "null"));
                    if(caught != null) {
                        boolean added = player.addFish(caught);
                        System.out.println("Added: " + added + " InvSize: " + player.getInventory().size());
                    }
                    fishingSystem.resetSuccess();
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
        entities.HitBox pk = shopKeeper.getHitBox();
        entities.HitBox plr = player.getHitBox();
        int range = 48;
        return plr.x < pk.x + pk.width  + range &&
               plr.x + plr.width  > pk.x - range &&
               plr.y < pk.y + pk.height + range &&
               plr.y + plr.height > pk.y - range;
    }

    public GameState        getState()       { return state; }
    public Player           getPlayer()      { return player; }
    public Camera           getCamera()      { return camera; }
    public TileMap          getTileMap()     { return tileMap; }
    public FishingSystem    getFishing()     { return fishingSystem; }
    public entities.ShopKeeper getShopKeeper() { return shopKeeper; }
    public ShopSystem       getShopSystem()  { return shopSystem; }
}
