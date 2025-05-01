package edu.ntnu.iir.bidata.exceptions;

import java.nio.file.Path;

/**
 * Exception thrown when a directory cannot be created.
 * This provides more specific information than the generic IOException.
 */
public class DirectoryCreationException extends Exception {

  /**
   * Constructs a new DirectoryCreationException with the specified directory path and cause.
   *
   * @param directoryPath the path of the directory that could not be created
   * @param cause         the cause of the exception
   */
  public DirectoryCreationException(Path directoryPath, Throwable cause) {
    super("Failed to create directory: " + directoryPath, cause);
  }
}
