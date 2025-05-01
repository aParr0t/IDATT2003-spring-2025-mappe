package edu.ntnu.iir.bidata.model.games;

import edu.ntnu.iir.bidata.model.GameType;

/**
 * Factory for creating game instances based on game type.
 * This follows the Factory Pattern to encapsulate game creation logic.
 */
public class GameFactory {

  /**
   * Creates a new game instance based on the specified game type.
   *
   * @param gameType the type of game to create
   * @return a new game instance
   * @throws IllegalArgumentException if the game type is not supported
   */
  public static Game createGame(GameType gameType) {
    if (gameType == null) {
      throw new IllegalArgumentException("Game type cannot be null");
    }

    return switch (gameType) {
      case SNAKES_AND_LADDERS -> new SnakesAndLaddersGame();
      case MONOPOLY -> new MonopolyGame();
    };
  }
}
