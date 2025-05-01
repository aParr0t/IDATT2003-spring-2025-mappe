package edu.ntnu.iir.bidata;

import edu.ntnu.iir.bidata.model.Player;
import edu.ntnu.iir.bidata.model.PlayingPiece;
import edu.ntnu.iir.bidata.model.PlayingPieceType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

  @Test
  void playerNameIsSetCorrectly() {
    // Arrange
    String playerName = "TestPlayer";

    // Act
    Player player = new Player(playerName);

    // Assert
    assertEquals(playerName, player.getName(), "Player name should be set correctly");
  }

  @Test
  void playerNameCanBeChanged() {
    // Arrange
    Player player = new Player("InitialName");
    String newName = "NewName";

    // Act
    player.setName(newName);

    // Assert
    assertEquals(newName, player.getName(), "Player name should be changeable");
  }

  @Test
  void playerPositionCanBeSetDirectly() {
    // Arrange
    Player player = new Player("TestPlayer");
    int newPosition = 10;

    // Act
    player.setPosition(newPosition);

    // Assert
    assertEquals(newPosition, player.getPosition(), "Player position should be settable");
  }

  @Test
  void playerPlayingPieceIsNullByDefault() {
    // Arrange & Act
    Player player = new Player("TestPlayer");

    // Assert
    assertNull(player.getPlayingPiece(), "Playing piece should be null by default");
  }

  @Test
  void playerPlayingPieceCanBeSet() {
    // Arrange
    Player player = new Player("TestPlayer");
    PlayingPiece piece = new PlayingPiece(PlayingPieceType.CAR);

    // Act
    player.setPlayingPiece(piece);

    // Assert
    assertEquals(piece, player.getPlayingPiece(), "Playing piece should be settable");
    assertEquals(PlayingPieceType.CAR, player.getPlayingPiece().getType(),
            "Playing piece type should match");
  }

  @Test
  void playerConstructorWithPlayingPieceSetsCorrectly() {
    // Arrange
    String playerName = "TestPlayer";
    PlayingPiece piece = new PlayingPiece(PlayingPieceType.HAT);

    // Act
    Player player = new Player(playerName, piece);

    // Assert
    assertEquals(playerName, player.getName(), "Player name should be set correctly");
    assertEquals(piece, player.getPlayingPiece(), "Playing piece should be set correctly");
  }
}
