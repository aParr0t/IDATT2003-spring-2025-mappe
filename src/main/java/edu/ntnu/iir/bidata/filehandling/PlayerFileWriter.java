package edu.ntnu.iir.bidata.filehandling;

import edu.ntnu.iir.bidata.model.Player;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Interface for writing player data to files.
 */
public interface PlayerFileWriter {
  /**
   * Writes a list of players to a file.
   *
   * @param players  the list of players to write
   * @param filePath the path to the file
   * @throws IOException if an I/O error occurs
   */
  void writePlayers(List<Player> players, Path filePath) throws IOException;
}
