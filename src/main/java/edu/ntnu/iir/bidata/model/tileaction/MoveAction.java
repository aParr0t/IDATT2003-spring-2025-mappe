package edu.ntnu.iir.bidata.model.tileaction;

/**
 * Represents an action that moves a player from one tile position to another.
 * This class implements the TileAction interface and provides functionality
 * for defining movement between board positions.
 */
public class MoveAction implements TileAction {
  private final int start;
  private final int end;

  /**
   * Constructs a new MoveAction with specified start and end positions.
   *
   * @param start the starting tile position
   * @param end the destination tile position
   */
  public MoveAction(int start, int end) {
    this.start = start;
    this.end = end;
  }

  /**
   * Gets the starting position of this move action.
   *
   * @return the starting tile position
   */
  public int getStart() {
    return start;
  }

  /**
   * Gets the ending position of this move action.
   *
   * @return the destination tile position
   */
  public int getEnd() {
    return end;
  }
}
