package edu.ntnu.iir.bidata.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A mock implementation of Dice that allows controlling the outcome of dice rolls.
 * This is useful for testing where we need deterministic dice results.
 */
public class MockDice extends Dice {
  private final List<Integer> predefinedResults = new ArrayList<>();
  private int currentResultIndex = 0;
  private List<Integer> lastRolledValues = new ArrayList<>();
  private boolean isDoubles = false;

  /**
   * Creates a mock dice with the specified number of dice and sides.
   *
   * @param numberOfDice the number of dice in the set
   * @param sides        the number of sides on each die
   */
  public MockDice(int numberOfDice, int sides) {
    super(numberOfDice, sides);
    // Initialize with default values
    for (int i = 0; i < numberOfDice; i++) {
      lastRolledValues.add(1);
    }
  }

  /**
   * Sets the next result to be returned when rollAll() is called.
   * This will be the sum of all dice.
   *
   * @param result the next result to return
   */
  public void setNextResult(int result) {
    predefinedResults.add(result);

    // For a standard 2-dice setup, split the result evenly
    if (lastRolledValues.size() == 2) {
      int half = result / 2;
      int remainder = result % 2;
      lastRolledValues.set(0, half + remainder);
      lastRolledValues.set(1, half);
      isDoubles = (half == (half + remainder));
    } else {
      // For other dice counts, just set the first die to the full value
      lastRolledValues.set(0, result);
      for (int i = 1; i < lastRolledValues.size(); i++) {
        lastRolledValues.set(i, 0);
      }
      isDoubles = false;
    }
  }

  /**
   * Sets whether the next roll should be doubles (both dice showing the same value).
   * This is particularly useful for Monopoly tests.
   *
   * @param isDoubles whether the next roll should be doubles
   * @param value     the value to use for both dice (1-6)
   */
  public void setNextRollAsDoubles(boolean isDoubles, int value) {
    if (isDoubles) {
      // Set both dice to the same value
      this.isDoubles = true;
      setNextResult(value * 2);
      lastRolledValues.set(0, value);
      lastRolledValues.set(1, value);
    } else {
      // Set dice to different values
      this.isDoubles = false;
      int secondValue = (value % 6) + 1;
      if (secondValue == value) {
        secondValue = (secondValue % 6) + 1;
      }
      setNextResult(value + secondValue);
      lastRolledValues.set(0, value);
      lastRolledValues.set(1, secondValue);
    }
  }

  /**
   * Instead of rolling dice randomly, returns the next predefined result.
   * If no predefined results are available, falls back to the standard random roll.
   *
   * @return the next predefined result or a random roll if none is available
   */
  @Override
  public int rollAll() {
    if (!predefinedResults.isEmpty() && currentResultIndex < predefinedResults.size()) {
      int result = predefinedResults.get(currentResultIndex);
      currentResultIndex++;
      return result;
    }

    // Fall back to random roll if no predefined values
    return super.rollAll();
  }

  /**
   * Returns the predefined dice values.
   */
  @Override
  public List<Integer> getCounts() {
    return new ArrayList<>(lastRolledValues);
  }

  /**
   * Returns the sum of the dice values.
   */
  @Override
  public int getSum() {
    return lastRolledValues.stream().mapToInt(Integer::intValue).sum();
  }
}
