package edu.ntnu.iir.bidata.model;

/**
 * Response object that encapsulates the result of a player configuration validation.
 * Contains information about whether the configuration is valid and any error message
 * if validation failed.
 */
public record PlayerConfigResponse(boolean isPlayerConfigOk, String errorMessage) {
}
