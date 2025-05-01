package edu.ntnu.iir.bidata.model;

import java.util.Map;

/**
 * Represents a playing piece in a board game.
 * Each piece has a specific type and an associated image for visual representation.
 */
public class PlayingPiece {
  private static final Map<PlayingPieceType, String> pieceImages = Map.of(
          PlayingPieceType.BOAT, "/images/playingPieces/boat.jpg",
          PlayingPieceType.CAR, "/images/playingPieces/car.jpg",
          PlayingPieceType.DOG, "/images/playingPieces/dog.jpg",
          PlayingPieceType.DUCK, "/images/playingPieces/duck.jpg",
          PlayingPieceType.HAT, "/images/playingPieces/hat.jpg",
          PlayingPieceType.PENGUIN, "/images/playingPieces/penguin.jpg"
  );

  private final PlayingPieceType type;
  private final String imagePath;

  /**
   * Creates a new playing piece with the specified type.
   *
   * @param type the type of playing piece to create
   */
  public PlayingPiece(PlayingPieceType type) {
    this.type = type;
    this.imagePath = pieceImages.get(type);
  }

  /**
   * Gets the type of this playing piece.
   *
   * @return the type of this playing piece
   */
  public PlayingPieceType getType() {
    return type;
  }

  /**
   * Gets the image path for this playing piece.
   *
   * @return the file path to the image representing this piece
   */
  public String getImagePath() {
    return imagePath;
  }
}