package edu.ntnu.iir.bidata.view.gui;


/**
 * Interface for board canvases that support animation
 */
public interface AnimatedBoardCanvas {
  /**
   * Starts the animation from previous positions to current positions
   *
   * @param onComplete Callback to run when animation completes
   */
  void startAnimation(Runnable onComplete);
} 