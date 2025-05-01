package edu.ntnu.iir.bidata.model.games;

import edu.ntnu.iir.bidata.model.*;
import edu.ntnu.iir.bidata.model.tileaction.GoToJailAction;
import edu.ntnu.iir.bidata.model.tileaction.JailAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

class MonopolyGameTest {

  private MonopolyGame game;
  private Player player1;
  private Player player2;
  private List<Player> players;
  private Board board;
  private MockDice mockDice;
  private static final int STARTING_MONEY = 1500;
  private static final int PASSING_GO_MONEY = 200;

  @BeforeEach
  void setUp() {
    game = new MonopolyGame();
    player1 = new Player("Player1", new PlayingPiece(PlayingPieceType.CAR));
    player2 = new Player("Player2", new PlayingPiece(PlayingPieceType.HAT));
    players = Arrays.asList(player1, player2);

    // Create a board with 10 tiles
    board = new Board();
    for (int i = 0; i < 10; i++) {
      board.addTile(new Tile(i));
    }

    // Add a "Go To Jail" action on tile 4
    Tile goToJailTile = board.getTile(4);
    goToJailTile.setAction(new GoToJailAction());

    // Add a "Jail" action on tile 7
    Tile jailTile = board.getTile(7);
    jailTile.setAction(new JailAction());

    // Create and set up mock dice
    mockDice = new MockDice(2, 6);
    game.setDice(mockDice);
    
    game.setBoard(board);
    game.setPlayers(players);
    game.start(); // Initialize the game
  }

  @Test
  void gameTypeIsMonopoly() {
    // Act & Assert
    assertEquals(GameType.MONOPOLY, game.getGameType(),
            "Game type should be MONOPOLY");
  }

  @Test
  void startSetsAllPlayerPositionsToZeroAndGivesStartingMoney() {
    // Arrange
    player1.setPosition(5);
    player2.setPosition(7);

    // Act
    game.start();

    // Assert
    assertEquals(0, player1.getPosition(), "Player1 position should be set to 0");
    assertEquals(0, player2.getPosition(), "Player2 position should be set to 0");
    assertEquals(STARTING_MONEY, game.getPlayerMoney(player1), "Player1 should have starting money");
    assertEquals(STARTING_MONEY, game.getPlayerMoney(player2), "Player2 should have starting money");
  }

  @Test
  void handleEventWithDiceRolledMakesPlayerMove() {
    // Arrange
    game.setCurrentPlayerIndex(0); // Ensure player1 is current
    // Make sure we're not rolling doubles (which would give player another turn)
    mockDice.setNextRollAsDoubles(false, 2); // Not doubles: 2 and 3 = 5 total

    // Act
    game.handleEvent("monopoly_dice_rolled");

    // Assert
    assertEquals(5, player1.getPosition(), "Player1 should have moved from position 0");
    assertEquals(1, game.getCurrentPlayerIndex(), "Current player should have changed to player2");
  }

  @Test
  void playerPassingGoReceivesMoney() {
    // Arrange
    game.setCurrentPlayerIndex(0);
    player1.setPosition(8); // Near the end of the board
    int initialMoney = game.getPlayerMoney(player1);
    mockDice.setNextResult(2); // This will make the player pass GO (position 0)

    // Act
    game.handleEvent("monopoly_dice_rolled");

    // Assert
    assertEquals(0, player1.getPosition(), "Player1 should be at position 0 after passing GO");
    assertEquals(initialMoney + PASSING_GO_MONEY, game.getPlayerMoney(player1),
            "Player1 should receive money for passing GO");
  }

  @Test
  void playerLandingOnGoToJailIsSentToJail() {
    // Arrange
    game.setCurrentPlayerIndex(0);
    player1.setPosition(1);
    mockDice.setNextResult(3); // This will land on the "Go To Jail" tile at position 4

    // Act
    game.handleEvent("monopoly_dice_rolled");

    // Assert
    assertEquals(7, player1.getPosition(), "Player1 should be moved to the jail position");
    assertTrue(game.isInJail(player1), "Player1 should be in jail");
    assertEquals(1, game.getCurrentPlayerIndex(), "Current player should have changed to player2");
  }

  @Test
  void playerInJailCannotMoveUntilRollingDoubles() {
    // Arrange
    game.setCurrentPlayerIndex(0);
    game.sendToJail(player1);
    int jailPosition = player1.getPosition();
    
    // First roll - not doubles
    mockDice.setNextRollAsDoubles(false, 3); // Not doubles: 3 and 4

    // Act - First turn in jail
    game.handleEvent("monopoly_dice_rolled");

    // Assert
    assertEquals(jailPosition, player1.getPosition(), "Player1 should still be in jail");
    assertTrue(game.isInJail(player1), "Player1 should still be in jail");
    assertEquals(1, game.getCurrentPlayerIndex(), "Current player should have changed to player2");
  }
  
