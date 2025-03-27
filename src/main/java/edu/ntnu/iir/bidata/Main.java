package edu.ntnu.iir.bidata;

import edu.ntnu.iir.bidata.controller.BoardGameApp;

public class Main {
  public static void main(String[] args) {
    BoardGameApp app = new BoardGameApp();
    app.setup();
    app.run();
  }
}