package edu.ntnu.iir.bidata;

import edu.ntnu.iir.bidata.controller.BoardGameController;

public class Main {
  public static void main(String[] args) {
    BoardGameController app = new BoardGameController();
    app.setup();
    app.run();
  }
}