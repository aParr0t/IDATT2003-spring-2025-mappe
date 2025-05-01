package edu.ntnu.iir.bidata.view.gui.screens;

import edu.ntnu.iir.bidata.model.Board;
import edu.ntnu.iir.bidata.model.GameType;
import edu.ntnu.iir.bidata.model.Player;
import edu.ntnu.iir.bidata.model.PlayingPiece;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.application.Platform;

import java.util.List;
import java.util.Objects;

/**
 * UI component that represents the Monopoly game screen.
 * Displays the game board, player information cards, dice, and game status.
 * Handles user interactions like rolling dice with space key.
 */
public class MonopolyScreen extends StackPane {
  private final MonopolyBoard boardCanvas;
  private final VBox playerCardsSection;
  private final HBox diceContainer;

  /**
   * Constructs a new Monopoly game screen.
   *
   * @param board the game board containing game state and tile information
   */
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
   * @return a styled help button that shows the rules dialog when clicked
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
   * Displays a dialog with the rules of the Monopoly game.
   * Shows information about how to play, including controls and winning conditions.
   */
  private void showRulesDialog() {
    Alert rulesDialog = new Alert(Alert.AlertType.INFORMATION);
    rulesDialog.setTitle("Monopoly Rules");
    rulesDialog.setHeaderText("Welcome to Monopoly!");

    // Create a label with the rules text
    Label rulesLabel = new Label(
            """
                    Rules of the Game:
                    
                    • Roll the dice using the SPACE key to move around the board
                    • Buy properties, collect rent, and build houses
                    • Increase your money by strategic buying and trading
                    • First player to reach $2000 wins the game!"""
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

  /**
   * Updates the UI components based on the current game state.
   * Updates player positions on the board with animation, player cards, and dice display.
   *
   * @param players the list of players in the game
   * @param currentPlayer the player whose turn it currently is
   * @param diceCounts the list of dice values from the last roll
   * @param playerMoney the list of money amounts for each player
   */
  public void update(List<Player> players, Player currentPlayer, List<Integer> diceCounts, List<Integer> playerMoney) {
    // Update player positions with animation
    boardCanvas.updatePlayersWithAnimation(players, () -> {
      // This is called when animation completes
    });

    // Update UI elements
    drawPlayerCards(players, currentPlayer, playerMoney);
    drawDice(diceCounts);
  }

  /**
   * Creates a visual card representation for a player.
   * The card displays the player's name, playing piece, money, and property statistics.
   *
   * @param player the player to create a card for
   * @param isActive whether this player is the current active player
   * @param money the player's current money amount
   * @return an HBox containing the styled player card
   */
  private HBox createPlayerCard(Player player, boolean isActive, int money) {
    VBox card = new VBox(5);
    card.setPadding(new Insets(10));
    card.setStyle("-fx-border-color: " + (isActive ? "#4CAF50" : "lightgray") + "; -fx-border-width: 2px; -fx-border-radius: 5; -fx-background-color: #f9f9f9;");
    card.setPrefWidth(200);

    // Create a horizontal box for the player name and playing piece
    HBox nameAndPieceBox = new HBox(10);
    nameAndPieceBox.setAlignment(Pos.CENTER_LEFT);

    // Player name
    Label nameLabel = new Label(player.getName());
    nameLabel.setFont(new Font(14));

    // Playing piece icon
    ImageView pieceImageView = null;
    PlayingPiece playingPiece = player.getPlayingPiece();

    if (playingPiece != null) {
      try {
        String imagePath = playingPiece.getImagePath();
        Image pieceImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        pieceImageView = new ImageView(pieceImage);
        pieceImageView.setFitHeight(36);
        pieceImageView.setFitWidth(36);
        pieceImageView.setPreserveRatio(true);
      } catch (Exception e) {
        System.err.println("Could not load playing piece image: " + e.getMessage());
      }
    }

    // Add name to the horizontal box
    nameAndPieceBox.getChildren().add(nameLabel);

    // Add playing piece image if available
    if (pieceImageView != null) {
      nameAndPieceBox.getChildren().add(pieceImageView);
    }

    // Money label
    Label moneyLabel = new Label("$" + money);
    moneyLabel.setStyle("-fx-font-weight: bold;");

    // Stats label
    Label stats = new Label("🏠 " + 5 + "   🏘 " + 3);

    card.getChildren().addAll(nameAndPieceBox, moneyLabel, stats);
    return new HBox(card);
  }

  /**
   * Updates the player cards section with current player information.
   * Highlights the current active player and shows updated money amounts.
   *
   * @param players the list of players in the game
   * @param currentPlayer the player whose turn it currently is
   * @param playerMoney the list of money amounts for each player
   */
  private void drawPlayerCards(List<Player> players, Player currentPlayer, List<Integer> playerMoney) {
    playerCardsSection.getChildren().clear();
    for (int i = 0; i < players.size(); i++) {
      Player player = players.get(i);
      boolean isActive = player.equals(currentPlayer);
      int money = (i < playerMoney.size()) ? playerMoney.get(i) : 0;
      playerCardsSection.getChildren().add(createPlayerCard(player, isActive, money));
    }
  }

  /**
   * Updates the dice display with the current dice values.
   * Highlights when doubles are rolled by changing the background color.
   *
   * @param diceCounts the list of dice values to display
   */
  private void drawDice(List<Integer> diceCounts) {
    diceContainer.getChildren().clear();

    // Check if dice are equal (doubles)
    boolean areEqual = diceCounts.size() >= 2 &&
            diceCounts.stream().distinct().count() == 1;

    // Set background color based on whether dice are equal
    diceContainer.setStyle("-fx-background-color: " + (areEqual ? "#FFD700;" : "#ffffff;"));
    diceContainer.setPadding(new Insets(10));
    diceContainer.setAlignment(Pos.CENTER);

    for (Integer diceCount : diceCounts) {
      DieRectangle die = new DieRectangle(diceCount, 70);
      diceContainer.getChildren().add(die);
    }
  }

  /**
   * Emits a dice roll event when the user presses the space key.
   * The event is handled by the game controller to advance game state.
   */
  private void emitDiceRolledEvent() {
    GUIApp.getInstance().emitEvent(AppEvent.IN_GAME_EVENT, "monopoly_dice_rolled");
  }
}
