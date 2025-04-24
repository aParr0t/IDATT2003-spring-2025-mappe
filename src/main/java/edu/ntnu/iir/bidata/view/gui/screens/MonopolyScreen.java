package edu.ntnu.iir.bidata.view.gui.screens;

import edu.ntnu.iir.bidata.model.Board;
import edu.ntnu.iir.bidata.model.GameType;
import edu.ntnu.iir.bidata.model.Player;
import edu.ntnu.iir.bidata.view.gui.BoardCanvas;
import edu.ntnu.iir.bidata.view.gui.BoardCanvasFactory;
import edu.ntnu.iir.bidata.view.gui.DieRectangle;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.util.List;
import java.util.Map;

public class MonopolyScreen extends StackPane {
  private BoardCanvas boardCanvas;
  private VBox playerCardsSection;
  private HBox diceContainer;

  public MonopolyScreen(List<Player> players, Board board,
                        List<Integer> diceCounts, Player currentPlayer,
                        Map<String, Integer> previousPositions) {
    // Root layout
    BorderPane root = new BorderPane();

    // Center section - Game board
    boardCanvas = BoardCanvasFactory.createBoardCanvas(GameType.MONOPOLY, board);
    boardCanvas.setPlayers(players);
    boardCanvas.setWidth(600);
    boardCanvas.setHeight(600);

    StackPane boardPane = new StackPane(boardCanvas);
    boardPane.setPadding(new Insets(10));
    root.setCenter(boardPane);

    // Right sidebar
    VBox sidebar = new VBox(20);
    sidebar.setPadding(new Insets(20));
    sidebar.setStyle("-fx-background-color: #ffffff;");
    sidebar.setPrefWidth(250);

    // Title
    Label title = new Label("Players");
    title.setFont(new Font(20));
    sidebar.getChildren().add(title);

    // Player cards
    playerCardsSection = new VBox(10);
    playerCardsSection.setAlignment(Pos.CENTER);
    for (Player player : players) {
      boolean isActive = player.equals(currentPlayer);
      playerCardsSection.getChildren().add(createPlayerCard(player, isActive));
    }
    sidebar.getChildren().add(playerCardsSection);

    // Dice section
    VBox diceSection = new VBox(10);
    diceSection.setAlignment(Pos.CENTER);
    diceSection.setPadding(new Insets(20));

    diceContainer = new HBox(10);
    diceContainer.setAlignment(Pos.CENTER);

    for (Integer diceCount : diceCounts) {
      DieRectangle die = new DieRectangle(diceCount, 70);
      diceContainer.getChildren().add(die);
    }

    diceSection.getChildren().add(diceContainer);
    sidebar.getChildren().add(diceSection);

    // Game status
    VBox statusBox = new VBox(5);
    statusBox.setPadding(new Insets(20, 0, 0, 0));
    Label gameStatus = new Label("Game Status: In Progress");
    Label round = new Label("Round: 5");

    statusBox.getChildren().addAll(gameStatus, round);
    sidebar.getChildren().add(statusBox);

    root.setRight(sidebar);
    getChildren().add(root);
  }

  private HBox createPlayerCard(Player player, boolean isActive) {
    VBox card = new VBox(5);
    card.setPadding(new Insets(10));
    card.setStyle("-fx-border-color: " + (isActive ? "#4CAF50" : "lightgray") + "; -fx-border-width: 2px; -fx-border-radius: 5; -fx-background-color: #f9f9f9;");
    card.setPrefWidth(200);

    // Player name and money
    Label nameLabel = new Label(player.getName());
    nameLabel.setFont(new Font(14));
    Label moneyLabel = new Label("$" + 100);
    Label stats = new Label("🏠 " + 5 + "   🏘 " + 3);

    card.getChildren().addAll(nameLabel, moneyLabel, stats);
    return new HBox(card);
  }

  public void updateGameState(List<Player> players, List<Integer> diceCounts,
                            Player currentPlayer, Map<String, Integer> previousPositions) {
    // Update the board canvas with new player positions
    boardCanvas.setPlayers(players);

    // Update player cards section
    playerCardsSection.getChildren().clear();
    for (Player player : players) {
      boolean isActive = player.equals(currentPlayer);
      playerCardsSection.getChildren().add(createPlayerCard(player, isActive));
    }

    // Update dice section
    diceContainer.getChildren().clear();
    for (Integer diceCount : diceCounts) {
      DieRectangle die = new DieRectangle(diceCount, 70);
      diceContainer.getChildren().add(die);
    }
  }
}
