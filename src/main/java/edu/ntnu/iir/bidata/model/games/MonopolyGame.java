package edu.ntnu.iir.bidata.model.games;

import edu.ntnu.iir.bidata.model.GameType;
import edu.ntnu.iir.bidata.model.Player;

public class MonopolyGame extends Game {
  public MonopolyGame() {
    super();
    gameType = GameType.MONOPOLY;
  }

  @Override
  public boolean isGameOver() {
    return false;
  }

  @Override
  public void handleEvent(String event) {
    switch (event) {
      case "monopoly_dice_rolled":
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
    int newPosition = (currentPlayer.getPosition() + sum) % getBoard().getTiles().size();
    currentPlayer.setPosition(newPosition);

    // increment current player index
    setCurrentPlayerIndex((getCurrentPlayerIndex() + 1) % getPlayers().size());
  }

  public void start() {
    // make players start on position 0
    var players = getPlayers();
    for (Player player : players) {
      player.setPosition(0);
    }
  }
}
