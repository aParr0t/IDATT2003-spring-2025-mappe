package edu.ntnu.iir.bidata;

import edu.ntnu.iir.bidata.model.Player;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void playerMovesCorrectly() {
        // Arrange
        Player player = new Player("TestPlayer");
        
        // Act
        player.move(5);
        
        // Assert
        assertEquals(5, player.getPosition(), "Player should have moved to position 5");
    }

    @Test
    void playerCannotMoveNegativeSteps() {
        // Arrange
        Player player = new Player("TestPlayer");
        
        // Act
        player.move(-3);
        
        // Assert
        assertEquals(0, player.getPosition(), "Player should not move backwards");
    }
}
