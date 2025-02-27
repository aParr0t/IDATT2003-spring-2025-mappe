package edu.ntnu.iir.bidata;

import java.util.Random;

/**
 * Represents a six-sided die.
 */
public class Die {
    private static final int SIDES = 6;
    private final Random random;

    /**
     * Creates a six-sided die.
     */
    public Die() {
        this.random = new Random();
    }

    /**
     * Rolls the die and returns a value between 1 and 6.
     *
     * @return a random value between 1 and 6
     */
    public int roll() {
        return random.nextInt(SIDES) + 1;
    }
}
