package edu.ntnu.iir.bidata.view.gui;

import edu.ntnu.iir.bidata.model.Board;
import edu.ntnu.iir.bidata.model.Player;
import edu.ntnu.iir.bidata.model.Tile;
import edu.ntnu.iir.bidata.utils.ImageLoadTester;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;


/**
 * Abstract base class for drawing board games on a JavaFX Canvas with animation support.
 * Handles rendering of tiles, players, and animations of player movements between tiles.
 */
public abstract class BoardCanvas extends Canvas implements AnimatedBoardCanvas {
  protected final Board board;
  protected List<Player> players = List.of();
  protected boolean showTileNumbers = true; // Controls whether tile numbers are displayed

  // Animation properties
  // (Help from AI: most animation logic was done with AI as this is not essential to the task)
  private boolean animating = false;
  // Use instance variable instead of static to allow proper position tracking
  private Map<String, Integer> previousPositions = new HashMap<>();
  // Track last known positions
  private final Map<String, Integer> lastKnownPositions = new HashMap<>();
  private final Map<Player, Double> animationProgress = new HashMap<>();
  private final AnimationTimer animationTimer;
  private Runnable onAnimationComplete;

  /**
   * Creates a new BoardCanvas with the specified game board.
   *
   * @param board the game board to display
   */
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

