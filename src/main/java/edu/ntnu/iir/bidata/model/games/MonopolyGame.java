package edu.ntnu.iir.bidata.model.games;

import edu.ntnu.iir.bidata.model.GameType;

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
    // Handle Monopoly-specific events here
    System.out.println("Handling event: " + event);
  }
}
