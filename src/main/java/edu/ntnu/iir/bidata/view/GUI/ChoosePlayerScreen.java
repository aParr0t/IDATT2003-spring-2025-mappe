package edu.ntnu.iir.bidata.view.GUI;

import edu.ntnu.iir.bidata.model.PlayingPiece;
import edu.ntnu.iir.bidata.model.PlayingPieceType;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import edu.ntnu.iir.bidata.model.Player;

import java.util.List;
import java.util.HashSet;

public class ChoosePlayerScreen extends StackPane {
  private final HBox playersContainer;
  private final Label errorLabel;
  private final List<Player> players = List.of(
          new Player("Atas", new PlayingPiece(PlayingPieceType.CAR)),
          new Player("Stian", new PlayingPiece(PlayingPieceType.DOG))
  );

//  static class Player {
//    String name;
//    PlayingPiece playingPiece;
//
//    public Player(String name, PlayingPiece playingPiece) {
//      this.name = name;
//      this.playingPiece = playingPiece;
//    }
//
//    public String getName() {
//      return name;
//    }
//
//    public PlayingPiece getPlayingPiece() {
//      return playingPiece;
//    }
//
//    public void setPlayingPiece(PlayingPiece playingPiece) {
//      this.playingPiece = playingPiece;
//    }
//  }

  public ChoosePlayerScreen() {
    // Create title label
    Label titleLabel = new Label("Who's playing?");
    titleLabel.setFont(new Font(32));

    // draw game previews
    playersContainer = new HBox(20);
    playersContainer.setAlignment(Pos.CENTER);
    redrawPlayers();

    // Create error label (initially hidden)
    errorLabel = new Label();
    errorLabel.setTextFill(Color.RED);
    errorLabel.setVisible(false);

    // add start game button
    Button startGameButton = new Button("Start game");
    startGameButton.setOnAction(event -> startGameHandler());

    // Vertical box to hold elements
    VBox container = new VBox(20, titleLabel, playersContainer, errorLabel, startGameButton);
    container.setAlignment(Pos.CENTER);

    // Add vbox to StackPane
    this.getChildren().add(container);
    this.setAlignment(Pos.CENTER);
  }

  private PlayingPieceType getNextPlayingPiece(PlayingPieceType playingPieceType) {
    // make a list of all the playing pieces from the enum class PlayingPieceType
    PlayingPieceType[] playingPieces = PlayingPieceType.values();
    // get the index of the current playing piece
    int currentIndex = playingPieceType.ordinal();
    // return the next playing piece
    return playingPieces[(currentIndex + 1) % playingPieces.length];
  }

  private void playerClickHandler(Player player) {
    PlayingPieceType nextPlayingPiece = getNextPlayingPiece(player.getPlayingPiece().getType());
    player.setPlayingPiece(new PlayingPiece(nextPlayingPiece));
    redrawPlayers();
  }

  private void redrawPlayers() {
    // clear the container
    playersContainer.getChildren().clear();

    // draw the players
    for (Player player : players) {
      VBox playerCard = new VBox();
      playerCard.setStyle("-fx-border-color: black; -fx-border-width: 1px;");

      // playing piece image
      Rectangle imageContainer = new Rectangle(200, 200);
      ImagePattern imagePattern = new ImagePattern(new Image(player.getPlayingPiece().getImagePath()));
      imageContainer.setFill(imagePattern);

      // player name is an input field
      TextField playerNameField = new TextField(player.getName());
      playerNameField.setFont(new Font(18));
      playerNameField.setStyle("-fx-padding: 10px;");

      playerCard.getChildren().addAll(imageContainer, playerNameField);
      playersContainer.getChildren().add(playerCard);

      // add click handler to gameCard
      playerCard.setOnMouseClicked(event -> playerClickHandler(player));
    }
  }

  private void startGameHandler() {
    // Check if all playing pieces are unique
    if (hasEveryPlayerUniquePiece()) {
      // Hide any previous error message
      errorLabel.setVisible(false);

      // Start game
      GUIApp.setContent(new GameplayScreen());
    } else {
      // Show error message
      errorLabel.setText("Every player must have a unique playing piece");
      errorLabel.setVisible(true);
    }
  }

  /**
   * Checks if all players have unique playing pieces using a HashSet
   *
   * @return true if all pieces are unique, false otherwise
   */
  private boolean hasEveryPlayerUniquePiece() {
    HashSet<PlayingPieceType> pieceTypes = new HashSet<>();

    for (Player player : players) {
      pieceTypes.add(player.getPlayingPiece().getType());
    }

    // If the set size equals the player count, all pieces are unique
    return pieceTypes.size() == players.size();
  }
}
