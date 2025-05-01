package edu.ntnu.iir.bidata.model;

import java.util.Random;

/**
 * Represents a die with a configurable number of sides.
 * This class simulates the rolling of a die and maintains the current value.
 */
public class Die {
  private final int sides;
  private final Random random;
  private int count = 1;

  /**
   * Creates a die with the specified number of sides.
   *
   * @param sides the number of sides on the die
   */
  public Die(int sides) {
    this.sides = sides;
    this.random = new Random();
  }

  /**
   * Rolls the die and returns a random value.
   * Updates the internal count to the new value.
   *
   * @return a random value between 1 and sides (inclusive)
   */
  public int roll() {
    count = random.nextInt(sides) + 1;
    return count;
  }

  /**
   * Returns the current value of the die from the last roll.
   * If the die has not been rolled yet, returns the initial value (1).
   *
   * @return the current value of the die
   */
  public int getCount() {
    return count;
  }
}
