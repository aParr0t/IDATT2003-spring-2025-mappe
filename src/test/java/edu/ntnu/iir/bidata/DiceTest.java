package edu.ntnu.iir.bidata;

import edu.ntnu.iir.bidata.model.Dice;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class DiceTest {

    @RepeatedTest(100)
    void rollAllReturnsSumWithinValidRange() {
        // Arrange
        Dice dice = new Dice(2, 6); // Two six-sided dice
        
        // Act
        int sum = dice.rollAll();
        
        // Assert
        assertTrue(sum >= 2 && sum <= 12, "Sum should be between 2 and 12, but got: " + sum);
    }

    @Test
    void diceWithInvalidValuesThrowsException() {
        // Assert (negative test)
        assertThrows(IllegalArgumentException.class, () -> new Dice(0, 6), "Must have at least one die");
        assertThrows(IllegalArgumentException.class, () -> new Dice(-1, 6), "Cannot have negative number of dice");
    }
    
    @ParameterizedTest
    @CsvSource({
        "1, 6, 1, 6",
        "2, 6, 2, 12",
        "3, 6, 3, 18",
        "2, 10, 2, 20"
    })
    void rollAllReturnsCorrectRangeForDifferentConfigurations(int numberOfDice, int sides, int minSum, int maxSum) {
        // Arrange
        Dice dice = new Dice(numberOfDice, sides);
        
        // Act & Assert
        for (int i = 0; i < 50; i++) {
            int sum = dice.rollAll();
            assertTrue(sum >= minSum && sum <= maxSum, 
                    "Sum should be between " + minSum + " and " + maxSum + ", but got: " + sum);
        }
    }
    
    @Test
    void getCountsReturnsCorrectNumberOfValues() {
        // Arrange
        int numberOfDice = 3;
        Dice dice = new Dice(numberOfDice, 6);
        
        // Act
        dice.rollAll();
        List<Integer> counts = dice.getCounts();
        
        // Assert
        assertEquals(numberOfDice, counts.size(), "Should return counts for all dice");
        for (Integer count : counts) {
            assertTrue(count >= 1 && count <= 6, "Each count should be between 1 and 6");
        }
    }
    
    @Test
    void getSumReturnsCorrectSum() {
        // Arrange
        Dice dice = new Dice(2, 6);
        
        // Act
        dice.rollAll();
        List<Integer> counts = dice.getCounts();
        int expectedSum = counts.stream().mapToInt(Integer::intValue).sum();
        
        // Assert
        assertEquals(expectedSum, dice.getSum(), "getSum() should return the sum of all dice counts");
    }
}
