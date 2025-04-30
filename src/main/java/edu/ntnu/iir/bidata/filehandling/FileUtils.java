package edu.ntnu.iir.bidata.filehandling;

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
   * @throws IOException if an I/O error occurs
   */
  public static void ensureDirectoryExists(Path directory) throws IOException {
    if (!Files.exists(directory)) {
      Files.createDirectories(directory);
    }
  }
}
