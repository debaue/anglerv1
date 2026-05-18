package util;

import world.TileMap;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class SpriteLoader {

    private static BufferedImage[][] idle_frames;
    private static BufferedImage[][] walk_frames;
    private static BufferedImage[][] fish_frames;
    private static BufferedImage[]   grass_tiles;
    private static BufferedImage[]   water_tiles;
    private static BufferedImage[]   ground_tiles;

    public static void init() {
        System.out.println(SpriteLoader.class.getResource("/character/Character_Idle_Angle.png"));
        // wenn null → Pfad falsch oder kein Resources Root

        idle_frames  = loadSpritesSet("/player-sprites/Character_Idle_Angle.png", 48);
        walk_frames  = loadSpritesSet("/player-sprites/Character_Walk_Angle.png", 48);
        fish_frames  = loadSpritesSet("/player-sprites/Character_Fishing_Angle.png", 48);
        grass_tiles  = loadTileSet("/tileset-sprites/Tileset_Grass.png", 16);
        water_tiles  = loadTileSet("/tileset-sprites/TileSet_Wasser.png", 16);
        ground_tiles = loadTileSet("/tileset-sprites/Tileset_Weg.png", 16);
    }

    public static BufferedImage[][] loadSpritesSet(String path, int tileSize) {
        BufferedImage sprite = load(path);
        int cols = sprite.getWidth()  / tileSize;
        int rows = sprite.getHeight() / tileSize;

        BufferedImage[][] tiles = new BufferedImage[rows][cols];

        for(int r = 0; r < rows; r++) {
            for(int c = 0; c < cols; c++) {
                tiles[r][c] = sprite.getSubimage(
                        c * tileSize,
                        r * tileSize,
                        tileSize,
                        tileSize
                );
            }
        }
        return tiles;
    }


    public static BufferedImage[] loadTileSet(String path, int tileSize) {
        BufferedImage sprite = load(path);
        int cols = sprite.getWidth() / tileSize;
        int rows = sprite.getHeight() / tileSize;

        BufferedImage[] tiles = new BufferedImage[cols*rows];
        for(int r = 0; r<rows; r++) {
            for(int c = 0; c< cols; c++) {
                tiles[r*cols +c] = sprite.getSubimage(
                        c*tileSize,
                        r*tileSize,
                        tileSize,
                        tileSize
                );
            }
        }
        return tiles;

    }

    public static BufferedImage load(String path) {
        try {
            return ImageIO.read(
                    SpriteLoader.class.getResource(path) // weil wir es im resource directory haben können wir es nicht mit Imageio read new File lesen
            );
        } catch (Exception e) {
            System.err.println("Sprite nicht gefunden: " + path);
            return null;
        }
    }


    public static BufferedImage[][] getIdleFrames()  { return idle_frames; }
    public static BufferedImage[][] getWalkFrames()  { return walk_frames; }
    public static BufferedImage[][] getFishFrames()  { return fish_frames; }
    public static BufferedImage[]   getGrassTiles()  { return grass_tiles; }
    public static BufferedImage[]   getWaterTiles()  { return water_tiles; }
    public static BufferedImage[]   getGroundTiles() { return ground_tiles; }

}
