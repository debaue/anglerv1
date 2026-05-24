package world;

public class Camera {

    private int x;
    private int y;

    private final int screenWidth;
    private final int screenHeight;
    private TileMap map;

    public Camera(int screenWidth, int screenHeight, TileMap map) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.x = 0;
        this.y = 0;
        this.map = map;
    }

    public void update(int playerX, int playerY) {
        x = playerX - screenWidth  / 2;
        y = playerY - screenHeight / 2;

        x = Math.max(0, x);
        y = Math.max(0, y);
        x = Math.min(x, map.getCols() * TileMap.TILE_SIZE - screenWidth);
        y = Math.min(y, map.getRows() * TileMap.TILE_SIZE - screenHeight);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
