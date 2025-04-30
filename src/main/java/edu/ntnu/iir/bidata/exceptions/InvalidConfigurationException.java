package edu.ntnu.iir.bidata.exceptions;

/**
 * Exception thrown when configuration data is invalid.
 * This can be used for any configuration-related errors, such as invalid board layouts,
 * player configurations, or game settings.
 */
public class InvalidConfigurationException extends Exception {
    
    /**
     * Constructs a new InvalidConfigurationException with the specified detail message.
     * 
     * @param message the detail message
     */
    public InvalidConfigurationException(String message) {
        super(message);
    }
    
    /**
     * Constructs a new InvalidConfigurationException with the specified detail message and cause.
     * 
     * @param message the detail message
     * @param cause the cause of the exception
     */
    public InvalidConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
