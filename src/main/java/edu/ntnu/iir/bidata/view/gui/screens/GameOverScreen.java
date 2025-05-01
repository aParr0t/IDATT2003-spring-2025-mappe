package edu.ntnu.iir.bidata.view.gui.screens;

import edu.ntnu.iir.bidata.view.AppEvent;
import edu.ntnu.iir.bidata.view.gui.GUIApp;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;


public class GameOverScreen extends StackPane {
  public GameOverScreen(String winnerName) {

    VBox vbox = new VBox(20);

    // winner label
    Label winnerLabel = new Label("Winner: " + winnerName);
    winnerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
    winnerLabel.setTextAlignment(TextAlignment.CENTER);

    // play again button
    Button playAgainButton = new Button("Play Again");
    playAgainButton.setOnAction(e -> GUIApp.getInstance().emitEvent(AppEvent.PLAY_AGAIN));

    vbox.getChildren().addAll(winnerLabel, playAgainButton);
    vbox.setAlignment(Pos.CENTER);
    getChildren().add(vbox);
  }
}
