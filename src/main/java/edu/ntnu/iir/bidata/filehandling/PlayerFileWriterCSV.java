package edu.ntnu.iir.bidata.filehandling;

import edu.ntnu.iir.bidata.exceptions.DirectoryCreationException;
import edu.ntnu.iir.bidata.model.Player;
import edu.ntnu.iir.bidata.model.PlayingPiece;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Implementation of PlayerFileWriter that writes players to a CSV file.
 */
public class PlayerFileWriterCSV implements PlayerFileWriter {
  @Override
  public void writePlayers(List<Player> players, Path filePath) throws IOException {
    // Ensure the parent directory exists
    try {
      FileUtils.ensureDirectoryExists(filePath.getParent());
    } catch (DirectoryCreationException e) {
      throw new IOException("Failed to create directory for file: " + filePath, e);
    }

    try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
      for (Player player : players) {
        String name = player.getName();
        PlayingPiece playingPiece = player.getPlayingPiece();

        if (playingPiece == null) {
          throw new IOException("Player " + name + " has no playing piece");
        }

        String pieceType = playingPiece.getType().name();
        writer.write(name + "," + pieceType);
        writer.newLine();
      }
    }
  }
}
