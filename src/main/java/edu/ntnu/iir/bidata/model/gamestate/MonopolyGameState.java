package edu.ntnu.iir.bidata.model.gamestate;

import edu.ntnu.iir.bidata.model.Player;

import java.util.Collections;
import java.util.List;

public class MonopolyGameState {
  private List<Player> players;
  private int currentPlayerIndex;
  private List<Integer> playerMoney;

  public MonopolyGameState(List<Player> players) {
    this.players = players;
    this.currentPlayerIndex = 0;
    this.playerMoney = initializePlayerMoney(players.size());
  }

  private List<Integer> initializePlayerMoney(int numberOfPlayers) {
    int STARTING_MONEY = 1500;
    return Collections.nCopies(numberOfPlayers, STARTING_MONEY);  // (Help from AI: didn't know about nCopies)
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

  public List<Integer> getPlayerMoney() {
    return playerMoney;
  }

  public void setPlayerMoney(List<Integer> playerMoney) {
    this.playerMoney = playerMoney;
  }
}
