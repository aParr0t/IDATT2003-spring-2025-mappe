package edu.ntnu.iir.bidata.model.games;

import edu.ntnu.iir.bidata.model.GameType;
import edu.ntnu.iir.bidata.model.Player;
import edu.ntnu.iir.bidata.model.tileaction.MoveAction;
import edu.ntnu.iir.bidata.model.tileaction.TileAction;

/**
 * Represents a Snakes and Ladders game implementation.
 * This class handles the game logic specific to Snakes and Ladders,
 * including player movement, dice rolling, and win conditions.
 */
public class SnakesAndLaddersGame extends Game {
  /**
   * Constructs a new Snakes and Ladders game.
   * Sets the game type to SNAKES_AND_LADDERS.
   */
  public SnakesAndLaddersGame() {
    super();
    gameType = GameType.SNAKES_AND_LADDERS;
  }

  /**
   * Handles game events for Snakes and Ladders.
   *
   * @param event the event to be handled, as a string identifier
   */
  @Override
  public void handleEvent(String event) {
    switch (event) {
      case "snakes_and_ladders_dice_rolled":
        makeTurn();
        break;
    }
  }

  /**
   * Executes a player's turn.
   * Gets the current player, executes turn logic, and checks for game-ending conditions.
   */
  private void makeTurn() {
    Player playerThatMadeTurn = getCurrentPlayer();
    turnLogic();
    afterTurn(playerThatMadeTurn);
  }

  /**
   * Implements the core turn logic for Snakes and Ladders.
   * Rolls dice, moves the player, handles special tile actions (snakes/ladders),
   * and advances to the next player.
   */
  private void turnLogic() {
    // roll dice
    this.dice.rollAll();
    int sum = this.dice.getSum();

    // move current player
    Player currentPlayer = getCurrentPlayer();
    int numberOfTiles = getBoard().getTiles().size();
    int newPosition = Math.min(currentPlayer.getPosition() + sum, numberOfTiles);
    currentPlayer.setPosition(newPosition);

    // check if player landed on a snake or ladder
    TileAction tileAction = getBoard().getTile(newPosition).getAction();
    if (tileAction instanceof MoveAction) {
      currentPlayer.setPosition(((MoveAction) tileAction).getEnd());
    }

    // increment current player index
    setCurrentPlayerIndex((getCurrentPlayerIndex() + 1) % getPlayers().size());
  }

  /**
   * Performs post-turn operations.
   * Checks if the player who just took a turn has won the game.
   *
   * @param playerThatMadeTurn the player who just completed their turn
   */
  private void afterTurn(Player playerThatMadeTurn) {
    if (playerThatMadeTurn.getPosition() == getBoard().getTiles().size()) {
      setWinner(playerThatMadeTurn);
    }
  }

  /**
   * Initializes the game by setting all players to the starting position.
   * In Snakes and Ladders, all players start at position 1.
   */
  @Override
  public void start() {
    // make players start on position 1
    var players = getPlayers();
    for (Player player : players) {
      player.setPosition(1);
    }
  }
}
