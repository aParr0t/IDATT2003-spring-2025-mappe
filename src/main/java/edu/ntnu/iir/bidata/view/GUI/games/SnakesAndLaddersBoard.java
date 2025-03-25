package edu.ntnu.iir.bidata.view.GUI.games;

import edu.ntnu.iir.bidata.model.Board;
import edu.ntnu.iir.bidata.model.Tile;
import javafx.scene.canvas.Canvas;

public class SnakesAndLaddersBoard extends Canvas {
  private final Board board;

  public SnakesAndLaddersBoard(Board board) {
    this.board = board;
    // Set default size if not specified otherwise
    setWidth(500);
    setHeight(500);

    // Add listener to redraw when the canvas size changes
    widthProperty().addListener(observable -> draw());
    heightProperty().addListener(observable -> draw());

    draw();
  }

  private void drawTiles() {
    // Draw the tiles
    for (Tile tile : board.getTiles()) {
      // Get the normalized position (0-1 range)
      double normalizedX = tile.getPosition().getX();
      double normalizedY = tile.getPosition().getY();
      double normalizedWidth = tile.getWidth();
      double normalizedHeight = tile.getHeight();

      // Scale to actual canvas size
      double canvasX = normalizedX * getWidth();
      double canvasY = normalizedY * getHeight();
      double tileWidth = normalizedWidth * getWidth();
      double tileHeight = normalizedHeight * getHeight();

      // Get the graphics context for drawing
      var gc = getGraphicsContext2D();

      // Draw the tile background (rectangle)
      gc.setFill(javafx.scene.paint.Color.LIGHTGRAY);
      gc.fillRect(canvasX, canvasY, tileWidth, tileHeight);

      // Draw tile border
      gc.setStroke(javafx.scene.paint.Color.BLACK);
      gc.strokeRect(canvasX, canvasY, tileWidth, tileHeight);

      // Draw the tile number
      gc.setFill(javafx.scene.paint.Color.BLACK);
      gc.fillText(String.valueOf(tile.getId()),
              canvasX + (tileWidth / 2) - 5,  // Better centering horizontally
              canvasY + (tileHeight / 2) + 5); // Better centering vertically
    }
  }

  public void draw() {
    // Clear the canvas
    getGraphicsContext2D().clearRect(0, 0, getWidth(), getHeight());
    // Draw all tiles
    drawTiles();
  }
}
