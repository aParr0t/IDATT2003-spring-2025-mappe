package edu.ntnu.iir.bidata.view.gui.games;

import edu.ntnu.iir.bidata.model.Board;
import edu.ntnu.iir.bidata.model.Player;
import edu.ntnu.iir.bidata.view.gui.BoardCanvas;
import java.util.List;

/**
 * A specialized canvas for rendering a Monopoly game board.
 * This class extends the general BoardCanvas and adds Monopoly-specific
 * rendering and animation functionality.
 */
public class MonopolyBoard extends BoardCanvas {
  private Runnable onAnimationCompleteCallback;

  /**
   * Constructs a new Monopoly board with the specified board data.
   *
   * @param board the game board containing tile data to be rendered
   */
  public MonopolyBoard(Board board) {
    super(board);
    this.showTileNumbers = false; // Hide tile numbers for Monopoly board
  }

  /**
   * Renders all components of the Monopoly board.
   * Clears the canvas first, then draws tiles and players if available.
   */
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
   * This method automatically handles tracking previous positions and executes
   * the provided callback when animation completes.
   *
   * @param players    the list of players to display
   * @param onComplete callback to run when animation completes
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
