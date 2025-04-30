package edu.ntnu.iir.bidata.filehandling;

import edu.ntnu.iir.bidata.exceptions.CsvParsingException;
import edu.ntnu.iir.bidata.exceptions.FileNotFoundException;
import edu.ntnu.iir.bidata.exceptions.PlayerDataException;
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
     * @throws FileNotFoundException if the file does not exist
     * @throws CsvParsingException if there is an error parsing the CSV
     * @throws PlayerDataException if there is an error with the player data
     */
    List<Player> readPlayers(Path filePath) throws IOException, FileNotFoundException, CsvParsingException, PlayerDataException;
}
