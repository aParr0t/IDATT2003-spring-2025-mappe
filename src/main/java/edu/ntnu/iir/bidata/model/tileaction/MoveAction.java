package edu.ntnu.iir.bidata.model.tileaction;


public class MoveAction implements TileAction {
  private final int start;
  private final int end;

  public MoveAction(int start, int end) {
    this.start = start;
    this.end = end;
  }

  public int getStart() {
    return start;
  }

  public int getEnd() {
    return end;
  }
}
