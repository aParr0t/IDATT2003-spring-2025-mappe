package edu.ntnu.iir.bidata.model;

import edu.ntnu.iir.bidata.model.TileAction.TileAction;
import javafx.geometry.Point2D;

/**
 * Represents a tile on the game board.
 */
public class Tile {
  private final int id;
  private Tile nextTile;
  private Point2D position; // Position as x,y coordinates (0.0-1.0)
  private double width, height;
  private TileAction action;

  /**
   * Creates a tile with a unique identifier and position.
   *
   * @param id the tile's unique identifier
   */
  public Tile(int id) {
    this.id = id;
    this.nextTile = null;
    this.position = new Point2D(0.0, 0.0);
  }

  /**
   * Gets the tile's unique identifier.
   *
   * @return the tile ID
   */
  public int getId() {
    return id;
  }

  public Tile getNextTile() {
    return nextTile;
  }

  /**
   * Gets the position of this tile on the board.
   *
   * @return the tile's position
   */
  public Point2D getPosition() {
    return position;
  }

  public void setPosition(Point2D position) {
    this.position = position;
  }

  public void setPosition(double x, double y) {
    this.position = new Point2D(x, y);
  }

  public void setNextTile(Tile nextTile) {
    this.nextTile = nextTile;
  }

  public void setSize(double width, double height) {
    this.width = width;
    this.height = height;
  }

  public void setWidth(double width) {
    this.width = width;
  }

  public void setHeight(double height) {
    this.height = height;
  }

  public double getWidth() {
    return width;
  }

  public double getHeight() {
    return height;
  }

  public void setAction(TileAction action) {
    this.action = action;
  }

  public TileAction getAction() {
    return action;
  }

  @Override
  public String toString() {
    return "Tile{" +
            "id=" + id +
            ", position=" + position +
            '}';
  }
}
