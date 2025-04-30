package edu.ntnu.iir.bidata.exceptions;

/**
 * Exception thrown when there is an error with board data.
 * This can be used for issues like invalid board data format, missing required fields,
 * or other board-related errors.
 */
public class BoardDataException extends Exception {
    
    /**
     * Constructs a new BoardDataException with the specified detail message.
     * 
     * @param message the detail message
     */
    public BoardDataException(String message) {
        super(message);
    }
    
    /**
     * Constructs a new BoardDataException with the specified detail message and cause.
     * 
     * @param message the detail message
     * @param cause the cause of the exception
     */
    public BoardDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
