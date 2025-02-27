package edu.ntnu.iir.bidata;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DiceTest {

    @Test
    void rollAllReturnsSumWithinValidRange() {
        // Arrange
        Dice dice = new Dice(2, 6); // Two six-sided dice
        
        // Act & Assert
        for (int i = 0; i < 100; i++) {
            int sum = dice.rollAll();
            assertTrue(sum >= 2 && sum <= 12, "Sum should be between 2 and 12, but got: " + sum);
        }
    }

    @Test
    void diceWithInvalidValuesThrowsException() {
        // Assert (negative test)
        assertThrows(IllegalArgumentException.class, () -> new Dice(0, 6), "Must have at least one die");
        assertThrows(IllegalArgumentException.class, () -> new Dice(2, 0), "Dice must have valid sides");
    }
}
