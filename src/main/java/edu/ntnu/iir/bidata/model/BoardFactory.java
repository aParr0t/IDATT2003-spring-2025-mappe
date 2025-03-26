package edu.ntnu.iir.bidata.model;

import edu.ntnu.iir.bidata.model.TileAction.MoveAction;
import edu.ntnu.iir.bidata.utils.RandomMath;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory class responsible for creating different Board configurations.
 */
public class BoardFactory {

  /**
   * Creates an empty board with no tiles.
   *
   * @return An empty board
   */
  public static Board createEmptyBoard() {
    return new Board();
  }

  /**
   * Creates a snakes and ladders board with a random layout.
   *
   * @return A board with standard configuration
   */
  public static Board createRandomSnakesAndLaddersBoard(int columns, int rows) {
    // the random generation doesn't work for columns < 3 and rows < 3
    if (columns < 3 || rows < 3) {
      throw new IllegalArgumentException("Columns and rows must be at least 3.");
    }

    List<Tile> tiles = BoardTileLayout.snakesAndLadders(columns, rows);
    Board board = new Board(tiles);

    // create a list of positions not yet used by a ladder or snake
    List<Integer> freePositions = new ArrayList<>();
    for (int i = 1; i <= columns * rows - 1; i++) {
      freePositions.add(i);
    }

    int ladderCount = 5, snakeCount = 5;
    // generate ladders
    for (int i = 0; i < ladderCount; i++) {
      int start = RandomMath.randomPick(freePositions.stream().filter(
              p -> 1 < p && p < columns * (rows - 1)
      ).toList()).orElse(-1);
      int end = RandomMath.randomPick(freePositions.stream().filter(
              p -> (start - start % columns) + columns < p && p < columns * rows - 1
      ).toList()).orElse(-1);

      // if no valid positions are found, skip this ladder
      if (start == -1 || end == -1) {
        continue;
      }

      // create the ladder action
      MoveAction ladderAction = new MoveAction(start, end);
      board.getTile(start).setAction(ladderAction);

      // remove the used positions from the list
      freePositions.remove(Integer.valueOf(start));
      freePositions.remove(Integer.valueOf(end));
    }

    // snakes
    for (int i = 0; i < snakeCount; i++) {
      int start = RandomMath.randomPick(freePositions.stream().filter(
              p -> columns + 1 < p && p < columns * rows - 1
      ).toList()).orElse(-1);
      int end = RandomMath.randomPick(freePositions.stream().filter(
              p -> 0 < p && p < start - start % columns
      ).toList()).orElse(-1);

      // if no valid positions are found, skip this ladder
      if (start == -1 || end == -1) {
        continue;
      }

      // create the snake action
      MoveAction snakeAction = new MoveAction(start, end);
      board.getTile(start).setAction(snakeAction);

      // remove the used positions from the list
      freePositions.remove(Integer.valueOf(start));
      freePositions.remove(Integer.valueOf(end));
    }

    return board;
  }
}
