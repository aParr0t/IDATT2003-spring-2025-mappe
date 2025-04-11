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

  public static List<Tile> monopoly() {
    List<Tile> tiles = new ArrayList<>();

    // tile configuration
    double CORNER_TILE_SIZE = 0.15;
    int sideTileCount = 9;
    double SIDE_TILE_SIZE = (1 - 2 * CORNER_TILE_SIZE) / sideTileCount;

    double x = 0;
    double y = 0;
    int tileIndex = 0;

    // create the tiles
    // GO tile
    Tile goTile = new Tile(tileIndex++);
    goTile.setPosition(x, y);
    goTile.setSize(CORNER_TILE_SIZE, CORNER_TILE_SIZE);
    tiles.add(goTile);
    x += CORNER_TILE_SIZE;

    // side 1
    for (int i = 0; i < sideTileCount; i++) {
      Tile sideTile = new Tile(tileIndex++);
      sideTile.setPosition(x, y);
      sideTile.setSize(SIDE_TILE_SIZE, CORNER_TILE_SIZE);
      tiles.add(sideTile);
      x += SIDE_TILE_SIZE;
    }

    // Prison tile
    Tile prisonTile = new Tile(tileIndex++);
    prisonTile.setPosition(x, y);
    prisonTile.setSize(CORNER_TILE_SIZE, CORNER_TILE_SIZE);
    tiles.add(prisonTile);

    // side 2
    y += CORNER_TILE_SIZE;
    for (int i = 0; i < sideTileCount; i++) {
      Tile sideTile = new Tile(tileIndex++);
      sideTile.setPosition(x, y);
      sideTile.setSize(CORNER_TILE_SIZE, SIDE_TILE_SIZE);
      tiles.add(sideTile);
      y += SIDE_TILE_SIZE;
    }

    // Parking tile
    Tile parkingTile = new Tile(tileIndex++);
    parkingTile.setPosition(x, y);
    parkingTile.setSize(CORNER_TILE_SIZE, CORNER_TILE_SIZE);
    tiles.add(parkingTile);


    // side 3
    for (int i = 0; i < sideTileCount; i++) {
      x -= SIDE_TILE_SIZE;
      Tile sideTile = new Tile(tileIndex++);
      sideTile.setPosition(x, y);
      sideTile.setSize(SIDE_TILE_SIZE, CORNER_TILE_SIZE);
      tiles.add(sideTile);
    }

    // Go to jail tile
    x -= CORNER_TILE_SIZE;
    Tile goToJailTile = new Tile(tileIndex++);
    goToJailTile.setPosition(x, y);
    goToJailTile.setSize(CORNER_TILE_SIZE, CORNER_TILE_SIZE);
    tiles.add(goToJailTile);

    // side 4
    for (int i = 0; i < sideTileCount; i++) {
      y -= SIDE_TILE_SIZE;
      Tile sideTile = new Tile(tileIndex++);
      sideTile.setPosition(x, y);
      sideTile.setSize(CORNER_TILE_SIZE, SIDE_TILE_SIZE);
      tiles.add(sideTile);
    }

    return tiles;
  }
}
