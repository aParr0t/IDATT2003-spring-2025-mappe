package edu.ntnu.iir.bidata.view.gui;

import edu.ntnu.iir.bidata.model.GameType;

public class GameTypePreview {
  private final String name;
  private final String imagePath;
  private final GameType gameType;  // this should be an enum or an object

  public GameTypePreview(String name, String imagePath, GameType gameType) {
    this.name = name;
    this.imagePath = imagePath;
    this.gameType = gameType;
  }

  public String getName() {
    return name;
  }

  public String getImagePath() {
    return imagePath;
  }

  public GameType getGameType() {
    return gameType;
  }
}