  @Test
  void playerReleasedFromJailCanMove() {
    // Arrange
    game.setCurrentPlayerIndex(0);
    game.sendToJail(player1);
    
    // Manually release the player from jail
    game.releaseFromJail(player1);
    
    // Verify player is out of jail
    assertFalse(game.isInJail(player1), "Player1 should be out of jail after being released");
    
    // Set player position to 0 (GO) to match the game's behavior
    player1.setPosition(0);
    
    // Set up dice roll
    mockDice.setNextResult(3);
    
    // Act - Roll dice after being released
    game.handleEvent("monopoly_dice_rolled");
    
    // Assert
    assertEquals(3, player1.getPosition(), 
            "Player1 should move to position 3 after being released from jail and rolling 3");
  }

  @Test
  void playerRollingDoublesGetsAnotherTurn() {
    // Arrange
    game.setCurrentPlayerIndex(0);
    mockDice.setNextRollAsDoubles(true, 4); // Roll doubles: 4 and 4
    
    // Act - First roll (doubles)
    game.handleEvent("monopoly_dice_rolled");
    
    // Assert - Player should still be the current player after rolling doubles
    assertEquals(0, game.getCurrentPlayerIndex(), 
            "Current player should not change after rolling doubles");
    
    // Arrange for second roll - not doubles
    mockDice.setNextRollAsDoubles(false, 3); // Not doubles: 3 and 4
    
    // Act - Second roll (not doubles)
    game.handleEvent("monopoly_dice_rolled");
    
    // Assert - Player turn should now change
    assertEquals(1, game.getCurrentPlayerIndex(), 
            "Current player should change after not rolling doubles");
  }

  @Test
  void addMoneyIncreasesPlayerBalance() {
    // Arrange
    int initialMoney = game.getPlayerMoney(player1);
    int amountToAdd = 200;

    // Act
    int newBalance = game.addMoney(player1, amountToAdd);

    // Assert
    assertEquals(initialMoney + amountToAdd, newBalance, "New balance should be initial + added amount");
    assertEquals(initialMoney + amountToAdd, game.getPlayerMoney(player1), 
            "Player money should be updated in the game");
  }

  @Test
  void removeMoneyDecreasesPlayerBalance() {
    // Arrange
    int initialMoney = game.getPlayerMoney(player1);
    int amountToRemove = 200;

    // Act
    int newBalance = game.removeMoney(player1, amountToRemove);

    // Assert
    assertEquals(initialMoney - amountToRemove, newBalance, "New balance should be initial - removed amount");
    assertEquals(initialMoney - amountToRemove, game.getPlayerMoney(player1), 
            "Player money should be updated in the game");
  }

  @Test
  void transferMoneyMovesMoneyBetweenPlayers() {
    // Arrange
    int player1InitialMoney = game.getPlayerMoney(player1);
    int player2InitialMoney = game.getPlayerMoney(player2);
    int transferAmount = 300;

    // Act
    game.transferMoney(player1, player2, transferAmount);

    // Assert
    assertEquals(player1InitialMoney - transferAmount, game.getPlayerMoney(player1), 
            "Player1 should have less money");
    assertEquals(player2InitialMoney + transferAmount, game.getPlayerMoney(player2), 
            "Player2 should have more money");
  }

  @Test
  void removeMoneyThrowsExceptionIfNotEnoughMoney() {
    // Arrange
    int playerMoney = game.getPlayerMoney(player1);
    int tooMuchToRemove = playerMoney + 100;

    // Act & Assert
    Exception exception = assertThrows(IllegalArgumentException.class, 
            () -> game.removeMoney(player1, tooMuchToRemove),
            "Should throw exception when trying to remove more money than player has");
    
    assertTrue(exception.getMessage().contains("enough money"), 
            "Exception message should mention not having enough money");
  }

  @Test
  void playerWithEnoughMoneyWinsTheGame() {
    // Arrange
    game.setCurrentPlayerIndex(0);
    // Give player1 enough money to win (2000 is the winning amount in MonopolyGame)
    game.addMoney(player1, 600); // Starting money is 1500, so add 600 to exceed 2000
    mockDice.setNextResult(1); // Just move one space to trigger the win check

    // Act
    game.handleEvent("monopoly_dice_rolled");

    // Assert
    assertEquals(player1, game.getWinner(), "Player1 should be the winner");
    assertTrue(game.isGameOver(), "Game should be over");
  }

  @Test
  void handleEventWithUnknownEventDoesNothing() {
    // Arrange
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
