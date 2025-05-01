package edu.ntnu.iir.bidata.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a collection of dice for board games.
 * This class manages multiple dice and provides methods to roll them and retrieve results.
 */
public class Dice {
  private final List<Die> dice = new ArrayList<>();

  /**
   * Creates a set of dice with specified number of sides.
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

  /**
   * Gets the current face value of each die.
   *
   * @return a list containing the face value of each die
   */
  public List<Integer> getCounts() {
    return dice.stream()
            .map(Die::getCount)
            .toList();
  }

  /**
   * Calculates the sum of all current dice values.
   *
   * @return the sum of the current face values of all dice
   */
  public int getSum() {
    return dice.stream()
            .mapToInt(Die::getCount)
            .sum();
  }
}
