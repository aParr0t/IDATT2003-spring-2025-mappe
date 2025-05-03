package edu.ntnu.iir.bidata.view.gui.screens;

import edu.ntnu.iir.bidata.exceptions.DirectoryCreationException;
import edu.ntnu.iir.bidata.filehandling.FileConstants;
import edu.ntnu.iir.bidata.filehandling.FileUtils;
import edu.ntnu.iir.bidata.model.Player;
import edu.ntnu.iir.bidata.model.PlayingPiece;
import edu.ntnu.iir.bidata.model.PlayingPieceType;
import edu.ntnu.iir.bidata.utils.Tuple;
import edu.ntnu.iir.bidata.view.AppEvent;
import edu.ntnu.iir.bidata.view.gui.GuiApp;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
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

/**
 * Screen for selecting and configuring players before starting a game.
 * Allows adding, removing, and customizing players,
 * as well as saving and loading player configurations.
 */
public class ChoosePlayerScreen extends StackPane {
  private final HBox playersContainer;
  private final List<Player> players;
  private final List<PlayingPiece> allPlayingPieces;
  private final int maxPlayers;

  /**
   * Constructs a new player selection screen.
   *
   * @param startingPlayers  initial list of players to display
   * @param allPlayingPieces list of all available playing pieces for selection
   * @param maxPlayers       maximum number of players allowed
   */
  public ChoosePlayerScreen(
          List<Player> startingPlayers,
          List<PlayingPiece> allPlayingPieces,
          int maxPlayers
  ) {
    this.players = new ArrayList<>(startingPlayers);
    this.allPlayingPieces = allPlayingPieces;
    this.maxPlayers = maxPlayers;

    // Create title label
    Label titleLabel = new Label("Who's playing?");
    titleLabel.setFont(new Font(32));

    Label subtitleLabel = new Label("Click on a player to change their playing piece");
    subtitleLabel.setFont(new Font(18));

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
    VBox container = new VBox(20, titleLabel, subtitleLabel, playersContainer, buttonContainer);
    container.setAlignment(Pos.CENTER);

    // Add vbox to StackPane
    this.getChildren().add(container);
    this.setAlignment(Pos.CENTER);
  }

  /**
   * Gets the next playing piece in the rotation sequence.
   *
   * @param playingPiece the current playing piece
   * @return the next playing piece in the sequence
   */
  private PlayingPiece getNextPlayingPiece(PlayingPiece playingPiece) {
    // get the index of the current playing piece
    int currentIndex = playingPiece.getType().ordinal();
    // return the next playing piece
    return allPlayingPieces.get((currentIndex + 1) % allPlayingPieces.size());
  }

  /**
   * Handles clicking on a player's playing piece to cycle through available options.
   *
   * @param player the player whose playing piece is being changed
   */
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

  /**
   * Removes a player from the game.
   *
   * @param player the player to remove
   */
  private void removePlayer(Player player) {
    players.remove(player);
    redrawPlayers();
  }

  /**
   * Adds a new player to the game with a default name.
   */
  private void addNewPlayer() {
    Player newPlayer = new Player("Player " + (players.size() + 1));
    players.add(newPlayer);
    redrawPlayers();
  }

  /**
   * Updates the players list with new players and redraws the UI.
   *
   * @param newPlayers the new list of players to display
   */
  public void updatePlayers(List<Player> newPlayers) {
    // Clear the current players list
    this.players.clear();
    // Add all the new players
    this.players.addAll(newPlayers);
    // Redraw the UI
    redrawPlayers();
  }

  /**
   * Redraws the player cards UI based on the current players list.
   * Also adds a "New Player" card if more players can be added.
   */
  private void redrawPlayers() {
    // clear the container
    playersContainer.getChildren().clear();

    // draw the players
    for (Player player : players) {
      VBox playerCard = createPlayerCard(player);
      // Make the image clickable to cycle through playing pieces
      Rectangle imageContainer = (Rectangle) playerCard.getChildren().getFirst();
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

  /**
   * Creates a UI card for displaying and editing a player.
   *
   * @param player the player to create a card for
   * @return a VBox containing the player card UI elements
   */
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
    playerNameField.textProperty().addListener(
            (observable, oldValue, newValue) -> {
              player.setName(newValue);
            }
    );

    // Delete button
    Button deleteButton = new Button("Delete");
    deleteButton.setOnAction(event -> removePlayer(player));
    deleteButton.setPrefWidth(180);

    playerCard.getChildren().addAll(imageContainer, playerNameField, deleteButton);
    return playerCard;
  }

  /**
   * Creates a "plus" card for adding a new player.
   *
   * @return a VBox containing the "add new player" UI elements
   */
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

  /**
   * Handles the "Start Game" button click by emitting a PLAYERS_CHOSEN event.
   */
  private void startGameHandler() {
    GuiApp.getInstance().emitEvent(AppEvent.PLAYERS_CHOSEN, players);
  }

  /**
   * Configures a FileChooser with common settings for player file operations.
   *
   * @param fileChooser the FileChooser to configure
   * @param title       the title to set for the FileChooser dialog
   */
  private void configureFileChooser(FileChooser fileChooser, String title) {
    fileChooser.setTitle(title);
    fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("CSV Files", "*.csv")
    );

    // Set the initial directory to the players directory in the project
    File playersDir = FileConstants.PLAYERS_DIR.toFile();
    // Ensure the directory exists
    try {
      FileUtils.ensureDirectoryExists(FileConstants.PLAYERS_DIR);
      fileChooser.setInitialDirectory(playersDir);
    } catch (DirectoryCreationException e) {
      GuiApp.getInstance().showMessage("Error accessing players directory: " + e.getMessage());
    }
  }

  /**
   * Handles the "Save Players" button click by showing a save dialog
   * and emitting a SAVE_PLAYERS event with the selected file path.
   */
  private void savePlayers() {
    // Create a file chooser
    FileChooser fileChooser = new FileChooser();
    configureFileChooser(fileChooser, "Save Players");

    fileChooser.setInitialFileName("players.csv");

    // Show the save dialog
    File file = fileChooser.showSaveDialog(this.getScene().getWindow());

    if (file != null) {
      // Emit the save event
      GuiApp.getInstance().emitEvent(AppEvent.SAVE_PLAYERS, new Tuple<>(file.toPath(), players));
    }
  }

  /**
   * Handles the "Load Players" button click by showing an open dialog
   * and emitting a LOAD_PLAYERS event with the selected file path.
   */
  private void loadPlayers() {
    // Create a file chooser
    FileChooser fileChooser = new FileChooser();
    configureFileChooser(fileChooser, "Load Players");

    // Show the open dialog
    File file = fileChooser.showOpenDialog(this.getScene().getWindow());

    if (file != null) {
      // Emit the load event
      GuiApp.getInstance().emitEvent(AppEvent.LOAD_PLAYERS, file.toPath());
    }
  }
}
