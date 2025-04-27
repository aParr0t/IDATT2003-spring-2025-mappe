package edu.ntnu.iir.bidata.view.gui.screens;

import edu.ntnu.iir.bidata.model.PlayingPiece;
import edu.ntnu.iir.bidata.model.PlayingPieceType;
import edu.ntnu.iir.bidata.view.AppEvent;
import edu.ntnu.iir.bidata.view.gui.GUIApp;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import edu.ntnu.iir.bidata.model.Player;

import java.util.ArrayList;
import java.util.List;

public class ChoosePlayerScreen extends StackPane {
  private final HBox playersContainer;
  private final List<Player> players;
  private final List<PlayingPiece> allPlayingPieces;
  private final int maxPlayers;

  public ChoosePlayerScreen(List<Player> startingPlayers, List<PlayingPiece> allPlayingPieces, int maxPlayers) {
    this.players = new ArrayList<>(startingPlayers);
    this.allPlayingPieces = allPlayingPieces;
    this.maxPlayers = maxPlayers;

    // Create title label
    Label titleLabel = new Label("Who's playing?");
    titleLabel.setFont(new Font(32));

    // draw game previews
    playersContainer = new HBox(20);
    playersContainer.setAlignment(Pos.CENTER);
    redrawPlayers();

    // add start game button
    Button startGameButton = new Button("Start game");
    startGameButton.setOnAction(event -> startGameHandler());

    // Vertical box to hold elements
    VBox container = new VBox(20, titleLabel, playersContainer, startGameButton);
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

  private void addNewPlayer() {
    Player newPlayer = new Player("Player " + (players.size() + 1));
    players.add(newPlayer);
    redrawPlayers();
  }

  private void redrawPlayers() {
    // clear the container
    playersContainer.getChildren().clear();

    // draw the players
    for (Player player : players) {
      VBox playerCard = createPlayerCard(player);
      playerCard.setOnMouseClicked(event -> playerClickHandler(player));
      playersContainer.getChildren().add(playerCard);
    }

    // Add "New Player" card
    if (players.size() < maxPlayers) {
      VBox newPlayerCard = createPlusCard();
      newPlayerCard.setOnMouseClicked(event -> addNewPlayer());
      playersContainer.getChildren().add(newPlayerCard);
    }
  }

  private VBox createPlayerCard(Player player) {
    VBox playerCard = new VBox();
    playerCard.setStyle("-fx-border-color: black; -fx-border-width: 1px;");
    playerCard.setAlignment(Pos.CENTER);
    playerCard.setPrefWidth(200);
    playerCard.setMaxWidth(200);

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
    playerNameField.setPrefWidth(180);
    playerNameField.setMaxWidth(180);

    playerCard.getChildren().addAll(imageContainer, playerNameField);
    return playerCard;
  }

  private VBox createPlusCard() {
    VBox newPlayerCard = new VBox();
    newPlayerCard.setStyle("-fx-border-color: black; -fx-border-width: 1px;");
    newPlayerCard.setAlignment(Pos.CENTER);
    newPlayerCard.setPrefWidth(200);
    newPlayerCard.setMaxWidth(200);

    // Plus sign container
    Rectangle plusContainer = new Rectangle(200, 200);
    plusContainer.setStyle("-fx-fill: #f0f0f0;");

    // Plus sign
    Text plusSign = new Text("+");
    plusSign.setFont(new Font(72));
    StackPane plusPane = new StackPane(plusContainer, plusSign);

    // "New Player" label
    Label newPlayerLabel = new Label("New Player");
    newPlayerLabel.setFont(new Font(18));
    newPlayerLabel.setStyle("-fx-padding: 10px;");

    newPlayerCard.getChildren().addAll(plusPane, newPlayerLabel);
    return newPlayerCard;
  }

  private void startGameHandler() {
    GUIApp.getInstance().emitEvent(AppEvent.PLAYERS_CHOSEN, players);
  }
}
