package edu.ntnu.iir.bidata.model.TileAction;

import edu.ntnu.iir.bidata.model.Player;

public class MoveAction implements TileAction {
  private final int start;
  private final int end;

  public MoveAction(int start, int end) {
    this.start = start;
    this.end = end;
  }

  public void execute(Player player) {
    player.setPosition(end);
  }

  public int getStart() {
    return start;
  }

  public int getEnd() {
    return end;
  }
}
