package edu.ntnu.iir.bidata.model.games;

import edu.ntnu.iir.bidata.model.*;
import edu.ntnu.iir.bidata.model.tileaction.MoveAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

class SnakesAndLaddersGameTest {

  private SnakesAndLaddersGame game;
  private Player player1;
  private Player player2;
  private List<Player> players;
  private Board board;
  private MockDice mockDice;

  @BeforeEach
  void setUp() {
    game = new SnakesAndLaddersGame();
    player1 = new Player("Player1", new PlayingPiece(PlayingPieceType.CAR));
    player2 = new Player("Player2", new PlayingPiece(PlayingPieceType.HAT));
    players = Arrays.asList(player1, player2);

    // Create a board with 10 tiles
    board = new Board();
    for (int i = 1; i <= 10; i++) {
      board.addTile(new Tile(i));
    }

    // Add a ladder (move action) from tile 2 to tile 8
    Tile tile2 = board.getTile(2);
    tile2.setAction(new MoveAction(2, 8));

    // Add a snake (move action) from tile 7 to tile 3
    Tile tile7 = board.getTile(7);
    tile7.setAction(new MoveAction(7, 3));

    // Create and set up mock dice
    mockDice = new MockDice(2, 6);
    game.setDice(mockDice);
    
    game.setBoard(board);
    game.setPlayers(players);
  }

  @Test
  void gameTypeIsSnakesAndLadders() {
    // Act & Assert
    assertEquals(GameType.SNAKES_AND_LADDERS, game.getGameType(),
            "Game type should be SNAKES_AND_LADDERS");
  }

  @Test
  void startSetsAllPlayerPositionsToOne() {
    // Arrange
    player1.setPosition(5);
    player2.setPosition(7);

    // Act
    game.start();

    // Assert
    assertEquals(1, player1.getPosition(), "Player1 position should be set to 1");
    assertEquals(1, player2.getPosition(), "Player2 position should be set to 1");
  }

  @Test
  void handleEventWithDiceRolledMakesPlayerMove() {
    // Arrange
    game.start(); // Set all players to position 1
    game.setCurrentPlayerIndex(0); // Ensure player1 is current
    mockDice.setNextResult(2); // Player will move 2 spaces

    // Act
    game.handleEvent("snakes_and_ladders_dice_rolled");

    // Assert
    assertEquals(3, player1.getPosition(), "Player1 should have moved 2 spaces from position 1");
    assertEquals(1, game.getCurrentPlayerIndex(), "Current player should have changed to player2");
  }

  @Test
  void playerLandingOnLadderMovesToLadderEnd() {
    // Arrange
    game.start();
    game.setCurrentPlayerIndex(0);
    
    // Set player position directly to position 1
    player1.setPosition(1);
    
    // Force dice to roll a 1 (to land on position 2 where the ladder is)
    mockDice.setNextResult(1);

    // Act
    game.handleEvent("snakes_and_ladders_dice_rolled");

    // Assert
    assertEquals(8, player1.getPosition(),
            "Player should move to position 8 after landing on ladder at position 2");
  }

  @Test
  void playerLandingOnSnakeMovesToSnakeEnd() {
    // Arrange
    game.start();
    game.setCurrentPlayerIndex(0);
    
    // Set player position directly to position 6
    player1.setPosition(6);
    
    // Force dice to roll a 1 (to land on position 7 where the snake is)
    mockDice.setNextResult(1);

    // Act
    game.handleEvent("snakes_and_ladders_dice_rolled");

    // Assert
    assertEquals(3, player1.getPosition(),
            "Player should move to position 3 after landing on snake at position 7");
  }

  @Test
  void playerWinsWhenReachingLastTile() {
    // Arrange
    game.start();
    game.setCurrentPlayerIndex(0);
    player1.setPosition(board.getTiles().size() - 1);
    
    // Force dice to roll a 1 to reach the last tile
    mockDice.setNextResult(1);

    // Act
    game.handleEvent("snakes_and_ladders_dice_rolled");

    // Assert
    assertEquals(player1, game.getWinner(), "Player1 should be the winner");
    assertTrue(game.isGameOver(), "Game should be over");
  }

  @Test
  void playerCannotMoveMoreThanLastTile() {
    // Arrange
    game.start();
    game.setCurrentPlayerIndex(0);
    int lastTilePosition = board.getTiles().size();
    player1.setPosition(lastTilePosition - 1);
    
    // Force dice to roll a 6 (which would put player beyond the last tile)
    mockDice.setNextResult(6);

    // Act
    game.handleEvent("snakes_and_ladders_dice_rolled");

    // Assert
    assertEquals(lastTilePosition, player1.getPosition(),
            "Player position should be capped at the last tile position");
  }

  @Test
  void handleEventWithUnknownEventDoesNothing() {
    // Arrange
    game.start();
    game.setCurrentPlayerIndex(0);
    int initialPosition = player1.getPosition();
    int initialPlayerIndex = game.getCurrentPlayerIndex();

    // Act
    game.handleEvent("unknown_event");

    // Assert
    assertEquals(initialPosition, player1.getPosition(), "Player position should not change");
    assertEquals(initialPlayerIndex, game.getCurrentPlayerIndex(), "Current player should not change");
  }
}
