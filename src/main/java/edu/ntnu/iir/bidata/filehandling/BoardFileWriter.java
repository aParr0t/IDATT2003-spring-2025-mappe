package edu.ntnu.iir.bidata.filehandling;

import edu.ntnu.iir.bidata.model.Board;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Interface for writing board data to files.
 */
public interface BoardFileWriter {
    /**
     * Writes a board to a file.
     *
     * @param board    the board to write
     * @param filePath the path to the file
     * @throws IOException if an I/O error occurs
     */
    void writeBoard(Board board, Path filePath) throws IOException;
}
