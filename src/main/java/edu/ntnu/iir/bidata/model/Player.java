package edu.ntnu.iir.bidata.model;

/**
 * Represents a player in the game.
 * Each player has a name, position on the board, and an associated playing piece.
 */
public class Player {
  private String name;
  private int position;
  private PlayingPiece playingPiece;

  /**
   * Creates a new player with the given name and no playing piece.
   *
   * @param name the player's name
   */
  public Player(String name) {
    this(name, null); // Using null as default, can be changed later via setter
  }

  /**
   * Creates a new player with a given name and playing piece.
   * The player's starting position is set to 1.
   *
   * @param name the player's name
   * @param playingPiece the playing piece representing this player on the board
   */
  public Player(String name, PlayingPiece playingPiece) {
    this.name = name;
    this.position = 1;
    this.playingPiece = playingPiece;
  }

  /**
   * Returns the player's current position on the board.
   *
   * @return the current position
   */
  public int getPosition() {
    return position;
  }

  /**
   * Sets the player's position on the board.
   *
   * @param position the new position
   */
  public void setPosition(int position) {
    this.position = position;
  }

  /**
   * Returns the player's name.
   *
   * @return the player's name
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the player's name.
   *
   * @param name the new name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Returns the playing piece that represents this player on the board.
   *
   * @return the player's playing piece
   */
  public PlayingPiece getPlayingPiece() {
    return playingPiece;
  }

  /**
   * Sets the playing piece that represents this player on the board.
   *
   * @param playingPiece the new playing piece
   */
  public void setPlayingPiece(PlayingPiece playingPiece) {
    this.playingPiece = playingPiece;
  }
}
