package edu.ntnu.iir.bidata.model.games;

import edu.ntnu.iir.bidata.model.GameType;
import edu.ntnu.iir.bidata.model.Player;
import edu.ntnu.iir.bidata.model.Tile;
import edu.ntnu.iir.bidata.model.tileaction.GoToJailAction;
import edu.ntnu.iir.bidata.model.tileaction.JailAction;
import edu.ntnu.iir.bidata.model.tileaction.TileAction;

import java.util.*;

/**
 * Represents a Monopoly game implementation.
 * This class extends the Game class and includes Monopoly-specific functionality
 * such as money management, jail mechanics, and turn handling.
 */
public class MonopolyGame extends Game {
  /** Map to track the amount of money each player has. */
  private final Map<Player, Integer> playerMoney;
  
  /** Set to track which players are currently in jail. */
  private final Set<Player> playersInJail;
  
  /** The amount of money each player starts with. */
  private static final int STARTING_MONEY = 1500;
  
  /** The amount of money received when passing GO. */
  private static final int PASSING_GO_MONEY = 200;
  
  /** The amount of money needed to win the game. */
  private static final int WINNING_MONEY = 2000;

  /**
   * Constructs a new Monopoly game with default values.
   * Initializes the player money map and the players in jail set.
   */
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
   * @throws IllegalArgumentException if amount is not positive
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
   * @throws IllegalArgumentException if the player doesn't have enough money or amount is not positive
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
   * @param from   the player paying money
   * @param to     the player receiving money
   * @param amount the amount to transfer (must be positive)
   * @throws IllegalArgumentException if the paying player doesn't have enough money or amount is not positive
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
   * Adds the player to the jail set and updates their position.
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
   * Removes the player from the jail set.
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

  /**
   * Handles game events.
   * Currently handles only the dice rolled event.
   *
   * @param event the event to handle
   */
  @Override
  public void handleEvent(String event) {
    switch (event) {
      case "monopoly_dice_rolled":
        makeTurn();
        break;
    }
  }

  /**
   * Executes the turn logic for a player's turn.
   * Handles dice rolling, moving the player, jail mechanics, and tile actions.
   */
  private void turnLogic() {
        // roll dice
    this.dice.rollAll();
    int sum = this.dice.getSum();
    List<Integer> counts = this.dice.getCounts();
    boolean rolledEqual = Objects.equals(counts.get(0), counts.get(1));

    // move current player
    Player currentPlayer = getCurrentPlayer();

    boolean escapedJail = false;

    if (rolledEqual) {
      if (isInJail(currentPlayer)) {
        escapedJail = true;
      }
      releaseFromJail(currentPlayer);
    }

    if (isInJail(currentPlayer)) {
      incrementPlayerTurn();
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

    // Process tile action for the new position
    TileAction tileAction = getBoard().getTile(newPosition).getAction();
    if (tileAction instanceof GoToJailAction) {
      sendToJail(currentPlayer);
      incrementPlayerTurn();
      return;
    }

    // increment current player index
    if (!rolledEqual || escapedJail) {
      incrementPlayerTurn();
    }
  }

  /**
   * Makes a turn for the current player and processes the aftermath.
   */
  private void makeTurn() {
    Player playerThatMadeTurn = getCurrentPlayer();
    turnLogic();
    afterTurn(playerThatMadeTurn);
  }

  /**
   * Processes actions after a player's turn.
   * Checks if the player has won the game.
   *
   * @param playerThatMadeTurn the player that just completed their turn
   */
  private void afterTurn(Player playerThatMadeTurn) {
    if (getPlayerMoney(playerThatMadeTurn) >= WINNING_MONEY) {
      setWinner(playerThatMadeTurn);
    }
  }

  /**
   * Increments the current player index to move to the next player's turn.
   */
  private void incrementPlayerTurn() {
    setCurrentPlayerIndex((getCurrentPlayerIndex() + 1) % getPlayers().size());
  }

  /**
   * Starts the game by initializing player positions and giving each player starting money.
   * Ensures no players start in jail.
   */
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
