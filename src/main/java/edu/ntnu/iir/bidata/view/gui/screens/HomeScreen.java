package edu.ntnu.iir.bidata.view.gui.screens;

import edu.ntnu.iir.bidata.view.gui.GUIApp;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class HomeScreen extends StackPane {
  public HomeScreen() {
    // Create title label
    Label titleLabel = new Label("IDATT2003 vår 2025 - mappe");
    titleLabel.setFont(new Font(24));

    // Create subtitle label
    Label subtitleLabel = new Label("by Gruppe 26");
    subtitleLabel.setFont(new Font(14));

    // Create buttons
    Button startButton = new Button("START");
    Button quitButton = new Button("QUIT");

    // Button click handlers
    startButton.setOnAction(event -> GUIApp.setContent(new ChooseGameScreen()));
    quitButton.setOnAction(event -> GUIApp.getInstance().quitApp());

    // Set button sizes
    startButton.setMinWidth(100);
    quitButton.setMinWidth(100);

    // Vertical box to hold elements
    VBox vbox = new VBox(10, titleLabel, subtitleLabel, startButton, quitButton);
    vbox.setAlignment(Pos.CENTER);

    // Add vbox to StackPane
    this.getChildren().add(vbox);
    this.setAlignment(Pos.CENTER);
  }
}