    // Preload all tile images asynchronously
    preloadImages();
  }

  /**
   * Preloads all tile images in the background to improve rendering performance.
   * Uses asynchronous loading to avoid blocking the UI thread.
   */
  private void preloadImages() {
    CompletableFuture.runAsync(() -> {
      // Preload all tile images
      for (Tile tile : board.getTiles()) {
        if (tile.getStyling() != null && tile.getStyling().getImagePath() != null) {
          ImageLoadTester.attemptLoadImage(tile.getStyling().getImagePath());
        }
      }
    });
  }

  /**
   * Preloads player piece images to improve rendering performance.
   */
  private void preloadPlayerImages() {
    for (Player player : players) {
      if (player != null && player.getPlayingPiece() != null
              && player.getPlayingPiece().getImagePath() != null) {
        ImageLoadTester.attemptLoadImage(player.getPlayingPiece().getImagePath());
      }
    }
  }

  /**
   * Sets the players to be displayed on the board.
   * Stores previous positions before updating, then updates last known positions.
   *
   * @param players the list of players to display
   */
  public void setPlayers(List<Player> players) {
    // Store previous positions before updating players list
    storePreviousPositions();
    this.players = players;
    // After setting players, update lastKnownPositions
    updateLastKnownPositions();
    // Preload player images
    preloadPlayerImages();
    draw();
  }

  /**
   * Updates the last known positions map with current player positions.
   */
  private void updateLastKnownPositions() {
    for (Player player : players) {
      if (player != null) {
        lastKnownPositions.put(player.getName(), player.getPosition());
      }
    }
  }

  /**
   * Animates player movement from their previous positions to current positions.
   * This method should be called after updating player positions.
   * (Help from AI: We got lots of help from AI with animation, as Java is way different from JS)
   *
   * @param onComplete Runnable to execute when animation completes
   */
  public void animatePlayers(Runnable onComplete) {
    startAnimation(onComplete);
  }

  /**
   * Stores the current positions of all players before they move.
   * This ensures we have a reference point for animation.
   * Call this before updating player positions.
   */
  protected void storePreviousPositions() {
    previousPositions = new HashMap<>(lastKnownPositions);
  }

  /**
   * Gets the previous position of a player before movement.
   *
   * @param player the player to get previous position for
   * @return the previous position of the player
   */
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

  /**
   * Starts the animation of player movements from previous to current positions.
   * Animation only runs if there are players with changed positions.
   * (Help from AI: We got lots of help from AI with animation, as Java is way different from JS)
   *
   * @param onComplete Runnable to execute when animation completes
   */
  @Override
  public void startAnimation(Runnable onComplete) {
    if (players.isEmpty()) {
      if (onComplete != null) {
        onComplete.run();
      }
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

  /**
   * Updates the animation progress for each player.
   * Called by the animation timer on each frame.
   * (Help from AI: We got lots of help from AI with animation, as Java is way different from JS)
   */
  private void updateAnimation() {
    if (!animating) {
      return;
    }

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
    }

    if (allComplete) {
      stopAnimation();
    }
  }

  /**
   * Stops the animation and executes the completion callback.
   * (Help from AI: We got lots of help from AI with animation, as Java is way different from JS)
   */
  private void stopAnimation() {
    animating = false;
    animationTimer.stop();

    // Update lastKnownPositions after animation completes
    updateLastKnownPositions();

    if (onAnimationComplete != null) {
      onAnimationComplete.run();
    }
  }

  /**
   * Draws the complete board, including tiles and players.
   * Abstract method to be implemented by subclasses.
   */
  public abstract void draw();

  /**
   * Draws the players on the board, handling animation of movement.
   *
   * @param players the list of players to draw
   */
  protected void drawPlayers(List<Player> players) {
    GraphicsContext gc = getGraphicsContext2D();

    for (int i = 0; i < players.size(); i++) {
      Player player = players.get(i);

      // Get the current position or animated position if animating
      int displayPosition = player.getPosition();
      if (animating && animationProgress.containsKey(player)) {
        int prevPos = getPreviousPosition(player);
        int currentPos = player.getPosition();
        double progress = animationProgress.get(player);

        // Use tile connections for animation path
        if (prevPos != currentPos) {
          // Find the tile for the previous position
          Tile prevTile = board.getTile(prevPos);
          Tile currentTile = board.getTile(currentPos);

          // If we're moving forward along connected tiles
          if (shouldAnimateAlongPath(prevTile, currentTile)) {
            // Use the tile connection path instead of direct interpolation
            displayPosition = getIntermediatePosition(prevTile, currentTile, progress);
          } else {
            // Use interpolation for a smooth transition (for dice moves or teleports)
            displayPosition = (int) Math.round(prevPos + (currentPos - prevPos) * progress);
          }
        }
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
      // Use modulo for safety with more than 4 players
      Point2D offset = offsets.get(i % offsets.size());
      Point2D playerPos = tile.getPosition().add(offset);
      double playerSize = Math.min(tileWidth, tileHeight) * 0.5 * getWidth();

      // Get the player image from cache
      String imagePath = player.getPlayingPiece().getImagePath();
      Image image = ImageLoadTester.attemptLoadImage(imagePath);

      if (image != null && !image.isError()) {
        gc.drawImage(
                image,
                playerPos.getX() * getWidth(),
                playerPos.getY() * getHeight(),
                playerSize,
                playerSize
        );
        // draw outline of the tile
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.strokeRect(
                playerPos.getX() * getWidth(),
                playerPos.getY() * getHeight(),
                playerSize,
                playerSize
        );
      }
    }
  }

  /**
   * Determines if animation should follow connected tile path.
   * (Help from AI: We got lots of help from AI with animation, as Java is way different from JS)
   *
   * @param prevTile    the tile the player is moving from
   * @param currentTile the tile the player is moving to
   * @return true if animation should follow connected path, false otherwise
   */
  private boolean shouldAnimateAlongPath(Tile prevTile, Tile currentTile) {
    // Check if current tile is reachable by following next tiles from prev
    Tile temp = prevTile;
    int maxSteps = board.getTiles().size(); // Prevent infinite loop

    for (int i = 0; i < maxSteps; i++) {
      if (temp == null) {
        return false;
      }

      // If the next tile is our target, we can animate along the path
      if (temp.getNextTile() != null && temp.getNextTile().getId() == currentTile.getId()) {
        return true;
      }
      temp = temp.getNextTile();
    }

    return false;
  }

  /**
   * Gets intermediate position following the path of connected tiles.
   * (Help from AI: We got lots of help from AI with animation, as Java is way different from JS)
   *
   * @param startTile the starting tile
   * @param endTile   the destination tile
   * @param progress  the animation progress (0.0 to 1.0)
   * @return the ID of the intermediate tile based on current progress
   */
  private int getIntermediatePosition(Tile startTile, Tile endTile, double progress) {
    if (progress >= 1.0) {
      return endTile.getId();
    }
    if (progress <= 0.0) {
      return startTile.getId();
    }

    // Count steps between tiles
    Tile temp = startTile;
    int steps = 0;
    int maxSteps = board.getTiles().size(); // Prevent infinite loop

    while (temp != null && temp.getId() != endTile.getId() && steps < maxSteps) {
      temp = temp.getNextTile();
      steps++;
    }

    if (steps == 0) {
      return endTile.getId();
    }

    // Calculate how many steps to move based on progress
    int stepsToMove = (int) Math.floor(progress * steps);

    // Move that many steps from start tile
    temp = startTile;
    for (int i = 0; i < stepsToMove && temp != null; i++) {
      temp = temp.getNextTile();
    }

    return temp != null ? temp.getId() : startTile.getId();
  }

  /**
   * Clears the entire canvas.
   */
  public void clearCanvas() {
    // Clear the canvas
    GraphicsContext gc = getGraphicsContext2D();
    gc.clearRect(0, 0, getWidth(), getHeight());
  }

  /**
   * Draws all tiles on the board with their appropriate styles and images.
   */
  public void drawTiles() {
    // Draw each tile on the board
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

      // First draw the background color (for all tiles)
      drawTileWithColor(gc, tile, canvasX, canvasY, tileWidth, tileHeight);

      // If there's an image, draw it on top of the background color
      if (tile.getStyling() != null && tile.getStyling().getImagePath() != null) {
        String imagePath = tile.getStyling().getImagePath();
        Image tileImage = ImageLoadTester.attemptLoadImage(imagePath);

        if (tileImage != null && !tileImage.isError()) {
          // Check if rotation needed
          double rotation = tile.getStyling().getImageRotation();
          if (rotation != 0) {
            // Save the current state
            gc.save();

            // Translate to the center of the tile for rotation
            gc.translate(canvasX + tileWidth / 2, canvasY + tileHeight / 2);
            // Rotate by the specified degrees
            gc.rotate(rotation);
            // Draw the image centered (adjust coordinates to account for rotation around center)

            // If the rotation is 90 or 270, we need to swap the width and height of the image
            if (rotation == 90 || rotation == 270) {
              gc.drawImage(tileImage, -tileHeight / 2, -tileWidth / 2, tileHeight, tileWidth);
            } else {
              gc.drawImage(tileImage, -tileWidth / 2, -tileHeight / 2, tileWidth, tileHeight);
            }

            // Restore the graphics context to its original state
            gc.restore();
          } else {
            // Draw normally if no rotation
            gc.drawImage(tileImage, canvasX, canvasY, tileWidth, tileHeight);
          }
        }
      }

      // Draw tile border (always on top)
      gc.setStroke(Color.BLACK);
      gc.setLineWidth(1);
      gc.strokeRect(canvasX, canvasY, tileWidth, tileHeight);

      // Draw the tile number (if enabled)
      if (showTileNumbers) {
        gc.setFill(Color.BLACK);
        gc.fillText(String.valueOf(tile.getId()),
                canvasX + (tileWidth / 2) - 5,  // Better centering horizontally
                canvasY + (tileHeight / 2) + 5); // Better centering vertically
      }
    }
  }

  /**
   * Draws a single tile with its background color.
   *
   * @param gc         the graphics context to draw on
   * @param tile       the tile to draw
   * @param canvasX    the x-coordinate on the canvas
   * @param canvasY    the y-coordinate on the canvas
   * @param tileWidth  the width of the tile
   * @param tileHeight the height of the tile
   */
  private void drawTileWithColor(
          GraphicsContext gc,
          Tile tile,
          double canvasX,
          double canvasY,
          double tileWidth,
          double tileHeight
  ) {
    // determine tile color
    Color fillColor = Color.WHITE;
    if (tile.getStyling() != null) {
      fillColor = Color.web(tile.getStyling().getColor());
    }

    // Fill with color
    gc.setFill(fillColor);
    gc.fillRect(canvasX, canvasY, tileWidth, tileHeight);
  }
}
