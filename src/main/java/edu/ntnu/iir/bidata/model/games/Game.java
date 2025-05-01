package edu.ntnu.iir.bidata.model.games;

import edu.ntnu.iir.bidata.model.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Abstract base class for all games in the application.
 * Provides common game functionality such as managing players, board, dice, 
 * and basic game state. Specific game implementations should extend this class
 * and provide game-specific rules and behaviors.
 */
public abstract class Game {
  private Board board;
  private List<Player> players;
  protected Dice dice;
  protected GameType gameType;
  protected int currentPlayerIndex;
  protected Player winner;
  private int maxPlayers = 4;

  /**
   * Default constructor for Game.
   * Initializes the game with two six-sided dice.
   */
  public Game() {
    this.dice = new Dice(2, 6);
  }

  /**
   * Gets the maximum number of players allowed in the game.
   *
   * @return the maximum number of players
   */
  public int getMaxPlayers() {
    return maxPlayers;
  }

  /**
   * Sets the maximum number of players allowed in the game.
   *
   * @param maxPlayers the maximum number of players
   */
  protected void setMaxPlayers(int maxPlayers) {
    this.maxPlayers = maxPlayers;
  }

  /**
   * Validates that the game type is properly set.
   *
   * @throws IllegalArgumentException if game type is null
   */
  protected void validateGameType() {
    if (gameType == null) {
      throw new IllegalArgumentException("Game type cannot be null");
    }
  }

  /**
   * Gets the index of the current player in the players list.
   *
   * @return the current player index
   */
  public int getCurrentPlayerIndex() {
    return currentPlayerIndex;
  }

  /**
   * Sets the index of the current player in the players list.
   *
   * @param currentPlayerIndex the index to set
   */
  public void setCurrentPlayerIndex(int currentPlayerIndex) {
    this.currentPlayerIndex = currentPlayerIndex;
  }

  /**
   * Gets the current player whose turn it is.
   *
   * @return the current player
   */
  public Player getCurrentPlayer() {
    return players.get(currentPlayerIndex);
  }

  /**
   * Gets the game board.
   *
   * @return the game board
   */
  public Board getBoard() {
    return board;
  }

  /**
   * Sets the game board.
   *
   * @param board the board to set
   */
  public void setBoard(Board board) {
    this.board = board;
  }

  /**
   * Sets the list of players participating in the game.
   *
   * @param players the list of players
   */
  public void setPlayers(List<Player> players) {
    this.players = players;
  }

  /**
   * Gets the list of players participating in the game.
   *
   * @return the list of players
   */
  public List<Player> getPlayers() {
    return players;
  }

  /**
   * Gets the current dice roll values.
   *
   * @return a list of dice values
   */
  public List<Integer> getDiceCounts() {
    return dice.getCounts();
  }
  
  /**
   * Sets the dice to be used in the game.
   * This is useful for testing with mock dice.
   *
   * @param dice the dice to use
   */
  public void setDice(Dice dice) {
    this.dice = dice;
  }

  /**
   * Gets the winner of the game, if there is one.
   *
   * @return the winner player, or null if no winner yet
   */
  public Player getWinner() {
    return winner;
  }

  /**
   * Sets the winner of the game.
   *
   * @param winner the player who won the game
   */
  protected void setWinner(Player winner) {
    this.winner = winner;
  }

  /**
   * Gets all available playing pieces for the game.
   *
   * @return a list of all playing pieces
   */
  public List<PlayingPiece> getAllPlayingPieces() {
    return Arrays.stream(PlayingPieceType.values()).map(PlayingPiece::new).toList();
  }

  /**
   * Gets the type of this game.
   *
   * @return the game type
   * @throws IllegalStateException if game type is not set
   */
  public GameType getGameType() {
    if (gameType == null) {
      throw new IllegalStateException("Game type is not set");
    }
    return gameType;
  }

  /**
   * Validates player configuration, ensuring all players have unique playing pieces.
   *
   * @param players the list of players to validate
   * @return a response indicating if the configuration is valid and an error message if not
   */
  public PlayerConfigResponse isPlayerConfigOk(List<Player> players) {
    // Checks if all players have unique playing pieces using a HashSet
    HashSet<PlayingPieceType> pieceTypes = new HashSet<>();
    for (Player player : players) {
      PlayingPiece playingPiece = player.getPlayingPiece();
      if (playingPiece == null) {
        return new PlayerConfigResponse(false, "All players must have a playing piece.");
      }
      pieceTypes.add(playingPiece.getType());
    }
    // If the set size equals the player count, all pieces are unique
    boolean areAllUnique = pieceTypes.size() == players.size();
    if (areAllUnique) {
      return new PlayerConfigResponse(true, null);
    } else {
      return new PlayerConfigResponse(false, "All players must have unique playing pieces.");
    }
  }

  /**
   * Checks if the game is over.
   *
   * @return true if the game is over, false otherwise
   */
  public boolean isGameOver() {
    return winner != null;
  }

  /**
   * Handles game events.
   *
   * @param event the event to handle
   */
  public abstract void handleEvent(String event);

  /**
   * Starts the game.
   */
  public abstract void start();
}
