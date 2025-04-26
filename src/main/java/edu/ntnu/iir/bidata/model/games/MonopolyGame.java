package edu.ntnu.iir.bidata.model.games;

import edu.ntnu.iir.bidata.model.GameType;
import edu.ntnu.iir.bidata.model.Player;
import edu.ntnu.iir.bidata.model.Tile;
import edu.ntnu.iir.bidata.model.tileaction.GoToJailAction;
import edu.ntnu.iir.bidata.model.tileaction.JailAction;
import edu.ntnu.iir.bidata.model.tileaction.TileAction;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MonopolyGame extends Game {
  private final Map<Player, Integer> playerMoney;
  private final Set<Player> playersInJail;
  private static final int STARTING_MONEY = 1500;
  private static final int PASSING_GO_MONEY = 200;

  public MonopolyGame() {
    super();
    gameType = GameType.MONOPOLY;
    playerMoney = new HashMap<>();
    playersInJail = new HashSet<>();
  }

  /**
   * Gets the current money amount for a player.
   * 
   * @param player the player to check
   * @return the amount of money the player has
   */
  public int getPlayerMoney(Player player) {
    return playerMoney.getOrDefault(player, 0);
  }

  /**
   * Adds money to a player's balance.
   * 
   * @param player the player to give money to
   * @param amount the amount to add (must be positive)
   * @return the new balance
   */
  public int addMoney(Player player, int amount) {
    if (amount <= 0) {
      throw new IllegalArgumentException("Amount must be positive");
    }
    
    int currentAmount = playerMoney.getOrDefault(player, 0);
    int newAmount = currentAmount + amount;
    playerMoney.put(player, newAmount);
    return newAmount;
  }

  /**
   * Removes money from a player's balance.
   * 
   * @param player the player to take money from
   * @param amount the amount to remove (must be positive)
   * @return the new balance
   * @throws IllegalArgumentException if the player doesn't have enough money
   */
  public int removeMoney(Player player, int amount) {
    if (amount <= 0) {
      throw new IllegalArgumentException("Amount must be positive");
    }
    
    int currentAmount = playerMoney.getOrDefault(player, 0);
    if (currentAmount < amount) {
      throw new IllegalArgumentException("Player doesn't have enough money");
    }
    
    int newAmount = currentAmount - amount;
    playerMoney.put(player, newAmount);
    return newAmount;
  }

  /**
   * Transfers money from one player to another.
   * 
   * @param from the player paying money
   * @param to the player receiving money
   * @param amount the amount to transfer (must be positive)
   * @throws IllegalArgumentException if the paying player doesn't have enough money
   */
  public void transferMoney(Player from, Player to, int amount) {
    removeMoney(from, amount);
    addMoney(to, amount);
  }

  /**
   * Finds the jail tile position in the board.
   *
   * @return the position of the jail tile, or -1 if not found
   */
  public int findJailPosition() {
    List<Tile> tiles = getBoard().getTiles();
    for (int i = 0; i < tiles.size(); i++) {
      TileAction action = tiles.get(i).getAction();
      if (action instanceof JailAction) {
        return i;
      }
    }
    return -1;
  }

  /**
   * Sends a player to jail.
   *
   * @param player the player to send to jail
   */
  public void sendToJail(Player player) {
    playersInJail.add(player);
    int jailPosition = findJailPosition();
      
    if (jailPosition != -1) {
      player.setPosition(jailPosition);
    }
  }

  /**
   * Releases a player from jail.
   *
   * @param player the player to release from jail
   */
  public void releaseFromJail(Player player) {
    playersInJail.remove(player);
  }

  /**
   * Checks if a player is in jail.
   *
   * @param player the player to check
   * @return true if the player is in jail, false otherwise
   */
  public boolean isInJail(Player player) {
    return playersInJail.contains(player);
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
    sum = 1;

    // move current player
    Player currentPlayer = getCurrentPlayer();
    
    // Check if player is in jail
    if (isInJail(currentPlayer)) {
      // Handle jail turn logic here
      // For now, we'll just skip their turn
      setCurrentPlayerIndex((getCurrentPlayerIndex() + 1) % getPlayers().size());
      return;
    }
    
    int oldPosition = currentPlayer.getPosition();
    int newPosition = (oldPosition + sum) % getBoard().getTiles().size();
    
    // Check if player passed GO (tile 0)
    if (oldPosition > 0 && (newPosition == 0 || newPosition < oldPosition)) {
      // Player passed GO, add $200
      addMoney(currentPlayer, PASSING_GO_MONEY);
    }
    
    currentPlayer.setPosition(newPosition);
    
    // Check if the new position has a GoToJailAction
    TileAction tileAction = getBoard().getTile(newPosition).getAction();
    if (tileAction instanceof GoToJailAction) {
      sendToJail(currentPlayer);
    }

    // increment current player index
    setCurrentPlayerIndex((getCurrentPlayerIndex() + 1) % getPlayers().size());
  }

  @Override
  public void start() {
    // make players start on position 0
    var players = getPlayers();
    for (Player player : players) {
      player.setPosition(0);
      // Give each player the starting amount of money
      playerMoney.put(player, STARTING_MONEY);
      // Ensure no players start in jail
      playersInJail.remove(player);
    }
  }
}
