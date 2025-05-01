package edu.ntnu.iir.bidata.view.gui.games;

import edu.ntnu.iir.bidata.model.Board;
import edu.ntnu.iir.bidata.model.Player;
import edu.ntnu.iir.bidata.model.Tile;
import edu.ntnu.iir.bidata.model.tileaction.MoveAction;
import edu.ntnu.iir.bidata.utils.RandomMath;
import edu.ntnu.iir.bidata.view.gui.BoardCanvas;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Canvas component for rendering a Snakes and Ladders game board.
 * Handles drawing the game board, snakes, ladders, and player pieces with animations.
 */
public class SnakesAndLaddersBoard extends BoardCanvas {
  private final Map<String, Color> snakeColors = new HashMap<>();
  private final Map<String, Color> ladderColors = new HashMap<>();
  private Runnable onAnimationCompleteCallback;

  private static final List<Color> SNAKE_COLOR_OPTIONS = List.of(
          Color.rgb(182, 70, 95),
          Color.rgb(217, 154, 197),
          Color.rgb(138, 132, 226),
          Color.rgb(194, 232, 18),
          Color.rgb(239, 118, 122)
  );

  private static final List<Color> LADDER_COLOR_OPTIONS = List.of(
          Color.rgb(205, 127, 50),
          Color.rgb(165, 42, 42),
          Color.rgb(218, 160, 109),
          Color.rgb(233, 116, 81),
          Color.rgb(123, 63, 0)
  );

  /**
   * Constructs a new SnakesAndLaddersBoard with the specified game board.
   *
   * @param board the game board to be rendered
   */
  public SnakesAndLaddersBoard(Board board) {
    super(board);
  }

  /**
   * Draws all snakes and ladders on the board based on tile actions.
   * Iterates through all tiles to find MoveAction instances and draws
   * either a snake or a ladder depending on whether the move is forward or backward.
   */
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
          Tile startTile = board.getTile(start);
          Tile endTile = board.getTile(end);
          Point2D centerOffset = new Point2D(1, 1).multiply(normalizedWidth / 2);
          Point2D startTilePos = startTile.getPosition().add(centerOffset);
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

  /**
   * Draws a ladder between two points on the board.
   * Creates a visual ladder with two side rails and multiple rungs between them.
   *
   * @param start       the starting point (bottom) of the ladder
   * @param end         the ending point (top) of the ladder
   * @param ladderWidth the width of the ladder
   */
  private void drawLadder(Point2D start, Point2D end, double ladderWidth) {
    // canvas width and height
    double canvasWidth = getWidth();
    final double canvasHeight = getHeight();
    GraphicsContext gc = getGraphicsContext2D();

    // Get or create color for this ladder
    String ladderKey = start.getX() + "," + start.getY() + "->" + end.getX() + "," + end.getY();
    // (Help from autocomplete: I didn't know about the computeIfAbsent method)
    Color ladderColor = ladderColors.computeIfAbsent(ladderKey,
            k -> RandomMath.randomPick(LADDER_COLOR_OPTIONS).orElse(Color.BROWN));

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
   * Draws a snake from start to end with a wave effect.
   * Creates a visual snake with a sinusoidal path and a distinct head.
   *
   * @param start                the starting point (head) of the snake
   * @param end                  the ending point (tail) of the snake
   * @param normalizedSnakeWidth the normalized width of the snake
   */
  private void drawSnake(Point2D start, Point2D end, double normalizedSnakeWidth) {
    // canvas width and height
    double canvasWidth = getWidth();
    final double canvasHeight = getHeight();
    GraphicsContext gc = getGraphicsContext2D();

    // Get or create color for this snake
    String snakeKey = start.getX() + "," + start.getY() + "->" + end.getX() + "," + end.getY();
    // (Help from autocomplete: I didn't know about the computeIfAbsent method)
    Color snakeColor = snakeColors.computeIfAbsent(snakeKey,
            k -> RandomMath.randomPick(SNAKE_COLOR_OPTIONS).orElse(Color.PURPLE));

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
    double[] xpoints = new double[segments + 1];
    double[] ypoints = new double[segments + 1];

    for (int i = 0; i <= segments; i++) {
      double t = (double) i / segments; // parametric value between 0 and 1

      // Position along direct path
      Point2D pos = start.add(direction.multiply(t * length));

      // Add sine wave perpendicular to path direction
      double wave = amplitude * Math.sin(t * 2 * Math.PI * frequency);
      Point2D waveOffset = perpendicular.multiply(wave);
      Point2D finalPos = pos.add(waveOffset);

      // Convert to canvas coordinates
      xpoints[i] = finalPos.getX() * canvasWidth;
      ypoints[i] = finalPos.getY() * canvasHeight;
    }

    // Draw snake body
    gc.setLineCap(javafx.scene.shape.StrokeLineCap.ROUND);
    for (int i = 0; i < segments; i++) {
      gc.strokeLine(xpoints[i], ypoints[i], xpoints[i + 1], ypoints[i + 1]);
    }

    // Draw snake head (larger circle)
    double headSize = snakeWidth * 2;
    gc.setFill(snakeColor.brighter());
    gc.fillOval(xpoints[0] - headSize / 2, ypoints[0] - headSize / 2, headSize, headSize);
  }

  /**
   * Renders the entire game board by drawing tiles, snakes, ladders, and players.
   * Overrides the draw method from the parent class to include game-specific elements.
   */
  @Override
  public void draw() {
    clearCanvas();
    drawTiles();
    drawSnakesAndLadders();
    if (!players.isEmpty()) {
      drawPlayers(players);
    }
  }

  /**
   * Updates the players on the board with animation.
   * This method automatically handles tracking previous positions.
   *
   * @param players    the list of players to display
   * @param onComplete callback to run when animation completes
   */
  public void updatePlayersWithAnimation(List<Player> players, Runnable onComplete) {
    // Store the callback
    this.onAnimationCompleteCallback = onComplete;

    // Update with new players - position tracking is handled in the parent class
    setPlayers(players);

    // Start animation
    animatePlayers(() -> {
      if (onAnimationCompleteCallback != null) {
        onAnimationCompleteCallback.run();
      }
    });
  }
}
