package edu.ntnu.iir.bidata.exceptions;

/**
 * Exception thrown when there is an error parsing a JSON file.
 * This exception wraps the underlying JSON library's exception,
 * making it easier to switch JSON libraries in the future.
 */
public class JsonParsingException extends FileParsingException {

  /**
   * Constructs a new JsonParsingException with the specified detail message and cause.
   *
   * @param message the detail message
   * @param cause   the cause of the exception
   */
  public JsonParsingException(String message, Throwable cause) {
    super(message, cause);
  }
}
