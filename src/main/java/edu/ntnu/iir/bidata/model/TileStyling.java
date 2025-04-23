package edu.ntnu.iir.bidata.model;

import java.util.Map;

public class TileStyling {
  private String color = "#FFFFFF"; // Default color is white
  private String imagePath = null; // Default image path is null

  public TileStyling() {
  }

  public TileStyling(Map<String, Object> styling) {
    this.color = (String) styling.get("color");
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
}
