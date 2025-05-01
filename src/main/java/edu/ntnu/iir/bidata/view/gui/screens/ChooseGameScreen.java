package edu.ntnu.iir.bidata.view.gui.screens;

import edu.ntnu.iir.bidata.model.GameType;
import edu.ntnu.iir.bidata.view.AppEvent;
import edu.ntnu.iir.bidata.view.gui.GameTypePreview;
import edu.ntnu.iir.bidata.view.gui.GuiApp;
import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

/**
 * A screen that allows users to choose which game they want to play.
 * Displays available games as clickable preview cards with images.
 */
public class ChooseGameScreen extends StackPane {
  /**
   * List of available game previews to display on the screen.
   */
  List<GameTypePreview> gamePreviews = List.of(
          new GameTypePreview(
                  "Stigespill", "/images/games/stigespill.png", GameType.SNAKES_AND_LADDERS
          ),
          new GameTypePreview("Monopoly", "/images/games/monopoly.png", GameType.MONOPOLY)
  );

  /**
   * Constructs a new ChooseGameScreen with game previews.
   * Creates a UI that displays available games as clickable cards with images and names.
   * When a game is selected, emits a GAME_CHOSEN event with the corresponding game type.
   */
  public ChooseGameScreen() {
    // Create title label
    Label titleLabel = new Label("Choose a game");
    titleLabel.setFont(new Font(32));

    // draw game previews
    HBox gamesContainer = new HBox(20);
    for (GameTypePreview gamePreview : gamePreviews) {
      VBox gameCard = new VBox();
      gameCard.setStyle("-fx-border-color: black; -fx-border-width: 1px;");

      // game preview image
      Rectangle imageContainer = new Rectangle(200, 200);
      ImagePattern imagePattern = new ImagePattern(new Image(gamePreview.imagePath()));
      imageContainer.setFill(imagePattern);

      // game name
      Label gameName = new Label(gamePreview.name());
      gameName.setFont(new Font(18));
      gameName.setStyle("-fx-padding: 10px;");

      gameCard.getChildren().addAll(imageContainer, gameName);
      gamesContainer.getChildren().add(gameCard);

      // add click handler to gameCard
      gameCard.setOnMouseClicked(event -> {
        GuiApp.getInstance().emitEvent(AppEvent.GAME_CHOSEN, gamePreview.gameType());
      });
    }
    gamesContainer.setAlignment(Pos.CENTER);

    // Vertical box to hold elements
    VBox container = new VBox(20, titleLabel, gamesContainer);
    container.setAlignment(Pos.CENTER);

    // Add vbox to StackPane
    this.getChildren().add(container);
    this.setAlignment(Pos.CENTER);
  }
}
