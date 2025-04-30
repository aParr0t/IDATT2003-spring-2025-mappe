package edu.ntnu.iir.bidata.view.gui.screens;

import edu.ntnu.iir.bidata.filehandling.FileConstants;
import edu.ntnu.iir.bidata.filehandling.FileUtils;
import edu.ntnu.iir.bidata.model.Board;
import edu.ntnu.iir.bidata.model.GameType;
import edu.ntnu.iir.bidata.view.AppEvent;
import edu.ntnu.iir.bidata.view.gui.BoardCanvas;
import edu.ntnu.iir.bidata.view.gui.BoardCanvasFactory;
import edu.ntnu.iir.bidata.view.gui.GUIApp;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class ChooseBoardScreen extends StackPane {
  private Board selectedBoard;
  private final GameType gameType;
  private final List<Board> boards;
  private final VBox boardCardContainer = new VBox(10);

  public ChooseBoardScreen(GameType gameType, List<Board> boards) {
    this.gameType = gameType;
    this.boards = boards;

    // Create title label
    Label titleLabel = new Label("Choose a board");
    titleLabel.setFont(new Font(32));

    // Draw board previews
    HBox gamesContainer = new HBox(20);
    for (Board board : boards) {
      VBox boardCard = createBoardCard(board);
      gamesContainer.getChildren().add(boardCard);
    }
    gamesContainer.setAlignment(Pos.CENTER);

    // Create buttons for file operations
    HBox buttonContainer = new HBox(20);
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
        GUIApp.getInstance().emitEvent(AppEvent.BOARD_CHOSEN, selectedBoard);
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
      GUIApp.getInstance().emitEvent(AppEvent.BOARD_SELECTED, board);

      // Enable the buttons
      for (javafx.scene.Node node : ((HBox) ((VBox) this.getChildren().get(0)).getChildren().get(3)).getChildren()) {
        if (node instanceof Button) {
          Button button = (Button) node;
          button.setDisable(false);
        }
      }
    });

    return boardCard;
  }

  private void updateSelectedBoardInfo() {
    boardCardContainer.getChildren().clear();

    if (selectedBoard != null) {
      Label infoLabel = new Label("Selected Board: " + selectedBoard.getName());
      infoLabel.setFont(new Font(16));
      boardCardContainer.getChildren().add(infoLabel);
    }
  }

  private void saveBoard() {
    try {
      // Ensure the directory exists
      FileUtils.ensureDirectoryExists(FileConstants.BOARDS_DIR);

      // Create a file chooser
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Save Board");
      fileChooser.setInitialDirectory(FileConstants.BOARDS_DIR.toFile());
      fileChooser.getExtensionFilters().add(
              new FileChooser.ExtensionFilter("JSON Files", "*" + FileConstants.BOARD_FILE_EXTENSION)
      );
      fileChooser.setInitialFileName(selectedBoard.getName() + FileConstants.BOARD_FILE_EXTENSION);

      // Show the save dialog
      File file = fileChooser.showSaveDialog(this.getScene().getWindow());

      if (file != null) {
        // Emit the save event
        GUIApp.getInstance().emitEvent(AppEvent.SAVE_BOARD, file.toPath());
      }
    } catch (IOException e) {
      GUIApp.getInstance().showMessage("Error preparing to save board: " + e.getMessage());
    }
  }

  private void loadBoard() {
    try {
      // Ensure the directory exists
      FileUtils.ensureDirectoryExists(FileConstants.BOARDS_DIR);

      // Create a file chooser
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Load Board");
      fileChooser.setInitialDirectory(FileConstants.BOARDS_DIR.toFile());
      fileChooser.getExtensionFilters().add(
              new FileChooser.ExtensionFilter("JSON Files", "*" + FileConstants.BOARD_FILE_EXTENSION)
      );

      // Show the open dialog
      File file = fileChooser.showOpenDialog(this.getScene().getWindow());

      if (file != null) {
        // Emit the load event
        GUIApp.getInstance().emitEvent(AppEvent.LOAD_BOARD, file.toPath());
      }
    } catch (IOException e) {
      GUIApp.getInstance().showMessage("Error preparing to load board: " + e.getMessage());
    }
  }
}
