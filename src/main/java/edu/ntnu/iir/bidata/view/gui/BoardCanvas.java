package edu.ntnu.iir.bidata.view.gui;

import edu.ntnu.iir.bidata.model.Board;
import edu.ntnu.iir.bidata.model.Player;
import edu.ntnu.iir.bidata.model.Tile;
import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BoardCanvas extends Canvas implements AnimatedBoardCanvas {
  protected final Board board;
  protected List<Player> players = List.of();

  // Animation properties
  // (Help from AI: most animation logic was done with AI as this is not essential to the task)
  private boolean animating = false;
  // Use instance variable instead of static to allow proper position tracking
  private Map<String, Integer> previousPositions = new HashMap<>();
  private Map<Player, Double> animationProgress = new HashMap<>();
  private AnimationTimer animationTimer;
  private Runnable onAnimationComplete;

  public BoardCanvas(Board board) {
    this.board = board;
    // Set default size if not specified otherwise
    setWidth(500);
    setHeight(500);

    // Add listener to redraw when the canvas size changes
    widthProperty().addListener(observable -> draw());
    heightProperty().addListener(observable -> draw());

    // Initialize animation timer
    animationTimer = new AnimationTimer() {
      private long lastUpdate = 0;

      @Override
      public void handle(long now) {
        // Update approximately 60 times per second
        if (lastUpdate == 0 || now - lastUpdate >= 16_000_000) {
          updateAnimation();
          draw();
          lastUpdate = now;
        }
      }
    };
  }

  public void setPlayers(List<Player> players) {
    this.players = players;
    draw();
  }

  @Override
  public void setPreviousPositions(Map<String, Integer> previousPositions) {
    if (previousPositions == null) {
      this.previousPositions = new HashMap<>();
      return;
    }
    this.previousPositions = new HashMap<>(previousPositions);
  }

  private int getPreviousPosition(Player player) {
    if (player == null) {
      return 1; // Default position
    }

    Integer prevPos = previousPositions.get(player.getName());
    if (prevPos == null) {
      prevPos = player.getPosition(); // Default to current position
    }
    return prevPos;
  }

  @Override
  public void startAnimation(Runnable onComplete) {
    if (players.isEmpty()) {
      if (onComplete != null) onComplete.run();
      return;
    }

    // Reset any ongoing animation
    if (animating) {
      animationTimer.stop();
    }

    // Initialize animation for each player
    animationProgress.clear();
    boolean needsAnimation = false;

    for (Player player : players) {
      animationProgress.put(player, 0.0);

      int currentPos = player.getPosition();
      int prevPos = getPreviousPosition(player);

      if (prevPos != currentPos) {
        needsAnimation = true;
      }
    }

    this.onAnimationComplete = onComplete;

    if (needsAnimation) {
      animating = true;
      animationTimer.start();
    } else {
      if (onComplete != null) {
        onComplete.run();
      }
    }
  }

  private void updateAnimation() {
    if (!animating) return;

    boolean allComplete = true;

    for (Player player : players) {
      double progress = animationProgress.get(player);
      progress += 0.02; // Slightly slower for smoother animation

      if (progress >= 1.0) {
        progress = 1.0;
      } else {
        allComplete = false;
      }

      animationProgress.put(player, progress);

      // Debug output at key points
      if (progress > 0 && progress < 0.1 ||
              progress > 0.45 && progress < 0.55 ||
              progress > 0.9) {
        int currentPos = player.getPosition();
        int prevPos = getPreviousPosition(player);
        int animatedPos = (int) Math.round(prevPos + (currentPos - prevPos) * progress);
      }
    }

    if (allComplete) {
      stopAnimation();
    }
  }

  private void stopAnimation() {
    animating = false;
    animationTimer.stop();

    if (onAnimationComplete != null) {
      onAnimationComplete.run();
    }
  }

  @Override
  public boolean isAnimating() {
    return animating;
  }

  public abstract void draw();

  protected void drawPlayers(List<Player> players) {
    GraphicsContext gc = getGraphicsContext2D();

    for (int i = 0; i < players.size(); i++) {
      Player player = players.get(i);

      // Get the current and previous positions
      int currentPos = player.getPosition();
      int prevPos = getPreviousPosition(player);

      // If animating, interpolate between positions
      int displayPosition;
      if (animating && prevPos != currentPos) {
        double progress = animationProgress.getOrDefault(player, 0.0);
        // Use linear interpolation for position
        displayPosition = (int) Math.round(prevPos + (currentPos - prevPos) * progress);

        // Debug animation progress at specific moments
        if (progress > 0 && progress < 0.05 ||
                Math.abs(progress - 0.5) < 0.05 ||
                progress > 0.95 && progress < 1.0) {
        }
      } else {
        displayPosition = currentPos;
      }

      // Make sure we have a valid tile position (safety check)
      if (displayPosition < 1 || displayPosition > board.getTiles().size()) {
        displayPosition = 1;
      }

      // Get the tile for the display position
      Tile tile = board.getTile(displayPosition);
      double tileWidth = tile.getWidth();
      double tileHeight = tile.getHeight();

      List<Point2D> offsets = List.of(
              new Point2D(0, 0),
              new Point2D(tileWidth / 2, 0),
              new Point2D(0, tileHeight / 2),
              new Point2D(tileWidth / 2, tileHeight / 2)
      );
      Point2D offset = offsets.get(i % offsets.size()); // Use modulo for safety with more than 4 players
      Point2D playerPos = tile.getPosition().add(offset);
      double playerSize = tileWidth * 0.5 * getWidth();
      String imagePath = player.getPlayingPiece().getImagePath();
      Image image = new Image(imagePath);
      gc.drawImage(image, playerPos.getX() * getWidth(), playerPos.getY() * getHeight(), playerSize, playerSize);
      // draw outline of the tile
      gc.setStroke(Color.BLACK);
      gc.setLineWidth(2);
      gc.strokeRect(playerPos.getX() * getWidth(), playerPos.getY() * getHeight(), playerSize, playerSize);
    }
  }

  public void clearCanvas() {
    // Clear the canvas
    GraphicsContext gc = getGraphicsContext2D();
    gc.clearRect(0, 0, getWidth(), getHeight());
  }

  public void drawTiles() {
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
      Color fillColor = Color.WHITE;
      if (tile.getStyling() != null) {
        fillColor = Color.web(tile.getStyling().getColor());
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
}
