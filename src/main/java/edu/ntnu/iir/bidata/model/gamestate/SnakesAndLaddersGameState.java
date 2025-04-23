package edu.ntnu.iir.bidata.model.gamestate;

import edu.ntnu.iir.bidata.model.Player;

import java.util.List;

public class SnakesAndLaddersGameState {
  private int currentPlayerIndex;
  private List<Player> players;

  public SnakesAndLaddersGameState(List<Player> players) {
    this.players = players;
    this.currentPlayerIndex = 0;
  }

  public int getCurrentPlayerIndex() {
    return currentPlayerIndex;
  }

  public void setCurrentPlayerIndex(int currentPlayerIndex) {
    this.currentPlayerIndex = currentPlayerIndex;
  }

  public List<Player> getPlayers() {
    return players;
  }

  public void setPlayers(List<Player> players) {
    this.players = players;
  }
}
