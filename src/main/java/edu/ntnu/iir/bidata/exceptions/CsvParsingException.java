package edu.ntnu.iir.bidata.exceptions;

/**
 * Exception thrown when there is an error parsing a CSV file.
 * This exception wraps the underlying CSV library's exception,
 * making it easier to switch CSV parsing libraries in the future.
 */
public class CsvParsingException extends FileParsingException {

  /**
   * Constructs a new CsvParsingException with the specified detail message.
   *
   * @param message the detail message
   */
  public CsvParsingException(String message) {
    super(message);
  }
}
