package edu.ntnu.iir.bidata.model;

import java.util.Map;

public class TileStyling {
  private String color = "#FFFFFF"; // Default color is white
  private String imagePath = null; // Default image path is null
  private double imageRotation = 0;  // degrees

  public TileStyling() {
  }

  public TileStyling(Map<String, Object> styling) {
    this.color = (String) styling.get("color");
    this.imagePath = (String) styling.get("imagePath");
    this.imageRotation = (Double) styling.get("imageRotation");
  }

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public String getImagePath() {
    return imagePath;
  }

  public void setImagePath(String imagePath) {
    this.imagePath = imagePath;
  }

  public double getImageRotation() {
    return imageRotation;
  }

  public void setImageRotation(double imageRotation) {
    this.imageRotation = imageRotation;
  }
}
