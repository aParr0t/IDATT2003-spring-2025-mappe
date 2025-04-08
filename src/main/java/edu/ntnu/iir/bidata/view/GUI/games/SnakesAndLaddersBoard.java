package edu.ntnu.iir.bidata.view.GUI.games;

import edu.ntnu.iir.bidata.model.Board;
import edu.ntnu.iir.bidata.model.Tile;
import edu.ntnu.iir.bidata.model.TileAction.MoveAction;
import edu.ntnu.iir.bidata.utils.RandomMath;
import edu.ntnu.iir.bidata.view.GUI.BoardCanvas;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.List;

public class SnakesAndLaddersBoard extends BoardCanvas {
  public SnakesAndLaddersBoard(Board board) {
    super(board);
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

      // determine tile color
      boolean hasAction = tile.getAction() != null;
      Color fillColor = Color.WHITE;
      if (hasAction) {
        boolean isMoveAction = tile.getAction() instanceof MoveAction;
        if (isMoveAction) {
          int start = ((MoveAction) tile.getAction()).getStart();
          int end = ((MoveAction) tile.getAction()).getEnd();
          boolean isLadder = start < end;
          fillColor = isLadder ? Color.LIGHTGREEN : Color.LIGHTCORAL;
        }
      }

      // styling
      gc.setStroke(Color.BLACK);
      gc.setLineWidth(1);

      // Draw the tile background (rectangle)
      gc.setFill(fillColor);
      gc.fillRect(canvasX, canvasY, tileWidth, tileHeight);

      // Draw tile border
      gc.setStroke(Color.BLACK);
      gc.strokeRect(canvasX, canvasY, tileWidth, tileHeight);

      // Draw the tile number
      gc.setFill(Color.BLACK);
      gc.fillText(String.valueOf(tile.getId()),
              canvasX + (tileWidth / 2) - 5,  // Better centering horizontally
              canvasY + (tileHeight / 2) + 5); // Better centering vertically
    }
  }

  public void drawSnakesAndLadders() {
    for (Tile tile : board.getTiles()) {
      double normalizedWidth = tile.getWidth();

      // determine tile color
      boolean hasAction = tile.getAction() != null;
      if (hasAction) {
        boolean isMoveAction = tile.getAction() instanceof MoveAction;
        if (isMoveAction) {
          int start = ((MoveAction) tile.getAction()).getStart();
          int end = ((MoveAction) tile.getAction()).getEnd();
          Tile endTile = board.getTile(end);
          Point2D centerOffset = new Point2D(1, 1).multiply(normalizedWidth / 2);
          Point2D startTilePos = tile.getPosition().add(centerOffset);
          Point2D endTilePos = endTile.getPosition().add(centerOffset);
          boolean isLadder = start < end;

          if (isLadder) {
            drawLadder(startTilePos, endTilePos, normalizedWidth * 0.7);
          } else {
            drawSnake(startTilePos, endTilePos, normalizedWidth * 0.15);
          }
        }
      }
    }
  }

  private void drawLadder(Point2D start, Point2D end, double ladderWidth) {
    // canvas width and height
    double canvasWidth = getWidth();
    double canvasHeight = getHeight();
    GraphicsContext gc = getGraphicsContext2D();

    // styling
    List<Color> ladderColors = List.of(
            Color.rgb(205, 127, 50),
            Color.rgb(165, 42, 42),
            Color.rgb(218, 160, 109),
            Color.rgb(233, 116, 81),
            Color.rgb(123, 63, 0),
            Color.rgb(123, 63, 0)
    );
    Color ladderColor = RandomMath.randomPick(ladderColors).orElse(Color.BROWN);
    gc.setStroke(ladderColor);
    gc.setLineWidth(5);  // Make ladder thicker

    // vector from start to end
    Point2D vector = end.subtract(start);
    // vector perpendicular to the ladder
    Point2D perpendicular = new Point2D(-vector.getY(), vector.getX()).normalize();
    // offset for the ladder legs. One of them is right, the other left (doesn't matter which)
    Point2D leg1Offset = perpendicular.multiply(ladderWidth / 2);
    Point2D leg2Offset = perpendicular.multiply(-ladderWidth / 2);

    // draw leg 1
    double x1 = (start.getX() + leg1Offset.getX()) * canvasWidth;
    double y1 = (start.getY() + leg1Offset.getY()) * canvasHeight;
    double x2 = (end.getX() + leg1Offset.getX()) * canvasWidth;
    double y2 = (end.getY() + leg1Offset.getY()) * canvasHeight;
    gc.strokeLine(x1, y1, x2, y2);

    // draw leg 2
    x1 = (start.getX() + leg2Offset.getX()) * canvasWidth;
    y1 = (start.getY() + leg2Offset.getY()) * canvasHeight;
    x2 = (end.getX() + leg2Offset.getX()) * canvasWidth;
    y2 = (end.getY() + leg2Offset.getY()) * canvasHeight;
    gc.strokeLine(x1, y1, x2, y2);

    // draw the steps
    double stepGap = ladderWidth;
    Point2D stepGapOffset = vector.normalize().multiply(stepGap);
    Point2D stepVector = vector.subtract(stepGapOffset);
    int stepCount = Math.max(2, (int) (stepVector.magnitude() / stepGap) + 1);
    for (int i = 0; i < stepCount; i++) {
      Point2D stepCenter = start.add(stepGapOffset.multiply(i + 0.5));
      Point2D stepStart = stepCenter.add(leg1Offset);
      Point2D stepEnd = stepCenter.add(leg2Offset);
      x1 = stepStart.getX() * canvasWidth;
      y1 = stepStart.getY() * canvasHeight;
      x2 = stepEnd.getX() * canvasWidth;
      y2 = stepEnd.getY() * canvasHeight;
      gc.strokeLine(x1, y1, x2, y2);
    }
  }

  /**
   * Draw a snake from start to end with a wave effect.
   * (Help from AI. Generated code for wave effect)
   *
   * @param start                The start position of the snake.
   * @param end                  The end position of the snake.
   * @param normalizedSnakeWidth The normalized width of the snake.
   */
  private void drawSnake(Point2D start, Point2D end, double normalizedSnakeWidth) {
    // canvas width and height
    double canvasWidth = getWidth();
    double canvasHeight = getHeight();
    GraphicsContext gc = getGraphicsContext2D();

    // styling
    List<Color> snakeColors = List.of(
            Color.rgb(182, 70, 95),
            Color.rgb(217, 154, 197),
            Color.rgb(138, 132, 226),
            Color.rgb(194, 232, 18),
            Color.rgb(239, 118, 122)
    );
    Color snakeColor = RandomMath.randomPick(snakeColors).orElse(Color.PURPLE);
    gc.setStroke(snakeColor);
    gc.setFill(snakeColor);
    double snakeWidth = normalizedSnakeWidth * canvasWidth;
    gc.setLineWidth(snakeWidth);  // Make snake body thick

    // vector from start to end
    Point2D vector = end.subtract(start);

    double length = vector.magnitude();  // length of the direct path
    Point2D direction = vector.normalize(); // normalized direction vector
    // perpendicular vector for wave effect
    Point2D perpendicular = new Point2D(-direction.getY(), direction.getX());

    // Wave parameters
    int segments = 20; // Number of line segments to draw
    double amplitude = normalizedSnakeWidth * 2; // Wave height
    double frequency = 3.0; // Number of waves

    // Create snake path using sine wave
    double[] xPoints = new double[segments + 1];
    double[] yPoints = new double[segments + 1];

    for (int i = 0; i <= segments; i++) {
      double t = (double) i / segments; // parametric value between 0 and 1

      // Position along direct path
      Point2D pos = start.add(direction.multiply(t * length));

      // Add sine wave perpendicular to path direction
      double wave = amplitude * Math.sin(t * 2 * Math.PI * frequency);
      Point2D waveOffset = perpendicular.multiply(wave);
      Point2D finalPos = pos.add(waveOffset);

      // Convert to canvas coordinates
      xPoints[i] = finalPos.getX() * canvasWidth;
      yPoints[i] = finalPos.getY() * canvasHeight;
    }

    // Draw snake body
    gc.setLineCap(javafx.scene.shape.StrokeLineCap.ROUND);
    for (int i = 0; i < segments; i++) {
      gc.strokeLine(xPoints[i], yPoints[i], xPoints[i + 1], yPoints[i + 1]);
    }

    // Draw snake head (larger circle)
    double headSize = snakeWidth * 2;
    gc.setFill(snakeColor.brighter());
    gc.fillOval(xPoints[0] - headSize / 2, yPoints[0] - headSize / 2, headSize, headSize);
  }

  @Override
  public void draw() {
    getGraphicsContext2D().clearRect(0, 0, getWidth(), getHeight()); // Clear the canvas
    drawTiles();
    drawSnakesAndLadders();
    if (!players.isEmpty()) {
      drawPlayers(players);
    }
  }
}
