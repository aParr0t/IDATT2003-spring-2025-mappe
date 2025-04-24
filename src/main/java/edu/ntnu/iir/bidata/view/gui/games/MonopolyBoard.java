package edu.ntnu.iir.bidata.view.gui.games;

import edu.ntnu.iir.bidata.model.Board;
import edu.ntnu.iir.bidata.view.gui.BoardCanvas;

public class MonopolyBoard extends BoardCanvas {
  public MonopolyBoard(Board board) {
    super(board);
  }

  @Override
  public void draw() {
    clearCanvas();
    drawTiles();
    if (!players.isEmpty()) {
      drawPlayers(players);
    }
  }
}
