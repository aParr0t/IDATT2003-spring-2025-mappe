package edu.ntnu.iir.bidata.model;

import java.util.Map;

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

  public PlayingPiece(PlayingPieceType type) {
    this.type = type;
    this.imagePath = pieceImages.get(type);
  }

  public PlayingPieceType getType() {
    return type;
  }

  public String getImagePath() {
    return imagePath;
  }
}