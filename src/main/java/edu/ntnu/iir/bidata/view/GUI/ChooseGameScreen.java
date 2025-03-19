package edu.ntnu.iir.bidata.view.GUI;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
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
    titleLabel.setFont(new Font(24));

    // get all games
    List<GamePreview> gamePreviews = List.of(
            new GamePreview("Stigespill", "game1.png"),
            new GamePreview("Monopoly", "game2.png")
    );

    // draw game previews
    HBox gamePreviewContainer = new HBox();
    for (GamePreview gamePreview : gamePreviews) {
      VBox gameCard = new VBox();
      // game preview image
      Rectangle imageContainer = new Rectangle(100, 100);
      ImagePattern imagePattern = new ImagePattern(new Image(gamePreview.imagePath));
      imageContainer.setFill(imagePattern);
      // game preview name
      Label gameName = new Label(gamePreview.name);

      gameCard.getChildren().addAll(imageContainer, gameName);
    }

    // Vertical box to hold elements
    VBox vbox = new VBox(10, titleLabel, gamePreviewContainer);
    vbox.setAlignment(Pos.CENTER);

    // Add vbox to StackPane
    this.getChildren().add(vbox);
    this.setAlignment(Pos.CENTER);
  }
}
