package edu.ntnu.iir.bidata.view.GUI;

import java.util.Map;

/**
 * Interface for board canvases that support animation
 */
public interface AnimatedBoardCanvas {
    /**
     * Sets the previous positions for the players
     * @param previousPositions Map of player names to their previous positions
     */
    void setPreviousPositions(Map<String, Integer> previousPositions);
    
    /**
     * Starts the animation from previous positions to current positions
     * @param onComplete Callback to run when animation completes
     */
    void startAnimation(Runnable onComplete);
    
    /**
     * Checks if the canvas is currently animating
     * @return true if animation is in progress
     */
    boolean isAnimating();
} 