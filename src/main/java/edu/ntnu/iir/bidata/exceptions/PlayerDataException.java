package edu.ntnu.iir.bidata.exceptions;

/**
 * Exception thrown when there is an error with player data.
 * This can be used for issues like invalid player data format, missing required fields,
 * or other player-related errors.
 */
public class PlayerDataException extends Exception {

  /**
   * Constructs a new PlayerDataException with the specified detail message and cause.
   *
   * @param message the detail message
   * @param cause   the cause of the exception
   */
  public PlayerDataException(String message, Throwable cause) {
    super(message, cause);
  }
}
