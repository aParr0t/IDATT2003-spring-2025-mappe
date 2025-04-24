package edu.ntnu.iir.bidata.model.games;

import edu.ntnu.iir.bidata.model.GameType;

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
    System.out.println("Handling event: " + event);
  }

  @Override
  public void start() {

  }
}
