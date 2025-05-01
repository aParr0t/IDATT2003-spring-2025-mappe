package edu.ntnu.iir.bidata.model;

/**
 * Response object that encapsulates the result of a player configuration validation.
 * Contains information about whether the configuration is valid and any error message
 * if validation failed.
 */
public record PlayerConfigResponse(
    /**
     * Flag indicating if the player configuration is valid.
     */
    boolean isPlayerConfigOk,
    
    /**
     * Error message detailing why the configuration failed validation.
     * Empty or null if the configuration is valid.
     */
    String errorMessage) {
}
