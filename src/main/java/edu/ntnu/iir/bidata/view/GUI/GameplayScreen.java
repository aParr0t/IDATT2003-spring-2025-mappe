package edu.ntnu.iir.bidata.view.GUI;

import edu.ntnu.iir.bidata.model.*;
import edu.ntnu.iir.bidata.view.GameEvent;
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

import java.util.List;

public class GameplayScreen extends StackPane {
  public GameplayScreen(List<Player> players, GameType gameType, Board board, List<Integer> diceCounts, Player currentPlayer) {
    // Create the main layout
    BorderPane mainLayout = new BorderPane();

    // Center section - Game board
    BoardCanvas boardCanvas = BoardCanvasFactory.createBoardCanvas(gameType, board);
    boardCanvas.setWidth(500);  // Set the width of the canvas
    boardCanvas.setHeight(500); // Set the height of the canvas
    StackPane gameBoardContainer = new StackPane(boardCanvas);
    gameBoardContainer.setPadding(new Insets(10));
    mainLayout.setCenter(gameBoardContainer);

    // Bottom section - Player info and dice
    HBox bottomSection = new HBox();
    bottomSection.setAlignment(Pos.CENTER_LEFT);
    // add top border to bottom section
    bottomSection.setStyle("-fx-border-color: black; -fx-border-width: 2px 0 0 0;");

    // Player cards section
    HBox playerCardsSection = new HBox(10);
    playerCardsSection.setPadding(new Insets(20));
    playerCardsSection.setStyle("-fx-border-color: black; -fx-border-width: 0 2px 0 0;");
    HBox.setHgrow(playerCardsSection, javafx.scene.layout.Priority.ALWAYS);
    playerCardsSection.setAlignment(Pos.CENTER);

    // create player cards
    playerCardsSection.getChildren().clear();
    for (Player player : players) {
      boolean isActive = player.equals(currentPlayer);
      playerCardsSection.getChildren().add(createPlayerCard(player, isActive));
    }

    // Dice section
    VBox diceSection = new VBox(10);
    diceSection.setAlignment(Pos.CENTER);
    diceSection.setPadding(new Insets(20));
    diceSection.setMinWidth(USE_PREF_SIZE);
    diceSection.setPrefWidth(Control.USE_COMPUTED_SIZE);
    diceSection.setMaxWidth(Control.USE_PREF_SIZE);

    HBox diceContainer = new HBox(10);
    diceContainer.setAlignment(Pos.CENTER);

    for (Integer diceCount : diceCounts) {
      DieRectangle die = new DieRectangle(diceCount, 70);
      diceContainer.getChildren().add(die);
    }

    Button rollButton = new Button("Roll");
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
