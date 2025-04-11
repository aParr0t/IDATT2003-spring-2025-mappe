package edu.ntnu.iir.bidata.model;

import java.util.Map;

public class TileStyling {
  private String color = "#FFFFFF"; // Default color is white

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
}
