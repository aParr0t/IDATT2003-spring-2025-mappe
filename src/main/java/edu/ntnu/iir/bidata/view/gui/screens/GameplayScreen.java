package edu.ntnu.iir.bidata.view.gui.screens;

import edu.ntnu.iir.bidata.model.*;
import javafx.scene.layout.StackPane;

import java.util.List;
import java.util.Map;

public class GameplayScreen extends StackPane {
  public GameplayScreen(GameType gameType, List<Player> players, Board board,
                        List<Integer> diceCounts, Player currentPlayer,
                        Map<String, Integer> previousPositions) {
    StackPane stackPane;
    switch (gameType) {
      case SNAKES_AND_LADDERS:
        stackPane = new SnakesAndLaddersScreen(players, board, diceCounts, currentPlayer, previousPositions);
        break;
      case MONOPOLY:
        stackPane = new MonopolyScreen(players, board, diceCounts, currentPlayer, previousPositions);
        break;
      default:
        stackPane = new StackPane();
    }

    this.getChildren().add(stackPane);
  }
}
