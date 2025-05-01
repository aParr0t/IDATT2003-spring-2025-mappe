package edu.ntnu.iir.bidata.view.gui.screens;

import edu.ntnu.iir.bidata.model.Board;
import edu.ntnu.iir.bidata.model.GameType;
import javafx.scene.layout.StackPane;

/**
 * Factory for creating game screen instances based on game type.
 * This follows the Factory Pattern to encapsulate screen creation logic.
 */
public class GameScreenFactory {
    
    /**
     * Creates a new game screen instance based on the specified game type.
     *
     * @param gameType the type of game screen to create
     * @param board the board to use for the game screen
     * @return a new game screen instance as a StackPane
     * @throws IllegalArgumentException if the game type is not supported
     */
    public static StackPane createGameScreen(GameType gameType, Board board) {
        if (gameType == null) {
            throw new IllegalArgumentException("Game type cannot be null");
        }
        
        if (board == null) {
            throw new IllegalArgumentException("Board cannot be null");
        }
        
        return switch (gameType) {
            case SNAKES_AND_LADDERS -> new SnakesAndLaddersScreen(board);
            case MONOPOLY -> new MonopolyScreen(board);
            default -> throw new IllegalArgumentException("Unsupported game type: " + gameType);
        };
    }
}
