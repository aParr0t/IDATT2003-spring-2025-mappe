package edu.ntnu.iir.bidata.model;

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
    List<Tile> tiles = BoardTileLayout.snakesAndLadders(columns, rows);
    Board board = new Board(tiles);

    // TODO add snakes and ladders

    return board;
  }
}
