package edu.ntnu.iir.bidata.view.GUI;

import edu.ntnu.iir.bidata.view.UIApp;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.geometry.Pos;
import javafx.stage.Stage;

import java.util.Stack;

public class GUIApp extends Application implements UIApp {
  static BorderPane sceneContent;
  static Stage stage;
  // Keep track of navigation history
  private static final Stack<Node> screenHistory = new Stack<>();

  @Override
  public void start(Stage stage) {
    sceneContent = new BorderPane();
    GUIApp.stage = stage;
    Scene scene = new Scene(sceneContent, 1000, 800);
    scene.setOnKeyPressed(event -> {
      switch (event.getCode()) {
        case ESCAPE -> quitApp();
      }
    });
    stage.setScene(scene);
    stage.show();

    // Set initial screen
    setContent(new HomeScreen(), false); // Don't add to history since it's first screen
  }

  public static void setContent(Node content) {
    setContent(content, true);
  }

  public static void setContent(Node content, boolean addToHistory) {
    // Add current screen to history before changing
    if (addToHistory && sceneContent.getCenter() != null) {
      screenHistory.push(sceneContent.getCenter());
    }

    // Change content
    sceneContent.setCenter(content);

    // Update back button visibility
    updateBackButton();
  }

  public static void goBack() {
    if (!screenHistory.isEmpty()) {
      Node previousScreen = screenHistory.pop();
      sceneContent.setCenter(previousScreen);
      updateBackButton();
    }
  }

  private static void updateBackButton() {
    if (screenHistory.isEmpty()) {
      sceneContent.setTop(null); // No back history, remove button
    } else {
      Button backButton = new Button("Go back");
      backButton.setOnAction(e -> goBack());
      BorderPane.setAlignment(backButton, Pos.TOP_LEFT);
      sceneContent.setTop(backButton);
    }
  }

  public static void quitApp() {
    stage.close();
  }

  public static void startApp() {
    launch();
  }
}