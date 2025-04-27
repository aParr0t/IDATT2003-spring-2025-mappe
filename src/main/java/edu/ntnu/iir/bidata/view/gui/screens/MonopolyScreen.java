package edu.ntnu.iir.bidata.view.gui.screens;

import edu.ntnu.iir.bidata.model.Board;
import edu.ntnu.iir.bidata.model.GameType;
import edu.ntnu.iir.bidata.model.Player;
import edu.ntnu.iir.bidata.view.AppEvent;
import edu.ntnu.iir.bidata.view.gui.BoardCanvasFactory;
import edu.ntnu.iir.bidata.view.gui.DieRectangle;
import edu.ntnu.iir.bidata.view.gui.GUIApp;
import edu.ntnu.iir.bidata.view.gui.games.MonopolyBoard;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.application.Platform;

import java.util.List;

public class MonopolyScreen extends StackPane {
  private MonopolyBoard boardCanvas;
  private VBox playerCardsSection;
  private HBox diceContainer;

  public MonopolyScreen(Board board) {
    // Root layout
    BorderPane root = new BorderPane();

    // Center section - Game board
    boardCanvas = (MonopolyBoard) BoardCanvasFactory.createBoardCanvas(GameType.MONOPOLY, board);
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
    sidebar.getChildren().add(playerCardsSection);

    // Dice section
    VBox diceSection = new VBox(10);
    diceSection.setAlignment(Pos.CENTER);
    diceSection.setPadding(new Insets(20));

    diceContainer = new HBox(10);
    diceContainer.setAlignment(Pos.CENTER);

    diceSection.getChildren().add(diceContainer);
    sidebar.getChildren().add(diceSection);

    // Game status
    VBox statusBox = new VBox(5);
    statusBox.setPadding(new Insets(20, 0, 0, 0));
    Label gameStatus = new Label("Game Status: In Progress");
    Label round = new Label("Round: 5");

    statusBox.getChildren().addAll(gameStatus, round);
    sidebar.getChildren().add(statusBox);

    // Help button (question mark)
    Button helpButton = createHelpButton();
    StackPane helpButtonContainer = new StackPane(helpButton);
    helpButtonContainer.setAlignment(Pos.TOP_RIGHT);
    helpButtonContainer.setPadding(new Insets(10));

    root.setRight(sidebar);

    // Add the root and help button to a stack pane to overlay the help button
    StackPane contentPane = new StackPane();
    contentPane.getChildren().addAll(root, helpButtonContainer);
    getChildren().add(contentPane);

    // Add space key press handler
    this.setOnKeyPressed(event -> {
      if (event.getCode() == KeyCode.SPACE) {
        emitDiceRolledEvent();
      }
    });

    // Make the pane focusable to receive key events
    this.setFocusTraversable(true);

    // Show the rules dialog when the screen is loaded
    Platform.runLater(this::showRulesDialog);
  }

  /**
   * Creates a circular help button with a question mark.
   *
   * @return A styled help button
   */
  private Button createHelpButton() {
    Button helpButton = new Button("?");
    helpButton.setFont(Font.font("System", FontWeight.BOLD, 14));
    helpButton.setStyle(
            "-fx-background-radius: 15; " +
                    "-fx-min-width: 30; " +
                    "-fx-min-height: 30; " +
                    "-fx-background-color: #4285F4; " +
                    "-fx-text-fill: white;"
    );

    // Prevent the button from receiving focus to avoid intercepting space key presses
    helpButton.setFocusTraversable(false);

    // Show rules dialog when clicked
    helpButton.setOnAction(e -> showRulesDialog());

    return helpButton;
  }

  /**
   * Display a dialog with the rules of the Monopoly game.
   */
  private void showRulesDialog() {
    Alert rulesDialog = new Alert(Alert.AlertType.INFORMATION);
    rulesDialog.setTitle("Monopoly Rules");
    rulesDialog.setHeaderText("Welcome to Monopoly!");

    // Create a label with the rules text
    Label rulesLabel = new Label(
            "Rules of the Game:\n\n" +
                    "• Roll the dice using the SPACE key to move around the board\n" +
                    "• Buy properties, collect rent, and build houses\n" +
                    "• Increase your money by strategic buying and trading\n" +
                    "• First player to reach $2000 wins the game!"
    );
    rulesLabel.setTextAlignment(TextAlignment.LEFT);
    rulesLabel.setWrapText(true);
    rulesLabel.setFont(Font.font("System", FontWeight.NORMAL, 14));
    rulesLabel.setPrefWidth(400);

    // Set the content of the dialog to the label
    rulesDialog.getDialogPane().setContent(rulesLabel);
    rulesDialog.getDialogPane().setPrefWidth(420);

    // Show the dialog
    rulesDialog.showAndWait();
  }

  public void update(List<Player> players, Player currentPlayer, List<Integer> diceCounts, List<Integer> playerMoney) {
    // Update player positions with animation
    boardCanvas.updatePlayersWithAnimation(players, () -> {
      // This is called when animation completes
    });

    // Update UI elements
    drawPlayerCards(players, currentPlayer, playerMoney);
    drawDice(diceCounts);
  }

  private HBox createPlayerCard(Player player, boolean isActive, int money) {
    VBox card = new VBox(5);
    card.setPadding(new Insets(10));
    card.setStyle("-fx-border-color: " + (isActive ? "#4CAF50" : "lightgray") + "; -fx-border-width: 2px; -fx-border-radius: 5; -fx-background-color: #f9f9f9;");
    card.setPrefWidth(200);

    // Player name and money
    Label nameLabel = new Label(player.getName());
    nameLabel.setFont(new Font(14));
    Label moneyLabel = new Label("$" + money);
    moneyLabel.setStyle("-fx-font-weight: bold;");
    Label stats = new Label("🏠 " + 5 + "   🏘 " + 3);

    card.getChildren().addAll(nameLabel, moneyLabel, stats);
    return new HBox(card);
  }

  private void drawPlayerCards(List<Player> players, Player currentPlayer, List<Integer> playerMoney) {
    playerCardsSection.getChildren().clear();
    for (int i = 0; i < players.size(); i++) {
      Player player = players.get(i);
      boolean isActive = player.equals(currentPlayer);
      int money = (i < playerMoney.size()) ? playerMoney.get(i) : 0;
      playerCardsSection.getChildren().add(createPlayerCard(player, isActive, money));
    }
  }

  private void drawDice(List<Integer> diceCounts) {
    diceContainer.getChildren().clear();
    for (Integer diceCount : diceCounts) {
      DieRectangle die = new DieRectangle(diceCount, 70);
      diceContainer.getChildren().add(die);
    }
  }

  private void emitDiceRolledEvent() {
    GUIApp.getInstance().emitEvent(AppEvent.IN_GAME_EVENT, "monopoly_dice_rolled");
  }
}
