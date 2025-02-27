package edu.ntnu.iir.bidata;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TileTest {

    @Test
    void tileHasCorrectId() {
        // Arrange
        Tile tile = new Tile(5);
        
        // Act & Assert
        assertEquals(5, tile.getId(), "Tile ID should be 5");
    }
}
