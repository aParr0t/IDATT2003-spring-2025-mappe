package edu.ntnu.iir.bidata.view.gui.screens;

import edu.ntnu.iir.bidata.filehandling.FileConstants;
import edu.ntnu.iir.bidata.filehandling.FileUtils;
import edu.ntnu.iir.bidata.model.PlayingPiece;
import edu.ntnu.iir.bidata.model.PlayingPieceType;
import edu.ntnu.iir.bidata.utils.Tuple;
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
import javafx.stage.FileChooser;
import edu.ntnu.iir.bidata.model.Player;

import java.io.File;
import java.io.IOException;
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

    // Create buttons for file operations and starting the game
    HBox buttonContainer = new HBox(20);
    buttonContainer.setAlignment(Pos.CENTER);

    Button startGameButton = new Button("Start Game");
    Button savePlayersButton = new Button("Save Players");
    Button loadPlayersButton = new Button("Load Players");

    buttonContainer.getChildren().addAll(startGameButton, savePlayersButton, loadPlayersButton);

    // Set button actions
    startGameButton.setOnAction(event -> startGameHandler());
    savePlayersButton.setOnAction(event -> savePlayers());
    loadPlayersButton.setOnAction(event -> loadPlayers());

    // Vertical box to hold elements
    VBox container = new VBox(20, titleLabel, playersContainer, buttonContainer);
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

  private void removePlayer(Player player) {
    players.remove(player);
    redrawPlayers();
  }

  private void addNewPlayer() {
    Player newPlayer = new Player("Player " + (players.size() + 1));
    players.add(newPlayer);
    redrawPlayers();
  }

  /**
   * Updates the players list with new players and redraws the UI.
   * 
   * @param newPlayers The new list of players to display
   */
  public void updatePlayers(List<Player> newPlayers) {
    // Clear the current players list
    this.players.clear();
    // Add all the new players
    this.players.addAll(newPlayers);
    // Redraw the UI
    redrawPlayers();
  }

  private void redrawPlayers() {
    // clear the container
    playersContainer.getChildren().clear();

    // draw the players
    for (Player player : players) {
      VBox playerCard = createPlayerCard(player);
      // Make the image clickable to cycle through playing pieces
      Rectangle imageContainer = (Rectangle) playerCard.getChildren().get(0);
      imageContainer.setOnMouseClicked(event -> playerClickHandler(player));
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

    // Add a listener to update the player name when the text field changes
    playerNameField.textProperty().addListener((observable, oldValue, newValue) -> {
      player.setName(newValue);
    });

    // Delete button
    Button deleteButton = new Button("Delete");
    deleteButton.setOnAction(event -> removePlayer(player));
    deleteButton.setPrefWidth(180);

    playerCard.getChildren().addAll(imageContainer, playerNameField, deleteButton);
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

  private void savePlayers() {
    // Create a file chooser
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Save Players");
    fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("CSV Files", "*.csv")
    );

    // Set the initial directory to the players directory in the project
    File playersDir = FileConstants.PLAYERS_DIR.toFile();
    // Ensure the directory exists
    try {
      FileUtils.ensureDirectoryExists(FileConstants.PLAYERS_DIR);
      fileChooser.setInitialDirectory(playersDir);
    } catch (IOException e) {
      GUIApp.getInstance().showMessage("Error accessing players directory: " + e.getMessage());
    }

    fileChooser.setInitialFileName("players.csv");

    // Show the save dialog
    File file = fileChooser.showSaveDialog(this.getScene().getWindow());

    if (file != null) {
      // Emit the save event
      GUIApp.getInstance().emitEvent(AppEvent.SAVE_PLAYERS, new Tuple<>(file.toPath(), players));
    }
  }

  private void loadPlayers() {
    // Create a file chooser
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Load Players");
    fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("CSV Files", "*.csv")
    );

    // Set the initial directory to the players directory in the project
    File playersDir = FileConstants.PLAYERS_DIR.toFile();
    // Ensure the directory exists
    try {
      FileUtils.ensureDirectoryExists(FileConstants.PLAYERS_DIR);
      fileChooser.setInitialDirectory(playersDir);
    } catch (IOException e) {
      GUIApp.getInstance().showMessage("Error accessing players directory: " + e.getMessage());
    }

    // Show the open dialog
    File file = fileChooser.showOpenDialog(this.getScene().getWindow());

    if (file != null) {
      // Emit the load event
      GUIApp.getInstance().emitEvent(AppEvent.LOAD_PLAYERS, file.toPath());
    }
  }
}
