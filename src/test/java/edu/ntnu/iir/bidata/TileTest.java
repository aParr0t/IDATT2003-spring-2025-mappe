package edu.ntnu.iir.bidata;

import edu.ntnu.iir.bidata.model.Tile;
import edu.ntnu.iir.bidata.model.TileStyling;
import edu.ntnu.iir.bidata.model.tileaction.MoveAction;
import edu.ntnu.iir.bidata.model.tileaction.TileAction;
import javafx.geometry.Point2D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TileTest {

  @Test
  void tileHasCorrectId() {
    // Arrange
    int tileId = 5;
    Tile tile = new Tile(tileId);

    // Act & Assert
    assertEquals(tileId, tile.getId(), "Tile ID should be 5");
  }

  @Test
  void tilePositionCanBeSet() {
    // Arrange
    Tile tile = new Tile(1);
    Point2D newPosition = new Point2D(0.5, 0.7);

    // Act
    tile.setPosition(newPosition);

    // Assert
    assertEquals(newPosition, tile.getPosition(), "Position should be settable");
  }

  @Test
  void tilePositionCanBeSetWithCoordinates() {
    // Arrange
    Tile tile = new Tile(1);
    double x = 0.3;
    double y = 0.8;

    // Act
    tile.setPosition(x, y);

    // Assert
    assertEquals(new Point2D(x, y), tile.getPosition(),
            "Position should be settable with coordinates");
  }

  @Test
  void tileSizeCanBeSet() {
    // Arrange
    Tile tile = new Tile(1);
    double width = 100.0;
    double height = 50.0;

    // Act
    tile.setSize(width, height);

    // Assert
    assertEquals(width, tile.getWidth(), "Width should be settable");
    assertEquals(height, tile.getHeight(), "Height should be settable");
  }

  @Test
  void tileWidthAndHeightCanBeSetSeparately() {
    // Arrange
    Tile tile = new Tile(1);
    double width = 75.0;
    double height = 25.0;

    // Act
    tile.setWidth(width);
    tile.setHeight(height);

    // Assert
    assertEquals(width, tile.getWidth(), "Width should be settable separately");
    assertEquals(height, tile.getHeight(), "Height should be settable separately");
  }

  @Test
  void tileNextTileIsNullByDefault() {
    // Arrange
    Tile tile = new Tile(1);

    // Act & Assert
    assertNull(tile.getNextTile(), "Next tile should be null by default");
  }

  @Test
  void tileNextTileCanBeSet() {
    // Arrange
    Tile tile1 = new Tile(1);
    Tile tile2 = new Tile(2);

    // Act
    tile1.setNextTile(tile2);

    // Assert
    assertEquals(tile2, tile1.getNextTile(), "Next tile should be settable");
  }

  @Test
  void tileStylingCanBeSet() {
    // Arrange
    Tile tile = new Tile(1);
    TileStyling styling = new TileStyling();
    styling.setColor("#FF0000");

    // Act
    tile.setStyling(styling);

    // Assert
    assertEquals(styling, tile.getStyling(), "Styling should be settable");
    assertEquals("#FF0000", tile.getStyling().getColor(), "Styling color should match");
  }

  @Test
  void tileActionIsNullByDefault() {
    // Arrange
    Tile tile = new Tile(1);

    // Act & Assert
    assertNull(tile.getAction(), "Action should be null by default");
  }

  @Test
  void tileActionCanBeSet() {
    // Arrange
    Tile tile = new Tile(1);
    TileAction action = new MoveAction(1, 5);

    // Act
    tile.setAction(action);

    // Assert
    assertEquals(action, tile.getAction(), "Action should be settable");
    assertTrue(tile.getAction() instanceof MoveAction, "Action should be of correct type");
  }
}
