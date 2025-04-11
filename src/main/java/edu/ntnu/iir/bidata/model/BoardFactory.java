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
  public static Board randomSnakesAndLadders(int columns, int rows) {
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

    board.setName("Random board");
    colorSnakesAndLaddersBoard(board);
    return board;
  }

  public static Board normalSnakesAndLadders() {
    List<Tile> tiles = BoardTileLayout.snakesAndLadders(10, 10);
    Board board = new Board(tiles);

    // ladders
    addMoveAction(board, 2, 38);
    addMoveAction(board, 4, 14);
    addMoveAction(board, 8, 30);
    addMoveAction(board, 21, 42);
    addMoveAction(board, 28, 76);
    addMoveAction(board, 50, 67);
    addMoveAction(board, 71, 92);
    addMoveAction(board, 88, 99);

    // snakes
    addMoveAction(board, 34, 6);
    addMoveAction(board, 32, 10);
    addMoveAction(board, 48, 26);
    addMoveAction(board, 62, 18);
    addMoveAction(board, 88, 24);
    addMoveAction(board, 95, 56);

    board.setName("Normal board");
    colorSnakesAndLaddersBoard(board);
    return board;
  }

  public static Board chineseSnakesAndLadders() {
    int columns = 20, rows = 20;
    List<Tile> tiles = BoardTileLayout.snakesAndLadders(columns, rows);
    Board board = new Board(tiles);

    // snakes
    for (int i = 1; i < columns * rows; i++) {
      if ((i - 1) % 12 == 0) {
        continue;
      }
      addMoveAction(board, i, 1);
    }

    board.setName("Chinese board");
    colorSnakesAndLaddersBoard(board);
    return board;
  }

  private static void addMoveAction(Board board, int start, int end) {
    MoveAction action = new MoveAction(start, end);
    board.getTile(start).setAction(action);
  }

  public static Board standardMonopoly() {
    List<Tile> tiles = BoardTileLayout.monopoly();
    Board board = new Board(tiles);
    board.setName("Monopoly board");
    return board;
  }

  private static void colorSnakesAndLaddersBoard(Board board) {
    // color the snakes and ladders
    for (Tile tile : board.getTiles()) {
      if (tile.getAction() instanceof MoveAction) {
        MoveAction action = (MoveAction) tile.getAction();
        if (action.getStart() < action.getEnd()) {
          tile.getStyling().setColor("#228B22");
        } else {
          tile.getStyling().setColor("#ec4a27");
        }
      }
    }
  }
}
