package edu.ntnu.iir.bidata;

import edu.ntnu.iir.bidata.model.Die;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DieTest {

    @Test
    void rollReturnsValueWithinValidRange() {
        // Arrange
        Die die = new Die(); // Endret fra new Die(6) til new Die(), siden terningen alltid har 6 sider

        // Act & Assert
        for (int i = 0; i < 100; i++) {
            int roll = die.roll();
            assertTrue(roll >= 1 && roll <= 6, "Roll should be between 1 and 6, but got: " + roll);
        }
    }
}
