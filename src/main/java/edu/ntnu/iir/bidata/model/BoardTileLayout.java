package edu.ntnu.iir.bidata.model;

import java.util.ArrayList;
import java.util.List;

public class BoardTileLayout {
  public static List<Tile> snakesAndLadders(int columns, int rows) {
    List<Tile> tiles = new ArrayList<>();

    double dx = 1.0 / columns;
    double dy = 1.0 / rows;
    double x = 0;
    double y = 1 - dy;
    int xDir = 1;

    int tileCount = 0;
    int totalTileCount = columns * rows;

    // create the tiles
    while (tileCount < totalTileCount) {
      // create a tile
      Tile tile = new Tile(tileCount + 1);
      tile.setPosition(x, y);
      tile.setSize(dx, dy);
      tiles.add(tile);
      tileCount++;

      // connect the previous tile to this tile
      if (tileCount >= 2) {
        tiles.get(tiles.size() - 2).setNextTile(tile);
      }

      // move to the next position
      x += xDir * dx;
      boolean shouldChangeXDir = tileCount % columns == 0;
      if (shouldChangeXDir) {
        xDir = -xDir;  // reverse direction
        x += xDir * dx;  // move back into the grid
        y -= dy;  // move up one row
        if (tileCount >= columns * rows) {
          break;
        }
      }
    }

    return tiles;
  }
}
