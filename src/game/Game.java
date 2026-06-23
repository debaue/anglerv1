package game;

import data.RodRegistry;
import data.SaveManager;
import entities.Fish;
import entities.HitBox;
import entities.Player;
import entities.ShopKeeper;
import org.json.JSONObject;
import util.InputHandler;
import util.SpriteLoader;
import world.Camera;
import world.TileMap;

public class Game {

    public enum GameState {
        TITLE, PAUSE,
        EXPLORING,
        INVENTORY, FISHING_MENU, SHOP, FISH_BOOK
    }

    private GameState state = GameState.TITLE;
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
    private String lastCheckedName = "";
    private boolean saveExists = false;
    private boolean checkingName = false;
    private boolean serverReachable = true;

    public Game(InputHandler input) {
        SpriteLoader.init();
        this.input = input;

        tileMap = new TileMap();
        tileMap.loadFromFile("src/world/map01.txt");
        camera = new Camera(GamePanel.WIDTH, GamePanel.HEIGHT, tileMap);
        fishingSystem = new FishingSystem(input);
        shopKeeper = new ShopKeeper(500, 300);
        shopSystem = new ShopSystem();
    }

    public void startNewGame() {
        player = new Player(RodRegistry.getStarter(), input, tileMap);
        player.setName(nameInput.toString().trim().isEmpty() ? "Angler" : nameInput.toString().trim());
        fishingSystem.fullReset();
        state = GameState.EXPLORING;
    }

    public void loadGame() {
        String name = nameInput.toString().trim();
        player = new Player(RodRegistry.getStarter(), input, tileMap);
        player.setName(name);
        fishingSystem.fullReset();
        new Thread(() -> {
            JSONObject data = SaveManager.loadGame(name);
            if (data != null) SaveManager.applyToPlayer(data, player);
        }, "load-thread").start();
        state = GameState.EXPLORING;
    }

    public void update(float delta) {
        switch(state) {
            case TITLE -> {
                if (input.lastTyped != 0 && nameInput.length() < 16) {
                    nameInput.append(input.lastTyped);
                    input.lastTyped = 0;
                }
                if (input.backspacePressed && nameInput.length() > 0) {
                    nameInput.deleteCharAt(nameInput.length() - 1);
                    input.backspacePressed = false;
                    saveExists = false;
                }

                String currentName = nameInput.toString().trim();
                if (currentName.length() >= 2 && !currentName.equals(lastCheckedName) && !checkingName) {
                    lastCheckedName = currentName;
                    checkingName = true;
                    SaveManager.checkSaveExists(currentName,
                        exists -> { saveExists = exists; checkingName = false; },
                        reachable -> { serverReachable = reachable; });
                }

                if (input.mouseClicked) {
                    int mx = input.mouseX, my = input.mouseY;
                    int btnX = GamePanel.WIDTH / 2 - 120, btnW = 240;
                    boolean nameOk = !currentName.isEmpty();
                    if (mx >= btnX && mx <= btnX + btnW && my >= 300 && my <= 336 && nameOk) {
                        startNewGame();
                    }
                    if (mx >= btnX && mx <= btnX + btnW && my >= 356 && my <= 392 && saveExists) {
                        loadGame();
                    }
                    input.mouseClicked = false;
                }
                if (input.enterPressed && !currentName.isEmpty()) {
                    if (saveExists) loadGame(); else startNewGame();
                    input.enterPressed = false;
                }
            }

            case PAUSE -> {
                if (input.escape) {
                    state = GameState.EXPLORING;
                    input.escape = false;
                }
                if (input.mouseClicked) {
                    int mx = input.mouseX, my = input.mouseY;
                    int panelX = GamePanel.WIDTH / 2 - 150;
                    int panelY = GamePanel.HEIGHT / 2 - 110;
                    int btnX = panelX + 30, btnW = 240;
                    if (mx >= btnX && mx <= btnX + btnW && my >= panelY + 80 && my <= panelY + 124) {
                        state = GameState.EXPLORING;
                    }
                    if (mx >= btnX && mx <= btnX + btnW && my >= panelY + 140 && my <= panelY + 184) {
                        SaveManager.saveGame(player);
                        System.exit(0);
                    }
                    input.mouseClicked = false;
                }
            }

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
                if(input.escape) {
                    state = GameState.PAUSE;
                    input.escape = false;
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

                if(fishingSystem.getState() == FishingSystem.FishState.CASTING && fishingSystem.isSuccess()) {
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

    public boolean isNearShopKeeper() {
        HitBox pk = shopKeeper.getHitBox();
        HitBox plr = player.getHitBox();
        int range = 48;
        return plr.x < pk.x + pk.width  + range &&
               plr.x + plr.width  > pk.x - range &&
               plr.y < pk.y + pk.height + range &&
               plr.y + plr.height > pk.y - range;
    }

    public String        getLockedZoneMessage() { return lockedZoneTimer > 0f ? lockedZoneMessage : null; }
    public String        getNameInput()         { return nameInput.toString(); }
    public boolean       getSaveExists()        { return saveExists; }
    public boolean       isCheckingName()       { return checkingName; }
    public boolean       isServerReachable()    { return serverReachable; }

    public GameState     getState()      { return state; }
    public Player        getPlayer()     { return player; }
    public Camera        getCamera()     { return camera; }
    public TileMap       getTileMap()    { return tileMap; }
    public FishingSystem getFishing()    { return fishingSystem; }
    public ShopKeeper    getShopKeeper() { return shopKeeper; }
    public ShopSystem    getShopSystem() { return shopSystem; }
}
