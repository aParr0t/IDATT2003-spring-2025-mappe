package edu.ntnu.iir.bidata.view.GUI;

import edu.ntnu.iir.bidata.model.Board;
import javafx.scene.canvas.Canvas;

public abstract class BoardCanvas extends Canvas {
  protected final Board board;

  public BoardCanvas(Board board) {
    this.board = board;
    // Set default size if not specified otherwise
    setWidth(500);
    setHeight(500);

    // Add listener to redraw when the canvas size changes
    widthProperty().addListener(observable -> draw());
    heightProperty().addListener(observable -> draw());

    draw();
  }

  public abstract void draw();
}
