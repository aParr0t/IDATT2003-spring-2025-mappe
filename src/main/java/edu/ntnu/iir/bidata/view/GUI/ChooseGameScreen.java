package edu.ntnu.iir.bidata.view.GUI;

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
  class GamePreview {
    String name;
    String imagePath;
//    GameType type;  // add this later so that clicking on a game selects that type of game

    public GamePreview(String name, String imagePath) {
      this.name = name;
      this.imagePath = imagePath;
    }
  }

  public ChooseGameScreen() {
    // Create title label
    Label titleLabel = new Label("Choose a game");
    titleLabel.setFont(new Font(32));

    // get all games
    List<GamePreview> gamePreviews = List.of(
            new GamePreview("Stigespill", "/images/games/stigespill.png"),
            new GamePreview("Monopoly", "/images/games/monopoly.png")
    );

    // draw game previews
    HBox gamesContainer = new HBox(20);
    for (GamePreview gamePreview : gamePreviews) {
      VBox gameCard = new VBox();
      gameCard.setStyle("-fx-border-color: black; -fx-border-width: 1px;");

      // game preview image
      Rectangle imageContainer = new Rectangle(200, 200);
      ImagePattern imagePattern = new ImagePattern(new Image(gamePreview.imagePath));
      imageContainer.setFill(imagePattern);

      // game name
      Label gameName = new Label(gamePreview.name);
      gameName.setFont(new Font(18));
      gameName.setStyle("-fx-padding: 10px;");

      gameCard.getChildren().addAll(imageContainer, gameName);
      gamesContainer.getChildren().add(gameCard);

      // add click handler to gameCard
      gameCard.setOnMouseClicked(event -> {
        System.out.println("Clicked on " + gamePreview.name);
        GUIApp.setContent(new ChoosePlayerScreen());
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
