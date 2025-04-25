package edu.ntnu.iir.bidata.view.gui.games;

import edu.ntnu.iir.bidata.model.Board;
import edu.ntnu.iir.bidata.model.Player;
import edu.ntnu.iir.bidata.view.gui.BoardCanvas;

import java.util.List;

public class MonopolyBoard extends BoardCanvas {
  private Runnable onAnimationCompleteCallback;

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
  
  /**
   * Updates the players on the board with animation.
   * This method automatically handles tracking previous positions.
   * 
   * @param players The list of players to display
   * @param onComplete Callback to run when animation completes
   */
  public void updatePlayersWithAnimation(List<Player> players, Runnable onComplete) {
    // Store the callback
    this.onAnimationCompleteCallback = onComplete;
    
    // Update with new players - position tracking is handled in the parent class
    setPlayers(players);
    
    // Start animation
    animatePlayers(() -> {
      if (onAnimationCompleteCallback != null) {
        onAnimationCompleteCallback.run();
      }
    });
  }
}
