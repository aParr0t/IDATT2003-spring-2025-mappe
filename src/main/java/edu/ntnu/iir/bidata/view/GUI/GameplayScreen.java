package edu.ntnu.iir.bidata.view.GUI;

import edu.ntnu.iir.bidata.model.*;
import edu.ntnu.iir.bidata.view.GUI.games.SnakesAndLaddersBoard;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameplayScreen extends StackPane {
  private final List<Player> players;
  private int currentPlayerIndex = 0;
  private final Random random = new Random();
  private final DieRectangle die1;
  private final DieRectangle die2;
  private final Button rollButton;
  private final HBox playerCardsSection;

  public GameplayScreen() {
    // Initialize players (static for now)
    players = new ArrayList<>();
    players.add(new Player("Atas", new PlayingPiece(PlayingPieceType.CAR)));
    players.add(new Player("Stian", new PlayingPiece(PlayingPieceType.DOG)));

    // Create the main layout
    BorderPane mainLayout = new BorderPane();

    // Center section - Game board
    Board board = BoardFactory.normalSnakesAndLadders();
    var gameBoard = new SnakesAndLaddersBoard(board);
    gameBoard.setWidth(500);  // Set the width of the canvas
    gameBoard.setHeight(500); // Set the height of the canvas
    StackPane gameBoardContainer = new StackPane(gameBoard);
    gameBoardContainer.setPadding(new Insets(10));
    mainLayout.setCenter(gameBoardContainer);

    // Bottom section - Player info and dice
    HBox bottomSection = new HBox();
    bottomSection.setAlignment(Pos.CENTER_LEFT);
    // add top border to bottom section
    bottomSection.setStyle("-fx-border-color: black; -fx-border-width: 2px 0 0 0;");

    // Player cards section
    playerCardsSection = new HBox(10);
    playerCardsSection.setPadding(new Insets(20));
    playerCardsSection.setStyle("-fx-border-color: black; -fx-border-width: 0 2px 0 0;");
    HBox.setHgrow(playerCardsSection, javafx.scene.layout.Priority.ALWAYS);
    playerCardsSection.setAlignment(Pos.CENTER);
    updatePlayerCards();

    // Dice section
    VBox diceSection = new VBox(10);
    diceSection.setAlignment(Pos.CENTER);
    diceSection.setPadding(new Insets(20));
    diceSection.setMinWidth(USE_PREF_SIZE);
    diceSection.setPrefWidth(Control.USE_COMPUTED_SIZE);
    diceSection.setMaxWidth(Control.USE_PREF_SIZE);

    HBox diceContainer = new HBox(10);
    diceContainer.setAlignment(Pos.CENTER);

    die1 = new DieRectangle(1, 70);
    die2 = new DieRectangle(1, 70);

    diceContainer.getChildren().addAll(die1, die2);

    rollButton = new Button("Roll");
    rollButton.setPrefSize(120, 40);
    rollButton.setOnAction(e -> handleRollDice());

    diceSection.getChildren().addAll(diceContainer, rollButton);

    bottomSection.getChildren().addAll(playerCardsSection, diceSection);
    mainLayout.setBottom(bottomSection);

    // Add the main layout to the stack pane
    this.getChildren().add(mainLayout);
  }

  private StackPane createPlayerCard(Player player, boolean isActive) {
    // Create player card container
    StackPane playerCard = new StackPane();
    playerCard.setPrefSize(150, 180);
    playerCard.setPadding(new Insets(10));

    // give container an outline if the player is active
    if (isActive) {
      playerCard.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    // Create player avatar (circle)
    Rectangle avatar = new Rectangle(120, 120);
    ImagePattern imagePattern = new ImagePattern(new Image(player.getPlayingPiece().getImagePath()));
    avatar.setFill(imagePattern);

    // Create player name label
    Label nameLabel = new Label(player.getName());
    nameLabel.setTextFill(Color.BLACK);
    nameLabel.setFont(new Font(16));
    nameLabel.setTranslateY(70);

    // Add components to card
    playerCard.getChildren().addAll(avatar, nameLabel);
    return playerCard;
  }

  private void handleRollDice() {
    GUIApp.getInstance().emitEvent(GameEvent.DICE_ROLLED);
  }
}
