package edu.ntnu.iir.bidata.view.GUI;

import edu.ntnu.iir.bidata.model.Board;
import edu.ntnu.iir.bidata.model.GameType;
import edu.ntnu.iir.bidata.view.GameEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.List;

public class ChooseBoardScreen extends StackPane {
  public ChooseBoardScreen(GameType gameType, List<Board> boards) {
    // Create title label
    Label titleLabel = new Label("Choose a board");
    titleLabel.setFont(new Font(32));

    // draw game previews
    HBox gamesContainer = new HBox(20);
    for (Board board : boards) {
      VBox boardCard = new VBox();
      boardCard.setStyle("-fx-border-color: black; -fx-border-width: 1px;");

      BoardCanvas boardCanvas = BoardCanvasFactory.createBoardCanvas(gameType, board);
      boardCanvas.setWidth(300);
      boardCanvas.setHeight(300);

      // game name
      Label gameName = new Label(board.getName());
      gameName.setFont(new Font(18));
      gameName.setStyle("-fx-padding: 10px;");

      boardCard.getChildren().addAll(boardCanvas, gameName);
      gamesContainer.getChildren().add(boardCard);

      // add click handler to gameCard
      boardCard.setOnMouseClicked(event -> {
        GUIApp.getInstance().emitEvent(GameEvent.BOARD_CHOSEN, board);
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
