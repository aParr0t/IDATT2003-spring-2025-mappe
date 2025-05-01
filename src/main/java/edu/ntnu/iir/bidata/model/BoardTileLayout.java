package edu.ntnu.iir.bidata.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for creating different board tile layouts for various games.
 * Provides static methods to generate tile layouts for games like Snakes and Ladders
 * and Monopoly with specific positioning and connections.
 */
public class BoardTileLayout {
  /**
   * Creates a snake and ladders board layout with the specified dimensions.
   *
   * @param columns the number of columns in the grid
   * @param rows the number of rows in the grid
   * @return a list of tiles representing the snake and ladders board
   */
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

  /**
   * Helper class to manage tile indices for board generation.
   * Handles index rotation when maximum index is reached.
   */
  private static class TileIndexManager {
    private int tileIndex;
    private int maxIndex;

    /**
     * Constructs a new TileIndexManager with specified starting and maximum indices.
     *
     * @param startIndex the initial index value
     * @param maxIndex the maximum index value before resetting to zero
     */
    public TileIndexManager(int startIndex, int maxIndex) {
      this.tileIndex = startIndex;
      this.maxIndex = maxIndex;
    }
    
    /**
     * Gets the next available index and increments the counter.
     * Resets to zero if maximum index is reached.
     *
     * @return the next available index
     */
    public int getNextIndex() {
      if (tileIndex >= maxIndex) {
        tileIndex = 0;
      }
      return tileIndex++;
    }
  }

  /**
   * Creates a Monopoly board layout with standard dimensions.
   * The board consists of corner tiles and side tiles arranged in a square pattern.
   *
   * @return a list of tiles representing the Monopoly board
   */
  public static List<Tile> monopoly() {
    List<Tile> tiles = new ArrayList<>();

    // tile configuration
    double CORNER_TILE_SIZE = 0.15;
    int sideTileCount = 9;
    double SIDE_TILE_SIZE = (1 - 2 * CORNER_TILE_SIZE) / sideTileCount;

    double x = 0;
    double y = 0;
    
    // Create index manager
    TileIndexManager indexManager = new TileIndexManager(20, 40);

    // create the tiles
    // GO tile
    Tile goTile = new Tile(indexManager.getNextIndex());
    goTile.setPosition(x, y);
    goTile.setSize(CORNER_TILE_SIZE, CORNER_TILE_SIZE);
    tiles.add(goTile);
    x += CORNER_TILE_SIZE;

    // side 1
    for (int i = 0; i < sideTileCount; i++) {
      Tile sideTile = new Tile(indexManager.getNextIndex());
      sideTile.setPosition(x, y);
      sideTile.setSize(SIDE_TILE_SIZE, CORNER_TILE_SIZE);
      tiles.add(sideTile);
      x += SIDE_TILE_SIZE;
    }

    // Prison tile
    Tile prisonTile = new Tile(indexManager.getNextIndex());
    prisonTile.setPosition(x, y);
    prisonTile.setSize(CORNER_TILE_SIZE, CORNER_TILE_SIZE);
    tiles.add(prisonTile);

    // side 2
    y += CORNER_TILE_SIZE;
    for (int i = 0; i < sideTileCount; i++) {
      Tile sideTile = new Tile(indexManager.getNextIndex());
      sideTile.setPosition(x, y);
      sideTile.setSize(CORNER_TILE_SIZE, SIDE_TILE_SIZE);
      tiles.add(sideTile);
      y += SIDE_TILE_SIZE;
    }

    // Parking tile
    Tile parkingTile = new Tile(indexManager.getNextIndex());
    parkingTile.setPosition(x, y);
    parkingTile.setSize(CORNER_TILE_SIZE, CORNER_TILE_SIZE);
    tiles.add(parkingTile);

    // side 3
    for (int i = 0; i < sideTileCount; i++) {
      x -= SIDE_TILE_SIZE;
      Tile sideTile = new Tile(indexManager.getNextIndex());
      sideTile.setPosition(x, y);
      sideTile.setSize(SIDE_TILE_SIZE, CORNER_TILE_SIZE);
      tiles.add(sideTile);
    }

    // Go to jail tile
    x -= CORNER_TILE_SIZE;
    Tile goToJailTile = new Tile(indexManager.getNextIndex());
    goToJailTile.setPosition(x, y);
    goToJailTile.setSize(CORNER_TILE_SIZE, CORNER_TILE_SIZE);
    tiles.add(goToJailTile);

    // side 4
    for (int i = 0; i < sideTileCount; i++) {
      y -= SIDE_TILE_SIZE;
      Tile sideTile = new Tile(indexManager.getNextIndex());
      sideTile.setPosition(x, y);
      sideTile.setSize(CORNER_TILE_SIZE, SIDE_TILE_SIZE);
      tiles.add(sideTile);
    }

    // connect the tiles
    for (int i = 0; i < tiles.size(); i++) {
      if (i < tiles.size() - 1) {
        tiles.get(i).setNextTile(tiles.get(i + 1));
      }
    }
    // connect the last tile to the first tile
    tiles.get(tiles.size() - 1).setNextTile(tiles.get(0));

    return tiles;
  }
}
