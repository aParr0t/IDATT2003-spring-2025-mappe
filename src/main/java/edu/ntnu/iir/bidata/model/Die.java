package edu.ntnu.iir.bidata.model;

import java.util.Random;

/**
 * Represents a six-sided die.
 */
public class Die {
  private final int sides;
  private final Random random;
  private int count = 1;

  /**
   * Creates a six-sided die.
   */
  public Die(int sides) {
    this.sides = sides;
    this.random = new Random();
  }

  /**
   * Rolls the die and returns a value between 1 and sides (inclusive).
   *
   * @return a random value between 1 and sides (inclusive)
   */
  public int roll() {
    count = random.nextInt(sides) + 1;
    return count;
  }

  public int getCount() {
    return count;
  }
}
