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
  private final List<Player> players;
  private final List<PlayingPiece> allPlayingPieces;

  public ChoosePlayerScreen(List<Player> players, List<PlayingPiece> allPlayingPieces) {
    this.players = players;
    this.allPlayingPieces = allPlayingPieces;

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

  private PlayingPiece getNextPlayingPiece(PlayingPiece playingPiece) {
    // get the index of the current playing piece
    int currentIndex = playingPiece.getType().ordinal();
    // return the next playing piece
    return allPlayingPieces.get((currentIndex + 1) % allPlayingPieces.size());
  }

  private void playerClickHandler(Player player) {
    PlayingPiece currentPlayingPiece = player.getPlayingPiece();
    PlayingPiece nextPlayingPiece;
    if (currentPlayingPiece == null) {
      nextPlayingPiece = new PlayingPiece(PlayingPieceType.CAR);
    } else {
      nextPlayingPiece = getNextPlayingPiece(player.getPlayingPiece());
    }
    player.setPlayingPiece(nextPlayingPiece);
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
      PlayingPiece playingPiece = player.getPlayingPiece();
      ImagePattern imagePattern;
      if (playingPiece == null) {
        imagePattern = new ImagePattern(new Image("/images/playingPieces/noPlayingPiece.png"));
      } else {
        imagePattern = new ImagePattern(new Image(playingPiece.getImagePath()));
      }
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
