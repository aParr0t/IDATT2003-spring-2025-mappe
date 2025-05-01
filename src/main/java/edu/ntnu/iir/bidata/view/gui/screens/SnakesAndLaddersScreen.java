package edu.ntnu.iir.bidata.view.gui.screens;

import edu.ntnu.iir.bidata.model.Board;
import edu.ntnu.iir.bidata.model.GameType;
import edu.ntnu.iir.bidata.model.Player;
import edu.ntnu.iir.bidata.view.AppEvent;
import edu.ntnu.iir.bidata.view.gui.BoardCanvas;
import edu.ntnu.iir.bidata.view.gui.BoardCanvasFactory;
import edu.ntnu.iir.bidata.view.gui.DieRectangle;
import edu.ntnu.iir.bidata.view.gui.GuiApp;
import edu.ntnu.iir.bidata.view.gui.games.SnakesAndLaddersBoard;
import java.util.List;
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

/**
 * A JavaFX UI component that displays the Snakes and Ladders game screen.
 * This screen includes the game board, player information cards, dice display,
 * and roll button controls.
 */
public class SnakesAndLaddersScreen extends StackPane {
  private final Button rollButton;
  private final BoardCanvas boardCanvas;
  private final HBox playerCardsSection;
  private final HBox diceContainer;

  /**
   * Creates a new Snakes and Ladders game screen.
   *
   * @param board the game board to display
   */
  public SnakesAndLaddersScreen(Board board) {
    // Create the main layout
    final BorderPane mainLayout = new BorderPane();

    // Center section - Game board
    boardCanvas = BoardCanvasFactory.createBoardCanvas(GameType.SNAKES_AND_LADDERS, board);
    boardCanvas.setWidth(600);
    boardCanvas.setHeight(600);

    StackPane gameBoardContainer = new StackPane(boardCanvas);
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

    // Dice section
    VBox diceSection = new VBox(10);
    diceSection.setAlignment(Pos.CENTER);
    diceSection.setPadding(new Insets(20));
    diceSection.setMinWidth(USE_PREF_SIZE);
    diceSection.setPrefWidth(Control.USE_COMPUTED_SIZE);
    diceSection.setMaxWidth(Control.USE_PREF_SIZE);

    diceContainer = new HBox(10);
    diceContainer.setAlignment(Pos.CENTER);

    rollButton = new Button("Roll");
    rollButton.setPrefSize(120, 40);
    rollButton.setOnAction(e -> handleRollDice());

    diceSection.getChildren().addAll(diceContainer, rollButton);

    bottomSection.getChildren().addAll(playerCardsSection, diceSection);
    mainLayout.setBottom(bottomSection);

    // Add the main layout to the stack pane
    this.getChildren().add(mainLayout);
  }

  /**
   * Creates a player information card UI component.
   *
   * @param player   the player whose information to display
   * @param isActive whether this player is the currently active player
   * @return a StackPane containing the player's information
   */
  private StackPane createPlayerCard(Player player, boolean isActive) {
    // Create player card container
    StackPane playerCard = new StackPane();
    playerCard.setPrefSize(150, 180);
    playerCard.setPadding(new Insets(10));

    // give container an outline if the player is active
    if (isActive) {
      playerCard.setBackground(
              new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY))
      );
    }

    // Create player avatar (circle)
    Rectangle avatar = new Rectangle(120, 120);
    ImagePattern imagePattern = new ImagePattern(
            new Image(player.getPlayingPiece().getImagePath())
    );
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

  /**
   * Handles the roll dice button click event by triggering the dice roll game event.
   * Disables the roll button during animation.
   */
  private void handleRollDice() {
    // Disable roll button during animation
    rollButton.setDisable(true);

    // First, notify that dice were rolled (this will update the game state in BoardGameApp)
    // This will trigger BoardGameApp.goToAndUpdateGameScreen(), which will update this screen
    GuiApp.getInstance().emitEvent(AppEvent.IN_GAME_EVENT, "snakes_and_ladders_dice_rolled");
  }

  /**
   * Updates the game screen with the current game state.
   *
   * @param players       list of all players in the game
   * @param currentPlayer the player whose turn it currently is
   * @param diceCounts    list of dice values that were rolled
   */
  public void update(List<Player> players, Player currentPlayer, List<Integer> diceCounts) {
    // Use animation for player movement
    // Position tracking is now handled automatically by boardCanvas
    ((SnakesAndLaddersBoard) boardCanvas).updatePlayersWithAnimation(players, () -> {
      // Re-enable roll button after animation completes
      rollButton.setDisable(false);
    });

    drawPlayerCards(players, currentPlayer);
    drawDice(diceCounts);
  }

  /**
   * Draws the dice with their current values.
   *
   * @param diceCounts list of values to display on the dice
   */
  private void drawDice(List<Integer> diceCounts) {
    diceContainer.getChildren().clear();
    for (Integer diceCount : diceCounts) {
      DieRectangle die = new DieRectangle(diceCount, 70);
      diceContainer.getChildren().add(die);
    }
  }

  /**
   * Draws player information cards for all players, highlighting the current player.
   *
   * @param players       list of players to display
   * @param currentPlayer the player whose turn it currently is
   */
  private void drawPlayerCards(List<Player> players, Player currentPlayer) {
    playerCardsSection.getChildren().clear();
    for (Player player : players) {
      boolean isActive = player.equals(currentPlayer);
      playerCardsSection.getChildren().add(createPlayerCard(player, isActive));
    }
  }
}
