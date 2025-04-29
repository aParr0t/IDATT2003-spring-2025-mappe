package edu.ntnu.iir.bidata.model;

/**
 * Represents a player in the game.
 */
public class Player {
  private String name;
  private int position;
  private PlayingPiece playingPiece;

  public Player(String name) {
    this(name, null); // Using null as default, can be changed later via setter
  }

  /**
   * Creates a new player with a given name.
   *
   * @param name the player's name
   */
  public Player(String name, PlayingPiece playingPiece) {
    this.name = name;
    this.position = 1;
    this.playingPiece = playingPiece;
  }

  /**
   * Moves the player forward a certain number of steps.
   * If the steps are negative, the player does not move.
   *
   * @param steps the number of steps to move forward
   */
  public void move(int steps) {
    if (steps > 0) {
      this.position += steps;
    }
    // Hvis steps er negativt, skjer ingenting
  }

  public int getPosition() {
    return position;
  }

  public void setPosition(int position) {
    this.position = position;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public PlayingPiece getPlayingPiece() {
    return playingPiece;
  }

  public void setPlayingPiece(PlayingPiece playingPiece) {
    this.playingPiece = playingPiece;
  }
}
