package edu.ntnu.iir.bidata.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Manages the main game logic.
 */
public class BoardGame {
  private Board board;
  private List<Player> players;
  private Dice dice;
  private boolean isGameOver;
  private String gameType;  // temporary

  /**
   * Initializes the board game with a given board, players, and dice.
   */
  public BoardGame() {
    this.isGameOver = false;
  }

  public void setBoard(Board board) {
    this.board = board;
  }

  public void setGameType(String gameType) {
    // temporary method
    this.gameType = gameType;
  }

  public PlayerConfigResponse isPlayerConfigOk(List<Player> players) {
    // Checks if all players have unique playing pieces using a HashSet
    HashSet<PlayingPieceType> pieceTypes = new HashSet<>();
    for (Player player : players) {
      pieceTypes.add(player.getPlayingPiece().getType());
    }
    // If the set size equals the player count, all pieces are unique
    boolean areAllUnique = pieceTypes.size() == players.size();
    if (areAllUnique) {
      return new PlayerConfigResponse(true, null);
    } else {
      return new PlayerConfigResponse(false, "All players must have unique playing pieces.");
    }
  }

  public void setPlayers(List<Player> players) {
    this.players = players;
  }

  public List<Player> getPlayers() {
    return players;
  }

  public List<PlayingPiece> getAllPlayingPieces() {
    return Arrays.stream(PlayingPieceType.values()).map(PlayingPiece::new).toList();
  }

  public void setDice(Dice dice) {
    this.dice = dice;
  }

  /**
   * Starts the game and prints the results to the console.
   */
  public void startGame() {
    int round = 1;
    while (!isGameOver) {
      System.out.println("\nRunde " + round); // Fjerner eventuelle spesialtegn

      for (Player player : players) {
        int roll = dice.rollAll();

        // Skriv ut kastet på terningen
        System.out.println(player.getName() + " kastet " + roll + " på terningene.");

        // Beveg spilleren
        player.move(roll);

        // Skriv ut spillerens nye posisjon
        System.out.println(player.getName() + " flyttet til rute " + player.getPosition() + ".");

        // Sjekk om spilleren har vunnet
        if (player.getPosition() >= board.getTileCount() - 1) {
          System.out.println("\n" + player.getName() + " har nådd siste rute og vant spillet!");
          isGameOver = true;
          return; // Avslutt spillet umiddelbart etter en vinner er funnet
        }
      }
      round++;
    }
  }
}
