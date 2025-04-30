package edu.ntnu.iir.bidata.exceptions;

/**
 * Exception thrown when there is an error parsing a file.
 * This is a base class for more specific file parsing exceptions.
 */
public class FileParsingException extends Exception {
    
    /**
     * Constructs a new FileParsingException with the specified detail message.
     * 
     * @param message the detail message
     */
    public FileParsingException(String message) {
        super(message);
    }
    
    /**
     * Constructs a new FileParsingException with the specified detail message and cause.
     * 
     * @param message the detail message
     * @param cause the cause of the exception
     */
    public FileParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
