package edu.ntnu.iir.bidata.model.tileaction;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MoveActionTest {

  @Test
  void constructorSetsStartAndEndCorrectly() {
    // Arrange
    int start = 5;
    int end = 20;

    // Act
    MoveAction moveAction = new MoveAction(start, end);

    // Assert
    assertEquals(start, moveAction.getStart(), "Start position should be set correctly");
    assertEquals(end, moveAction.getEnd(), "End position should be set correctly");
  }
}
