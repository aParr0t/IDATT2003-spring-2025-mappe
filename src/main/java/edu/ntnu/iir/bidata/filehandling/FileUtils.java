package edu.ntnu.iir.bidata.filehandling;

import edu.ntnu.iir.bidata.exceptions.DirectoryCreationException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Utility class for file operations.
 */
public class FileUtils {
  private FileUtils() {
    // Private constructor to prevent instantiation
  }

  /**
   * Ensures that the directory exists, creating it if necessary.
   *
   * @param directory the directory to ensure exists
   * @throws DirectoryCreationException if the directory cannot be created
   */
  public static void ensureDirectoryExists(Path directory) throws DirectoryCreationException {
    if (!Files.exists(directory)) {
      try {
        Files.createDirectories(directory);
      } catch (IOException e) {
        throw new DirectoryCreationException(directory, e);
      }
    }
  }
}
