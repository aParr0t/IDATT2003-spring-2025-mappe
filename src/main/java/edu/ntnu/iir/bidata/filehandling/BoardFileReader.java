package edu.ntnu.iir.bidata.filehandling;

import edu.ntnu.iir.bidata.model.Board;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Interface for reading board data from files.
 */
public interface BoardFileReader {
    /**
     * Reads a board from a file.
     *
     * @param filePath the path to the file
     * @return the board read from the file
     * @throws IOException if an I/O error occurs
     */
    Board readBoard(Path filePath) throws IOException;
}
