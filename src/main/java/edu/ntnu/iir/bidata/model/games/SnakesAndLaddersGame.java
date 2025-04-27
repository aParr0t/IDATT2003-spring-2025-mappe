package edu.ntnu.iir.bidata.model.games;

import edu.ntnu.iir.bidata.model.GameType;
import edu.ntnu.iir.bidata.model.Player;

public class SnakesAndLaddersGame extends Game {
  public SnakesAndLaddersGame() {
    super();
    gameType = GameType.SNAKES_AND_LADDERS;
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
    Player playerThatMadeTurn = getCurrentPlayer();
    turnLogic();
    afterTurn(playerThatMadeTurn);
  }

  private void turnLogic() {
    // roll dice
    this.dice.rollAll();
    int sum = this.dice.getSum();

    // move current player
    Player currentPlayer = getCurrentPlayer();
    int numberOfTiles = getBoard().getTiles().size();
    int newPosition = Math.min(currentPlayer.getPosition() + sum, numberOfTiles);
    currentPlayer.setPosition(newPosition);

    // increment current player index
    setCurrentPlayerIndex((getCurrentPlayerIndex() + 1) % getPlayers().size());
  }

  private void afterTurn(Player playerThatMadeTurn) {
    if (playerThatMadeTurn.getPosition() == getBoard().getTiles().size()) {
      setWinner(playerThatMadeTurn);
    }
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
