package edu.ntnu.iir.bidata;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @Test
    void boardHasCorrectNumberOfTiles() {
        // Arrange
        Board board = new Board(90);
        
        // Act & Assert
        assertEquals(90, board.getSize(), "Board should have 90 tiles");
    }

    @Test
    void boardWithInvalidSizeThrowsException() {
        // Assert (negative test)
        assertThrows(IllegalArgumentException.class, () -> new Board(0), "Board must have at least one tile");
    }
}
