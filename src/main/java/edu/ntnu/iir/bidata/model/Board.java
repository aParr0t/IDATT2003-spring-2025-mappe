package edu.ntnu.iir.bidata.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the game board consisting of multiple tiles.
 */
public class Board {
  private String name = "Unnamed board";
  private Map<Integer, Tile> tiles;
  private double width;
  private double height;

  public Board() {
    this(new ArrayList<>());
  }

  public Board(List<Tile> tiles) {
    this.tiles = new HashMap<>();
    for (Tile tile : tiles) {
      this.tiles.put(tile.getId(), tile);
    }
    this.width = 1;
    this.height = 1;
  }

  public void setName(String newName) {
    name = newName;
  }

  public String getName() {
    return name;
  }

  public void addTile(Tile tile) {
    tiles.put(tile.getId(), tile);
  }

  public Tile getTile(int tileId) {
    return tiles.get(tileId);
  }

  public List<Tile> getTiles() {
    return new ArrayList<>(tiles.values());
  }

  public int getTileCount() {
    return tiles.size();
  }

  public double getWidth() {
    return width;
  }

  public double getHeight() {
    return height;
  }

  @Override
  public String toString() {
    return "Board{" +
            "tileCount=" + getTileCount() +
            ", width=" + width +
            ", height=" + height +
            '}';
  }
}
