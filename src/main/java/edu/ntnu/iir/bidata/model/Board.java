package edu.ntnu.iir.bidata.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the game board consisting of multiple tiles.
 * A board has dimensions, a name, and contains a collection of tiles indexed by their IDs.
 */
public class Board {
  private String name = "Unnamed board";
  private final Map<Integer, Tile> tiles;
  private final double width;
  private final double height;

  /**
   * Creates a new empty board with default dimensions.
   */
  public Board() {
    this(new ArrayList<>());
  }

  /**
   * Creates a new board with the specified tiles and default dimensions.
   *
   * @param tiles List of tiles to add to the board
   */
  public Board(List<Tile> tiles) {
    this.tiles = new HashMap<>();
    for (Tile tile : tiles) {
      this.tiles.put(tile.getId(), tile);
    }
    this.width = 1;
    this.height = 1;
  }

  /**
   * Sets the name of the board.
   *
   * @param newName The new name for the board
   */
  public void setName(String newName) {
    name = newName;
  }

  /**
   * Gets the name of the board.
   *
   * @return The board's name
   */
  public String getName() {
    return name;
  }

  /**
   * Adds a tile to the board.
   *
   * @param tile The tile to add to the board
   */
  public void addTile(Tile tile) {
    tiles.put(tile.getId(), tile);
  }

  /**
   * Retrieves a specific tile by its ID.
   *
   * @param tileId The ID of the tile to retrieve
   * @return The tile with the specified ID, or null if no such tile exists
   */
  public Tile getTile(int tileId) {
    return tiles.get(tileId);
  }

  /**
   * Gets all tiles on the board.
   *
   * @return A list containing all tiles on the board
   */
  public List<Tile> getTiles() {
    return new ArrayList<>(tiles.values());
  }

  /**
   * Gets the number of tiles on the board.
   *
   * @return The number of tiles
   */
  public int getTileCount() {
    return tiles.size();
  }

  /**
   * Gets the width of the board.
   *
   * @return The board width
   */
  public double getWidth() {
    return width;
  }

  /**
   * Gets the height of the board.
   *
   * @return The board height
   */
  public double getHeight() {
    return height;
  }

  /**
   * Returns a string representation of the board.
   *
   * @return A string containing the tile count and board dimensions
   */
  @Override
  public String toString() {
    return "Board{" +
            "tileCount=" + getTileCount() +
            ", width=" + width +
            ", height=" + height +
            '}';
  }
}
