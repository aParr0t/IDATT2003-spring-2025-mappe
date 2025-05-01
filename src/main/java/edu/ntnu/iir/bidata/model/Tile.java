package edu.ntnu.iir.bidata.model;

import edu.ntnu.iir.bidata.model.tileaction.TileAction;
import javafx.geometry.Point2D;

/**
 * Represents a tile on the game board.
 * <p>
 * A tile is a fundamental element of the game board with a unique identifier,
 * position, dimensions, and an optional action that occurs when a player lands on it.
 * Tiles form a linked list where each tile points to the next tile in sequence.
 */
public class Tile {
  private final int id;
  private Tile nextTile;
  private Point2D position; // Position as x,y coordinates (0.0-1.0)
  private double width, height;
  private TileAction action;
  private TileStyling styling;

  /**
   * Creates a tile with a unique identifier.
   * <p>
   * Initializes the tile with default position at (0.0, 0.0) and default styling.
   *
   * @param id the tile's unique identifier
   */
  public Tile(int id) {
    this.id = id;
    this.nextTile = null;
    this.position = new Point2D(0.0, 0.0);
    this.styling = new TileStyling();
  }

  /**
   * Gets the styling information for this tile.
   *
   * @return the tile's styling object
   */
  public TileStyling getStyling() {
    return styling;
  }

  /**
   * Sets the styling information for this tile.
   *
   * @param styling the styling object to apply to this tile
   */
  public void setStyling(TileStyling styling) {
    this.styling = styling;
  }

  /**
   * Gets the tile's unique identifier.
   *
   * @return the tile ID
   */
  public int getId() {
    return id;
  }

  /**
   * Gets the next tile in sequence.
   *
   * @return the next tile, or null if this is the last tile
   */
  public Tile getNextTile() {
    return nextTile;
  }

  /**
   * Gets the position of this tile on the board.
   *
   * @return the tile's position as a Point2D object
   */
  public Point2D getPosition() {
    return position;
  }

  /**
   * Sets the position of this tile using a Point2D object.
   *
   * @param position the new position for this tile
   */
  public void setPosition(Point2D position) {
    this.position = position;
  }

  /**
   * Sets the position of this tile using x and y coordinates.
   *
   * @param x the x-coordinate (0.0-1.0)
   * @param y the y-coordinate (0.0-1.0)
   */
  public void setPosition(double x, double y) {
    this.position = new Point2D(x, y);
  }

  /**
   * Sets the next tile in sequence.
   *
   * @param nextTile the tile that follows this one
   */
  public void setNextTile(Tile nextTile) {
    this.nextTile = nextTile;
  }

  /**
   * Sets both width and height dimensions of this tile.
   *
   * @param width the width of the tile
   * @param height the height of the tile
   */
  public void setSize(double width, double height) {
    this.width = width;
    this.height = height;
  }

  /**
   * Sets the width of this tile.
   *
   * @param width the width to set
   */
  public void setWidth(double width) {
    this.width = width;
  }

  /**
   * Sets the height of this tile.
   *
   * @param height the height to set
   */
  public void setHeight(double height) {
    this.height = height;
  }

  /**
   * Gets the width of this tile.
   *
   * @return the tile's width
   */
  public double getWidth() {
    return width;
  }

  /**
   * Gets the height of this tile.
   *
   * @return the tile's height
   */
  public double getHeight() {
    return height;
  }

  /**
   * Sets the action that occurs when a player lands on this tile.
   *
   * @param action the tile action to set
   */
  public void setAction(TileAction action) {
    this.action = action;
  }

  /**
   * Gets the action associated with this tile.
   *
   * @return the tile's action, or null if no action is set
   */
  public TileAction getAction() {
    return action;
  }

  /**
   * Returns a string representation of this tile.
   *
   * @return a string containing the tile's ID and position
   */
  @Override
  public String toString() {
    return "Tile{" +
            "id=" + id +
            ", position=" + position +
            '}';
  }
}
