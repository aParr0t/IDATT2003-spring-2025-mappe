package edu.ntnu.iir.bidata.model;

public class PlayerConfigResponse {
  private final boolean isPlayerConfigOk;
  private final String errorMessage;

  public PlayerConfigResponse(boolean isPlayerConfigOk, String errorMessage) {
    this.isPlayerConfigOk = isPlayerConfigOk;
    this.errorMessage = errorMessage;
  }

  public boolean isPlayerConfigOk() {
    return isPlayerConfigOk;
  }

  public String getErrorMessage() {
    return errorMessage;
  }
}
