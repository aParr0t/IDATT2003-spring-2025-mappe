package edu.ntnu.iir.bidata.view.GUI;

import edu.ntnu.iir.bidata.model.Board;
import edu.ntnu.iir.bidata.model.GameType;
import edu.ntnu.iir.bidata.view.GUI.games.MonopolyBoard;
import edu.ntnu.iir.bidata.view.GUI.games.SnakesAndLaddersBoard;

import java.util.HashMap;
import java.util.function.Function;

public class BoardCanvasFactory {
  private static final HashMap<GameType, Function<Board, BoardCanvas>> boardFactories = new HashMap<>() {{
    put(GameType.SNAKES_AND_LADDERS, SnakesAndLaddersBoard::new);
    put(GameType.MONOPOLY, MonopolyBoard::new);
  }};

  /**
   * Creates a BoardCanvas instance for the given game type and board
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
