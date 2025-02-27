package edu.ntnu.iir.bidata;

/**
 * Represents a player in the game.
 */
public class Player {
    private final String name;
    private int position;

    /**
     * Creates a new player with a given name.
     *
     * @param name the player's name
     */
    public Player(String name) {
        this.name = name;
        this.position = 0; // Spilleren starter på posisjon 0
    }

    /**
     * Moves the player forward a certain number of steps.
     * If the steps are negative, the player does not move.
     *
     * @param steps the number of steps to move forward
     */
    public void move(int steps) {
        if (steps > 0) {
            this.position += steps;
        }
        // Hvis steps er negativt, skjer ingenting
    }

    /**
     * Gets the player's current position.
     *
     * @return the player's position
     */
    public int getPosition() {
        return position;
    }

    /**
     * Gets the player's name.
     *
     * @return the player's name
     */
    public String getName() {
        return name;
    }
}
