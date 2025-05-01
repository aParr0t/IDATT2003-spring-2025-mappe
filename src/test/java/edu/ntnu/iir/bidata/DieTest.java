package edu.ntnu.iir.bidata;

import edu.ntnu.iir.bidata.model.Die;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class DieTest {

  @RepeatedTest(100)
  void rollReturnsValueWithinValidRange() {
    // Arrange
    Die die = new Die(6);

    // Act
    int roll = die.roll();

    // Assert
    assertTrue(roll >= 1 && roll <= 6, "Roll should be between 1 and 6, but got: " + roll);
  }

  @Test
  void getCountReturnsLastRollValue() {
    // Arrange
    Die die = new Die(6);

    // Act
    int roll = die.roll();

    // Assert
    assertEquals(roll, die.getCount(), "getCount() should return the last roll value");
  }

  @ParameterizedTest
  @ValueSource(ints = {4, 6, 8, 10, 12, 20})
  void dieWithDifferentSidesReturnsCorrectRange(int sides) {
    // Arrange
    Die die = new Die(sides);

    // Act & Assert
    for (int i = 0; i < 50; i++) {
      int roll = die.roll();
      assertTrue(roll >= 1 && roll <= sides,
              "Roll should be between 1 and " + sides + ", but got: " + roll);
    }
  }
}
