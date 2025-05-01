package edu.ntnu.iir.bidata.view.gui.screens;

import edu.ntnu.iir.bidata.exceptions.DirectoryCreationException;
import edu.ntnu.iir.bidata.filehandling.FileConstants;
import edu.ntnu.iir.bidata.filehandling.FileUtils;
import edu.ntnu.iir.bidata.model.Board;
import edu.ntnu.iir.bidata.model.GameType;
import edu.ntnu.iir.bidata.view.AppEvent;
import edu.ntnu.iir.bidata.view.gui.BoardCanvas;
import edu.ntnu.iir.bidata.view.gui.BoardCanvasFactory;
import edu.ntnu.iir.bidata.view.gui.GuiApp;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;

/**
 * A JavaFX screen for selecting, saving, and loading game boards.
 * Displays a collection of board options for a specific game type and allows
 * the user to select a board or load/save boards to files.
 */
public class ChooseBoardScreen extends StackPane {
  private Board selectedBoard;
  private final GameType gameType;
  private final List<Board> boards;
  private final VBox boardCardContainer = new VBox(10);
  private final HBox gamesContainer = new HBox(20);
  private final HBox buttonContainer;

  /**
   * Creates a new ChooseBoardScreen for selecting boards of a specific game type.
   *
   * @param gameType the type of game to display boards for
   * @param boards   the list of available boards to display
   */
  public ChooseBoardScreen(GameType gameType, List<Board> boards) {
    this.gameType = gameType;
    this.boards = new ArrayList<>(boards); // Create a mutable copy of the list

    // Create title label
    Label titleLabel = new Label("Choose a board");
    titleLabel.setFont(new Font(32));

    // Draw board previews
    for (Board board : boards) {
      VBox boardCard = createBoardCard(board);
      gamesContainer.getChildren().add(boardCard);
    }
    gamesContainer.setAlignment(Pos.CENTER);

    // Create buttons for file operations
    buttonContainer = new HBox(20);
    buttonContainer.setAlignment(Pos.CENTER);

    Button continueButton = new Button("Continue with Selected Board");
    continueButton.setDisable(true); // Initially disabled until a board is selected

    Button saveButton = new Button("Save Board to File");
    saveButton.setDisable(true); // Initially disabled until a board is selected

    Button loadButton = new Button("Load Board from File");

    buttonContainer.getChildren().addAll(continueButton, saveButton, loadButton);

    // Set button actions
    continueButton.setOnAction(event -> {
      if (selectedBoard != null) {
        GuiApp.getInstance().emitEvent(AppEvent.BOARD_CHOSEN, selectedBoard);
      }
    });

    saveButton.setOnAction(event -> {
      if (selectedBoard != null) {
        saveBoard();
      }
    });

    loadButton.setOnAction(event -> loadBoard());

    // Create a container for the selected board info
    boardCardContainer.setAlignment(Pos.CENTER);
    boardCardContainer.setPadding(new Insets(10));

    // Vertical box to hold elements
    VBox container = new VBox(20, titleLabel, gamesContainer, boardCardContainer, buttonContainer);
    container.setAlignment(Pos.CENTER);
    container.setPadding(new Insets(20));

    // Add vbox to StackPane
    this.getChildren().add(container);
    this.setAlignment(Pos.CENTER);
  }

  /**
   * Creates a visual card representation for a game board.
   * The card includes a canvas preview of the board and its name.
   * Clicking the card selects the board.
   *
   * @param board the board to create a card for
   * @return a VBox containing the board preview and name
   */
  private VBox createBoardCard(Board board) {
    VBox boardCard = new VBox();
    boardCard.setStyle("-fx-border-color: black; -fx-border-width: 1px;");

    BoardCanvas boardCanvas = BoardCanvasFactory.createBoardCanvas(gameType, board);
    boardCanvas.setWidth(300);
    boardCanvas.setHeight(300);

    // Board name
    Label boardName = new Label(board.getName());
    boardName.setFont(new Font(18));
    boardName.setStyle("-fx-padding: 10px;");

    boardCard.getChildren().addAll(boardCanvas, boardName);

    // Add click handler to select the board
    boardCard.setOnMouseClicked(event -> {
      selectedBoard = board;
      updateSelectedBoardInfo();

      // Emit the BOARD_SELECTED event so the controller can update the game model
      GuiApp.getInstance().emitEvent(AppEvent.BOARD_SELECTED, board);

      // Enable the buttons
      enableButtons();
    });

    return boardCard;
  }

