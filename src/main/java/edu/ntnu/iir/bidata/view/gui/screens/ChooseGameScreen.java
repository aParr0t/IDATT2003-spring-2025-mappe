package edu.ntnu.iir.bidata.view.gui.screens;

import edu.ntnu.iir.bidata.model.GameType;
import edu.ntnu.iir.bidata.view.AppEvent;
import edu.ntnu.iir.bidata.view.gui.GUIApp;
import edu.ntnu.iir.bidata.view.gui.GameTypePreview;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.util.List;

public class ChooseGameScreen extends StackPane {
  List<GameTypePreview> gamePreviews = List.of(
          new GameTypePreview("Stigespill", "/images/games/stigespill.png", GameType.SNAKES_AND_LADDERS),
          new GameTypePreview("Monopoly", "/images/games/monopoly.png", GameType.MONOPOLY)
  );

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
        GUIApp.getInstance().emitEvent(AppEvent.GAME_CHOSEN, gamePreview.gameType());
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
