package edu.ntnu.iir.bidata.model.games;

import edu.ntnu.iir.bidata.model.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public abstract class Game {
  private Board board;
  private List<Player> players;
  protected Dice dice;
  protected GameType gameType;
  protected int currentPlayerIndex;
  protected Player winner;
  private int maxPlayers = 4;

  public Game() {
    this.dice = new Dice(2, 6);
  }

  public int getMaxPlayers() {
    return maxPlayers;
  }

  protected void setMaxPlayers(int maxPlayers) {
    this.maxPlayers = maxPlayers;
  }

  protected void validateGameType() {
    if (gameType == null) {
      throw new IllegalArgumentException("Game type cannot be null");
    }
  }

  public int getCurrentPlayerIndex() {
    return currentPlayerIndex;
  }

  public void setCurrentPlayerIndex(int currentPlayerIndex) {
    this.currentPlayerIndex = currentPlayerIndex;
  }

  public Player getCurrentPlayer() {
    return players.get(currentPlayerIndex);
  }

  public Board getBoard() {
    return board;
  }

  public void setBoard(Board board) {
    this.board = board;
  }

  public void setPlayers(List<Player> players) {
    this.players = players;
  }

  public List<Player> getPlayers() {
    return players;
  }

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

  public Player getWinner() {
    return winner;
  }

  protected void setWinner(Player winner) {
    this.winner = winner;
  }

  public List<PlayingPiece> getAllPlayingPieces() {
    return Arrays.stream(PlayingPieceType.values()).map(PlayingPiece::new).toList();
  }

  public GameType getGameType() {
    if (gameType == null) {
      throw new IllegalStateException("Game type is not set");
    }
    return gameType;
  }


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

  public boolean isGameOver() {
    return winner != null;
  }

  public abstract void handleEvent(String event);

  public abstract void start();
}
