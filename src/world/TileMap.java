package world;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TileMap {
    public static final int TILE_SIZE = 48;

    private TileType[][] tiles;
    private int cols;
    private int rows;

    public void loadFromFile(String path) {
        try {
            Scanner sc = new Scanner(new File(path));

            rows = sc.nextInt();
            cols = sc.nextInt();
            tiles = new TileType[rows][cols];

            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    int id = sc.nextInt();
                    tiles[r][c] = TileType.fromId(id);
                }
            }

            sc.close();

        } catch (Exception e) {
            System.err.println("" + e.getMessage());
        }
    }

    public TileType getTile(int col, int row) {
        if (col < 0 || col >= cols) return TileType.EARTH;
        if (row < 0 || row >= rows) return TileType.EARTH;
        return tiles[row][col];
    }

    public boolean isFishable(int col, int row) {
        return getTile(col,row).fishable;
    }

    public int getCols() { return cols; }
    public int getRows() { return rows; }
}
