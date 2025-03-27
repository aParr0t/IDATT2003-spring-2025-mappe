package edu.ntnu.iir.bidata.model;

/**
 * Represents a collection of dice.
 */
public class Dice {
    private final Die[] dice;

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
        if (sides != 6) {
            throw new IllegalArgumentException("Dice must have 6 sides");
        }

        dice = new Die[numberOfDice];
        for (int i = 0; i < numberOfDice; i++) {
            dice[i] = new Die(); // Die-klassen skal alltid være en 6-sidet terning
        }
    }

    /**
     * Rolls all dice and returns the total sum of the results.
     *
     * @return the sum of all dice rolls
     */
    public int rollAll() {
        int sum = 0;
        for (Die d : dice) {
            sum += d.roll();
        }
        return sum;
    }
}
