package edu.ntnu.iir.bidata.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a collection of dice.
 */
public class Dice {
  private final List<Die> dice = new ArrayList<>();

  /**
   * Creates a set of six-sided dice.
   *
   * @param numberOfDice the number of dice in the set
   * @param sides        the number of sides on each die (default should be 6)
   * @throws IllegalArgumentException if numberOfDice is less than 1 or sides is not 6
   */
  public Dice(int numberOfDice, int sides) {
    if (numberOfDice < 1) {
      throw new IllegalArgumentException("Must have at least one die");
    }

    for (int i = 0; i < numberOfDice; i++) {
      dice.add(new Die(sides));
    }
  }

  /**
   * Rolls all dice and returns the total sum of the results.
   *
   * @return the sum of all dice rolls
   */
  public int rollAll() {
    int sum = 0;
    for (Die die : dice) {
      sum += die.roll();
    }
    return sum;
  }

  public List<Integer> getCounts() {
    return dice.stream()
            .map(Die::getCount)
            .toList();
  }
}
