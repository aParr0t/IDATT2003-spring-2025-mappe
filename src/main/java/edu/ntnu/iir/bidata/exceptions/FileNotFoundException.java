package edu.ntnu.iir.bidata.exceptions;

import java.nio.file.Path;

/**
 * Exception thrown when a file is not found.
 * This provides more specific information than the generic IOException.
 */
public class FileNotFoundException extends Exception {

  private final Path filePath;

  /**
   * Constructs a new FileNotFoundException with the specified file path and cause.
   *
   * @param filePath the path of the file that was not found
   * @param cause    the cause of the exception
   */
  public FileNotFoundException(Path filePath, Throwable cause) {
    super("File not found: " + filePath, cause);
    this.filePath = filePath;
  }

  /**
   * Gets the path of the file that was not found.
   *
   * @return the file path
   */
  public Path getFilePath() {
    return filePath;
  }
}
