package edu.ntnu.iir.bidata.view.gui.screens;

import edu.ntnu.iir.bidata.view.AppEvent;
import edu.ntnu.iir.bidata.view.gui.GuiApp;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

/**
 * A screen displayed when a game is over, showing the winner and a play again option.
 * This screen is shown after a game has been completed.
 */
public class GameOverScreen extends StackPane {

  /**
   * Creates a new game over screen with the specified winner name.
   *
   * @param winnerName the name of the player who won the game
   */
  public GameOverScreen(String winnerName) {

    final VBox vbox = new VBox(20);

    // winner label
    Label winnerLabel = new Label("Winner: " + winnerName);
    winnerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
    winnerLabel.setTextAlignment(TextAlignment.CENTER);

    // play again button
    Button playAgainButton = new Button("Play Again");
    playAgainButton.setOnAction(e -> GuiApp.getInstance().emitEvent(AppEvent.PLAY_AGAIN));

    vbox.getChildren().addAll(winnerLabel, playAgainButton);
    vbox.setAlignment(Pos.CENTER);
    getChildren().add(vbox);
  }
}
