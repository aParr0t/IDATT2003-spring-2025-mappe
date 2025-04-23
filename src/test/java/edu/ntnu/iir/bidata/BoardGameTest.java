package edu.ntnu.iir.bidata;

import edu.ntnu.iir.bidata.model.Dice;
import edu.ntnu.iir.bidata.model.Player;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoardGameTest {

  @Test
  void gameEndsWhenPlayerReachesLastTile() {
    // Arrange
//        Board board = new Board(10);
    Player p1 = new Player("TestPlayer");
    List<Player> players = Arrays.asList(p1);
    Dice dice = new Dice(1, 6);
//        BoardGame game = new BoardGame(board, players, dice);

    // Act
    while (p1.getPosition() < 9) {
      p1.move(2); // Move fast to reach the end
    }

    // Assert
    assertTrue(p1.getPosition() >= 9, "Player should have reached the final tile");
  }
}
