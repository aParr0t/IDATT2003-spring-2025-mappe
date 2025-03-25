package edu.ntnu.iir.bidata.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the game board consisting of multiple tiles.
 */
public class Board {
  private final List<Tile> tiles;
  private double width;
  private double height;

  public Board() {
    this.tiles = new ArrayList<>();
    this.width = 1;
    this.height = 1;
  }

  public Board(List<Tile> tiles) {
    this.tiles = tiles;
  }

  public void addTile(Tile tile) {
    tiles.add(tile);
  }

  public Tile getTile(int tileId) {
    return tiles.get(tileId);
  }

  public List<Tile> getTiles() {
    return tiles;
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
