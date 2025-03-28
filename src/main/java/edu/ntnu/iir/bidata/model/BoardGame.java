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
  private GameType gameType;
  private int playerTurn;

  /**
   * Initializes the board game with a given board, players, and dice.
   */
  public BoardGame() {
    this.isGameOver = false;
  }

  public void setBoard(Board board) {
    this.board = board;
  }

  public Board getBoard() {
    return board;
  }

  public List<Board> getAllBoardsForGameType(GameType gameType) {
    switch (gameType) {
      case GameType.SNAKES_AND_LADDERS:
        return List.of(
                BoardFactory.randomSnakesAndLadders(10, 10),
                BoardFactory.normalSnakesAndLadders(),
                BoardFactory.chineseSnakesAndLadders()
        );
      case GameType.MONOPOLY:
        return List.of(
                BoardFactory.createEmptyBoard()
        );
      default:
        return List.of();
    }
  }

  public void setGameType(GameType gameType) {
    this.gameType = gameType;
  }

  public GameType getGameType() {
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

  public void setPlayers(List<Player> players) {
    this.players = players;
  }

  public List<Player> getPlayers() {
    return players;
  }

  public List<PlayingPiece> getAllPlayingPieces() {
    return Arrays.stream(PlayingPieceType.values()).map(PlayingPiece::new).toList();
  }

  private void configureRestOfGame() {
    // These are hardcoded configs for each game type.
    // This could be refactored, but there are limits to over-engineering :)
    switch (gameType) {
      case GameType.SNAKES_AND_LADDERS:
      case GameType.MONOPOLY: {
        this.dice = new Dice(2, 6);
        break;
      }
      default:
        this.dice = new Dice(1, 6);
        break;
    }
  }

  /**
   * Starts the game and prints the results to the console.
   */
  public void startGame() {
    System.out.println("BoardGame started");
    configureRestOfGame();
    this.playerTurn = 0;
  }

  public void makeTurn() {
    dice.rollAll();
    Player currentPlayer = players.get(playerTurn);
    List<Integer> diceCounts = dice.getCounts();
    int diceSum = diceCounts.stream().mapToInt(Integer::intValue).sum();

    // get the tile that the player would land on
    Tile newPlayerTile = board.getTile(currentPlayer.getPosition());
    for (int i = 0; i < diceSum; i++) {
      if (newPlayerTile.getNextTile() == null) {
        break;
      }
      newPlayerTile = newPlayerTile.getNextTile();
    }

    // move the player to the new tile
    currentPlayer.setPosition(newPlayerTile.getId());

    playerTurn = (playerTurn + 1) % players.size();
  }

  public List<Integer> getDiceCounts() {
    return dice.getCounts();
  }

  public Player getCurrentPlayerTurn() {
    return players.get(playerTurn);
  }
}
