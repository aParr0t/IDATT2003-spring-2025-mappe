package edu.ntnu.iir.bidata.model.games;

import edu.ntnu.iir.bidata.model.GameType;
import edu.ntnu.iir.bidata.model.Player;

public class SnakesAndLaddersGame extends Game {
  public SnakesAndLaddersGame() {
    super();
    gameType = GameType.SNAKES_AND_LADDERS;
  }

  @Override
  public boolean isGameOver() {
    return false;
  }

  @Override
  public void handleEvent(String event) {
    switch (event) {
      case "snakes_and_ladders_dice_rolled":
        makeTurn();
        break;
    }
  }

  private void makeTurn() {
    // roll dice
    this.dice.rollAll();
    int sum = this.dice.getSum();

    // move current player
    Player currentPlayer = getCurrentPlayer();
    int newPosition = Math.min(currentPlayer.getPosition() + sum, getBoard().getTiles().size());
    currentPlayer.setPosition(newPosition);

    // increment current player index
    setCurrentPlayerIndex((getCurrentPlayerIndex() + 1) % getPlayers().size());
  }

  @Override
  public void start() {
    // make players start on position 1
    var players = getPlayers();
    for (Player player : players) {
      player.setPosition(1);
    }
  }
}
