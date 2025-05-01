package edu.ntnu.iir.bidata.model;

/**
 * Represents styling information for a game tile, including color, image path, and image rotation.
 * This class provides customization options for the visual appearance of tiles in the game.
 */
public class TileStyling {
  private String color = "#FFFFFF";
  private String imagePath = null;
  private double imageRotation = 0;

  /**
   * Constructs a new TileStyling instance with default values.
   * Default color is white (#FFFFFF), no image, and 0 degrees rotation.
   */
  public TileStyling() {
  }

  /**
   * Gets the color of the tile.
   *
   * @return the hex color code as a String
   */
  public String getColor() {
    return color;
  }

  /**
   * Sets the color of the tile.
   *
   * @param color the hex color code as a String
   */
  public void setColor(String color) {
    this.color = color;
  }

  /**
   * Gets the path to the tile's image.
   *
   * @return the image path as a String, or null if no image is set
   */
  public String getImagePath() {
    return imagePath;
  }

  /**
   * Sets the path to the tile's image.
   *
   * @param imagePath the image path as a String
   */
  public void setImagePath(String imagePath) {
    this.imagePath = imagePath;
  }

  /**
   * Gets the rotation angle of the tile's image in degrees.
   *
   * @return the rotation angle in degrees
   */
  public double getImageRotation() {
    return imageRotation;
  }

  /**
   * Sets the rotation angle of the tile's image.
   *
   * @param imageRotation the rotation angle in degrees
   */
  public void setImageRotation(double imageRotation) {
    this.imageRotation = imageRotation;
  }
}
