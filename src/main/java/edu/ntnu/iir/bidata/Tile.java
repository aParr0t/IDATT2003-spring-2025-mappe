package edu.ntnu.iir.bidata;

/**
 * Represents a tile on the game board.
 */
public class Tile {
    private final int id;

    /**
     * Creates a tile with a unique identifier.
     *
     * @param id the tile's unique identifier
     */
    public Tile(int id) {
        this.id = id;
    }

    /**
     * Gets the tile's unique identifier.
     *
     * @return the tile ID
     */
    public int getId() {
        return id;
    }
}
