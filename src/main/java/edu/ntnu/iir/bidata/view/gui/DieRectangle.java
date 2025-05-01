package edu.ntnu.iir.bidata.view.gui;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;

/**
 * A custom JavaFX component that represents a die with dots.
 * The number of dots (1-6) determines the display pattern.
 */
public class DieRectangle extends Pane {
  private final int dotCount;
  private final double size;
  private final double dotRadius;

  // Standard die dot positions (relative coordinates from 0.0 to 1.0)
  private static final double[][][] DOT_POSITIONS = {
          {}, // Empty for index 0 (not used)
          {{0.5, 0.5}}, // 1 dot
          {{0.25, 0.25}, {0.75, 0.75}}, // 2 dots
          {{0.25, 0.25}, {0.5, 0.5}, {0.75, 0.75}}, // 3 dots
          {{0.25, 0.25}, {0.75, 0.25}, {0.25, 0.75}, {0.75, 0.75}}, // 4 dots
          {{0.25, 0.25}, {0.75, 0.25}, {0.5, 0.5}, {0.25, 0.75}, {0.75, 0.75}}, // 5 dots
          {{0.25, 0.25}, {0.75, 0.25}, {0.25, 0.5}, {0.75, 0.5}, {0.25, 0.75}, {0.75, 0.75}} // 6 dots
  };

  /**
   * Creates a new die with the specified number of dots and size.
   *
   * @param dotCount The number of dots to display (1-6)
   * @param size     The size of the die (width and height in pixels)
   */
  public DieRectangle(int dotCount, double size) {
    if (dotCount < 1 || dotCount > 6) {
      throw new IllegalArgumentException("Dot count must be between 1 and 6, but got " + dotCount);
    }

    this.dotCount = dotCount;
    this.size = size;
    this.dotRadius = size * 0.08; // Dot size proportional to die size

    // Create the background rectangle
    Rectangle background = new Rectangle(size, size);
    background.setFill(Color.WHITE);
    background.setStroke(Color.BLACK);
    background.setArcWidth(size * 0.2);
    background.setArcHeight(size * 0.2);

    getChildren().add(background);
    drawDots();
  }

  /**
   * Draws the appropriate number of dots on the die.
   */
  private void drawDots() {
    double[][] positions = DOT_POSITIONS[dotCount];

    for (double[] pos : positions) {
      double relativeX = pos[0];
      double relativeY = pos[1];

      // Convert relative position to absolute coordinates
      double x = relativeX * size;
      double y = relativeY * size;

      // Create and position the dot
      Ellipse dot = new Ellipse(x, y, dotRadius, dotRadius);
      dot.setFill(Color.BLACK);

      getChildren().add(dot);
    }
  }
}
