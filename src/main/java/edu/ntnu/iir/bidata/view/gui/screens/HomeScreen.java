package edu.ntnu.iir.bidata.view.gui.screens;

import edu.ntnu.iir.bidata.view.gui.GuiApp;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 * The home screen of the application displaying the welcome page with title and navigation options.
 * This screen serves as the entry point for users, providing options to start the game or quit.
 */
public class HomeScreen extends StackPane {
  /**
   * Constructs a new HomeScreen with title, subtitle, and navigation buttons.
   * Initializes the UI components and sets up event handlers for the buttons.
   */
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
    startButton.setOnAction(event -> GuiApp.setContent(new ChooseGameScreen()));
    quitButton.setOnAction(event -> GuiApp.getInstance().quitApp());

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