  /**
   * Updates the display with information about the currently selected board.
   * Clears previous information and shows the name of the selected board.
   */
  private void updateSelectedBoardInfo() {
    boardCardContainer.getChildren().clear();

    if (selectedBoard != null) {
      Label infoLabel = new Label("Selected Board: " + selectedBoard.getName());
      infoLabel.setFont(new Font(16));
      boardCardContainer.getChildren().add(infoLabel);
    }
  }

  /**
   * Configures a FileChooser with common settings for board file operations.
   *
   * @param fileChooser the FileChooser to configure
   * @param title       the title to set for the FileChooser dialog
   */
  private void configureFileChooser(FileChooser fileChooser, String title) {
    fileChooser.setTitle(title);
    fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("JSON Files", "*.json")
    );

    // Set the initial directory to the boards directory in the project
    File boardsDir = FileConstants.BOARDS_DIR.toFile();
    // Ensure the directory exists
    try {
      FileUtils.ensureDirectoryExists(FileConstants.BOARDS_DIR);
      fileChooser.setInitialDirectory(boardsDir);
    } catch (DirectoryCreationException e) {
      GuiApp.getInstance().showMessage("Error accessing boards directory: " + e.getMessage());
    }
  }

  /**
   * Opens a save dialog and saves the currently selected board to a file.
   * Emits a SAVE_BOARD event with the selected file path.
   */
  private void saveBoard() {
    // Create a file chooser
    FileChooser fileChooser = new FileChooser();
    configureFileChooser(fileChooser, "Save Board");

    fileChooser.setInitialFileName(selectedBoard.getName() + ".json");

    // Show the save dialog
    File file = fileChooser.showSaveDialog(this.getScene().getWindow());

    if (file != null) {
      // Emit the save event
      GuiApp.getInstance().emitEvent(AppEvent.SAVE_BOARD, file.toPath());
    }
  }

  /**
   * Opens a load dialog and loads a board from the selected file.
   * Emits a LOAD_BOARD event with the selected file path.
   */
  private void loadBoard() {
    // Create a file chooser
    FileChooser fileChooser = new FileChooser();
    configureFileChooser(fileChooser, "Load Board");

    // Show the open dialog
    File file = fileChooser.showOpenDialog(this.getScene().getWindow());

    if (file != null) {
      // Emit the load event
      GuiApp.getInstance().emitEvent(AppEvent.LOAD_BOARD, file.toPath());
    }
  }

  /**
   * Adds a loaded board to the board selection screen.
   * This method is called from the BoardGameController when a board is loaded from a file.
   *
   * @param board the loaded board to add to the UI
   */
  public void addLoadedBoard(Board board) {
    // Add the board to the list if it's not already there
    if (!boards.contains(board)) {
      boards.add(board);
    }

    // Clear existing board cards
    gamesContainer.getChildren().clear();

    // Redraw all board cards
    for (Board b : boards) {
      VBox boardCard = createBoardCard(b);
      gamesContainer.getChildren().add(boardCard);
    }

    // Select the loaded board
    selectedBoard = board;
    updateSelectedBoardInfo();

    // Enable the buttons
    enableButtons();
  }

  /**
   * Helper method to enable all buttons in the button container.
   * This simplifies the code and makes it more readable.
   */
  private void enableButtons() {
    for (javafx.scene.Node node : buttonContainer.getChildren()) {
      if (node instanceof Button button) {
        button.setDisable(false);
      }
    }
  }
}
