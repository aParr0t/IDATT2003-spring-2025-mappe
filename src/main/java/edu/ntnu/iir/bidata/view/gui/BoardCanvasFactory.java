package edu.ntnu.iir.bidata.view.gui;

import edu.ntnu.iir.bidata.model.Board;
import edu.ntnu.iir.bidata.model.GameType;
import edu.ntnu.iir.bidata.view.gui.games.MonopolyBoard;
import edu.ntnu.iir.bidata.view.gui.games.SnakesAndLaddersBoard;
import java.util.HashMap;
import java.util.function.Function;

/**
 * Factory class for creating board canvas instances based on game type.
 * This factory uses a mapping of game types to board creation functions
 * to instantiate the appropriate board canvas for a given game type.
 */
public class BoardCanvasFactory {
  /**
   * Map of game types to their corresponding board creation functions.
   * Each function takes a Board instance and returns the appropriate BoardCanvas
   * implementation for that game type.
   */
  private static HashMap<GameType, Function<Board, BoardCanvas>> boardFactories;

  static {
    boardFactories = new HashMap<>() {
      {
        put(GameType.SNAKES_AND_LADDERS, SnakesAndLaddersBoard::new);
        put(GameType.MONOPOLY, MonopolyBoard::new);
      }
    };
  }

  /**
   * Creates a BoardCanvas instance for the given game type and board.
   *
   * @param gameType the type of game
   * @param board    the board configuration
   * @return a BoardCanvas instance for the specified game type
   * @throws IllegalArgumentException if the game type is not supported
   */
  public static BoardCanvas createBoardCanvas(GameType gameType, Board board) {
    Function<Board, BoardCanvas> factory = boardFactories.get(gameType);
    if (factory == null) {
      throw new IllegalArgumentException("Unsupported game type: " + gameType);
    }
    return factory.apply(board);
  }
}
