package world;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TileMap {
    public static final int TILE_SIZE = 32;

    private TileType[][] tiles;
    private int cols;
    private int rows;

    public void loadFromFile(String path) {
        try {
            Scanner sc = new Scanner(new File(path));

            List<int[]> lines = new ArrayList<>();

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
        if (col < 0 || col >= cols) return null;
        if (row < 0 || row >= rows) return null;
        return tiles[row][col];
    }

    public boolean isFishable(int col, int row) {
        return getTile(col,row).fishable;
    }

    public int getDualMask(int col, int row, TileType type) {
        int mask = 0;
        if(sampleTile(col-1, row-1, type)) mask += 1;
        if(sampleTile(col,   row-1, type)) mask += 2;
        if(sampleTile(col-1, row,   type)) mask += 4;
        if(sampleTile(col,   row,   type)) mask += 8;
        return mask;
    }
// jetzt verbindet es sich zur wand
   private boolean sampleTile(int col, int row, TileType type) {
        if (col < 0 || col >= cols || row < 0 || row >= rows) return true;
        return tiles[row][col] == type;
    }



    public int getCols() { return cols; }
    public int getRows() { return rows; }
}
