package edu.ntnu.iir.bidata;

import edu.ntnu.iir.bidata.model.Board;
import edu.ntnu.iir.bidata.model.Tile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

class BoardTest {

  @Test
  void boardDefaultConstructorCreatesEmptyBoard() {
    // Arrange & Act
    Board board = new Board();

    // Assert
    assertEquals(0, board.getTileCount(), "Board should have no tiles initially");
  }

  @Test
  void boardConstructorWithTilesAddsAllTiles() {
    // Arrange
    List<Tile> tiles = new ArrayList<>();
    tiles.add(new Tile(1));
    tiles.add(new Tile(2));
    tiles.add(new Tile(3));

    // Act
    Board board = new Board(tiles);

    // Assert
    assertEquals(3, board.getTileCount(), "Board should have 3 tiles");
    assertNotNull(board.getTile(1), "Board should contain tile with ID 1");
    assertNotNull(board.getTile(2), "Board should contain tile with ID 2");
    assertNotNull(board.getTile(3), "Board should contain tile with ID 3");
  }

  @Test
  void boardNameCanBeSet() {
    // Arrange
    Board board = new Board();
    String newName = "Test Board";

    // Act
    board.setName(newName);

    // Assert
    assertEquals(newName, board.getName(), "Board name should be settable");
  }

  @Test
  void addTileAddsTileToBoard() {
    // Arrange
    Board board = new Board();
    Tile tile = new Tile(5);

    // Act
    board.addTile(tile);

    // Assert
    assertEquals(1, board.getTileCount(), "Board should have 1 tile after adding");
    assertEquals(tile, board.getTile(5), "Board should contain the added tile");
  }

  @Test
  void getTileReturnsCorrectTile() {
    // Arrange
    Board board = new Board();
    Tile tile1 = new Tile(1);
    Tile tile2 = new Tile(2);
    board.addTile(tile1);
    board.addTile(tile2);

    // Act & Assert
    assertEquals(tile1, board.getTile(1), "getTile should return the correct tile");
    assertEquals(tile2, board.getTile(2), "getTile should return the correct tile");
  }

  @Test
  void getTileReturnsNullForNonexistentTile() {
    // Arrange
    Board board = new Board();

    // Act & Assert
    assertNull(board.getTile(999), "getTile should return null for nonexistent tile ID");
  }

  @Test
  void getTilesReturnsAllTiles() {
    // Arrange
    Board board = new Board();
    Tile tile1 = new Tile(1);
    Tile tile2 = new Tile(2);
    board.addTile(tile1);
    board.addTile(tile2);

    // Act
    List<Tile> tiles = board.getTiles();

    // Assert
    assertEquals(2, tiles.size(), "getTiles should return all tiles");
    assertTrue(tiles.contains(tile1), "Returned tiles should contain tile1");
    assertTrue(tiles.contains(tile2), "Returned tiles should contain tile2");
  }

  @Test
  void getTilesReturnsEmptyListForEmptyBoard() {
    // Arrange
    Board board = new Board();

    // Act
    List<Tile> tiles = board.getTiles();

    // Assert
    assertTrue(tiles.isEmpty(), "getTiles should return empty list for empty board");
  }

  @Test
  void getTileCountReturnsCorrectCount() {
    // Arrange
    Board board = new Board();
    board.addTile(new Tile(1));
    board.addTile(new Tile(2));

    // Act & Assert
    assertEquals(2, board.getTileCount(), "getTileCount should return correct number of tiles");
  }
}
