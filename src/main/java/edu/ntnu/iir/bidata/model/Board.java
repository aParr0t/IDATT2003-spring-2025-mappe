package edu.ntnu.iir.bidata.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the game board consisting of multiple tiles.
 */
public class Board {
    private final List<Tile> tiles;

    /**
     * Creates a board with a specified number of tiles.
     *
     * @param size the number of tiles on the board
     * @throws IllegalArgumentException if the size is less than 1
     */
    public Board(int size) {
        if (size < 1) {
            throw new IllegalArgumentException("Board must have at least one tile");
        }
        tiles = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            tiles.add(new Tile(i));
        }
    }

    /**
     * Returns the total number of tiles on the board.
     *
     * @return the board size
     */
    public int getSize() {
        return tiles.size();
    }
}
