package edu.ntnu.iir.bidata.filehandling;

import edu.ntnu.iir.bidata.exceptions.BoardDataException;
import edu.ntnu.iir.bidata.exceptions.FileNotFoundException;
import edu.ntnu.iir.bidata.exceptions.JsonParsingException;
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
   * @throws IOException           if an I/O error occurs
   * @throws FileNotFoundException if the file does not exist
   * @throws JsonParsingException  if there is an error parsing the JSON
   * @throws BoardDataException    if there is an error with the board data
   */
  Board readBoard(Path filePath) 
      throws IOException, FileNotFoundException, 
             JsonParsingException, BoardDataException;
}
