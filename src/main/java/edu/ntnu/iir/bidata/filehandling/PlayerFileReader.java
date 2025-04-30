package edu.ntnu.iir.bidata.filehandling;

import edu.ntnu.iir.bidata.model.Player;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Interface for reading player data from files.
 */
public interface PlayerFileReader {
    /**
     * Reads a list of players from a file.
     *
     * @param filePath the path to the file
     * @return the list of players read from the file
     * @throws IOException if an I/O error occurs
     */
    List<Player> readPlayers(Path filePath) throws IOException;
}
