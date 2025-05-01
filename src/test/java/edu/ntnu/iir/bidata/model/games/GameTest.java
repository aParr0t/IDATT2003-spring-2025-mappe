package edu.ntnu.iir.bidata.model.games;

import edu.ntnu.iir.bidata.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

class GameTest {

  // Concrete implementation of the abstract Game class for testing
  private static class TestGame extends Game {
    public TestGame() {
      super();
      gameType = GameType.SNAKES_AND_LADDERS; // Set a game type for testing
    }

    @Override
    public void handleEvent(String event) {
      // Simple implementation for testing
      if ("test_event".equals(event)) {
        setCurrentPlayerIndex((getCurrentPlayerIndex() + 1) % getPlayers().size());
      }
    }

    @Override
    public void start() {
      // Simple implementation for testing
      for (Player player : getPlayers()) {
        player.setPosition(1);
      }
    }

    // Expose protected methods for testing
    public void testSetMaxPlayers(int maxPlayers) {
      setMaxPlayers(maxPlayers);
    }

    public void testValidateGameType() {
      validateGameType();
    }

    public void testSetWinner(Player winner) {
      setWinner(winner);
    }
  }

  private TestGame game;
  private Player player1;
  private Player player2;
  private List<Player> players;
  private Board board;

  @BeforeEach
  void setUp() {
    game = new TestGame();
    player1 = new Player("Player1", new PlayingPiece(PlayingPieceType.CAR));
    player2 = new Player("Player2", new PlayingPiece(PlayingPieceType.HAT));
    players = Arrays.asList(player1, player2);
    board = new Board();

    // Add some tiles to the board
    board.addTile(new Tile(1));
    board.addTile(new Tile(2));

    game.setBoard(board);
    game.setPlayers(players);
  }

  @Test
  void setMaxPlayersChangesMaxPlayers() {
    // Arrange
    int newMaxPlayers = 6;

    // Act
    game.testSetMaxPlayers(newMaxPlayers);

    // Assert
    assertEquals(newMaxPlayers, game.getMaxPlayers(), "Max players should be changeable");
  }

  @Test
  void validateGameTypeDoesNotThrowWithValidGameType() {
    // Act & Assert
    assertDoesNotThrow(() -> game.testValidateGameType(),
            "validateGameType should not throw with valid game type");
  }

  @Test
  void getCurrentPlayerIndexReturnsCorrectIndex() {
    // Arrange
    game.setCurrentPlayerIndex(1);

    // Act & Assert
    assertEquals(1, game.getCurrentPlayerIndex(), "Should return correct current player index");
  }

  @Test
  void getCurrentPlayerReturnsCorrectPlayer() {
    // Arrange
    game.setCurrentPlayerIndex(1);

    // Act & Assert
    assertEquals(player2, game.getCurrentPlayer(), "Should return correct current player");
  }

  @Test
  void getBoardReturnsCorrectBoard() {
    // Act & Assert
    assertEquals(board, game.getBoard(), "Should return the correct board");
  }

  @Test
  void getPlayersReturnsCorrectPlayers() {
    // Act & Assert
    assertEquals(players, game.getPlayers(), "Should return the correct players list");
  }

  @Test
  void getDiceCountsReturnsCorrectCounts() {
    // Act
    List<Integer> diceCounts = game.getDiceCounts();

    // Assert
    assertEquals(2, diceCounts.size(), "Should return counts for 2 dice");
    for (Integer count : diceCounts) {
      assertTrue(count >= 1 && count <= 6, "Each count should be between 1 and 6");
    }
  }

  @Test
  void getWinnerReturnsNullInitially() {
    // Act & Assert
    assertNull(game.getWinner(), "Winner should be null initially");
  }

  @Test
  void setWinnerSetsWinnerCorrectly() {
    // Act
    game.testSetWinner(player1);

    // Assert
    assertEquals(player1, game.getWinner(), "Winner should be set correctly");
  }

  @Test
  void getAllPlayingPiecesReturnsAllPieceTypes() {
    // Act
    List<PlayingPiece> pieces = game.getAllPlayingPieces();

    // Assert
    assertEquals(PlayingPieceType.values().length, pieces.size(),
            "Should return a piece for each PlayingPieceType");

    // Check that each piece type is represented
    for (PlayingPieceType type : PlayingPieceType.values()) {
      boolean found = false;
      for (PlayingPiece piece : pieces) {
        if (piece.getType() == type) {
          found = true;
          break;
        }
      }
      assertTrue(found, "Should include a piece of type " + type);
    }
  }

  @Test
  void getGameTypeReturnsCorrectType() {
    // Act & Assert
    assertEquals(GameType.SNAKES_AND_LADDERS, game.getGameType(),
            "Should return the correct game type");
  }

  @Test
  void isPlayerConfigOkReturnsTrueForValidConfig() {
    // Arrange
    List<Player> validPlayers = Arrays.asList(
            new Player("P1", new PlayingPiece(PlayingPieceType.CAR)),
            new Player("P2", new PlayingPiece(PlayingPieceType.HAT))
    );

    // Act
    PlayerConfigResponse response = game.isPlayerConfigOk(validPlayers);

    // Assert
    assertTrue(response.isPlayerConfigOk(), "Should return true for valid player config");
    assertNull(response.getErrorMessage(), "Error message should be null for valid config");
  }

  @Test
  void isPlayerConfigOkReturnsFalseForNullPlayingPiece() {
    // Arrange
    List<Player> invalidPlayers = Arrays.asList(
            new Player("P1", new PlayingPiece(PlayingPieceType.CAR)),
            new Player("P2", null) // Null playing piece
    );

    // Act
    PlayerConfigResponse response = game.isPlayerConfigOk(invalidPlayers);

    // Assert
    assertFalse(response.isPlayerConfigOk(), "Should return false for null playing piece");
    assertNotNull(response.getErrorMessage(), "Error message should not be null for invalid config");
  }

  @Test
  void isPlayerConfigOkReturnsFalseForDuplicatePieces() {
    // Arrange
    List<Player> invalidPlayers = Arrays.asList(
            new Player("P1", new PlayingPiece(PlayingPieceType.CAR)),
            new Player("P2", new PlayingPiece(PlayingPieceType.CAR)) // Same piece type
    );

    // Act
    PlayerConfigResponse response = game.isPlayerConfigOk(invalidPlayers);

    // Assert
    assertFalse(response.isPlayerConfigOk(), "Should return false for duplicate playing pieces");
    assertNotNull(response.getErrorMessage(), "Error message should not be null for invalid config");
  }

  @Test
  void isGameOverReturnsFalseInitially() {
    // Act & Assert
    assertFalse(game.isGameOver(), "Game should not be over initially");
  }

  @Test
  void isGameOverReturnsTrueWhenWinnerIsSet() {
    // Arrange
    game.testSetWinner(player1);

    // Act & Assert
    assertTrue(game.isGameOver(), "Game should be over when winner is set");
  }

  @Test
  void startInitializesPlayerPositions() {
    // Arrange
    player1.setPosition(5);
    player2.setPosition(10);

    // Act
    game.start();

    // Assert
    assertEquals(1, player1.getPosition(), "Player1 position should be reset to 1");
    assertEquals(1, player2.getPosition(), "Player2 position should be reset to 1");
  }
}
