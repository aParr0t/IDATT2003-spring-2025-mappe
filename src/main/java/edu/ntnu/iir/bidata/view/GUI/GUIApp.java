package edu.ntnu.iir.bidata.view.GUI;

import edu.ntnu.iir.bidata.view.UIApp;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.geometry.Pos;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;

import edu.ntnu.iir.bidata.view.GameEvent;
import edu.ntnu.iir.bidata.view.GameEventListener;

import java.util.Stack;

public class GUIApp extends Application implements UIApp {
  private static Map<GameEvent<?>, List<GameEventListener<?>>> eventListeners = new HashMap<>();  // (Help from AI)
  private static BorderPane sceneContent;
  private static Stage stage;
  private static final Stack<Node> screenHistory = new Stack<>();  // Keep track of navigation history
  private static GUIApp instance;

  public GUIApp() {
    instance = this;
  }

  @Override
  public void start(Stage stage) {
    GUIApp.sceneContent = new BorderPane();
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
//    setContent(new GameplayScreen(), false); // Don't add to history since it's first screen
  }

  // Static method to get the instance
  public static GUIApp getInstance() {
    if (instance == null) {
      instance = new GUIApp();
    }
    return instance;
  }

  public static void setContent(Node content) {
    GUIApp.setContent(content, true);
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

  public void quitApp() {
    emitEvent(GameEvent.QUIT);
    stage.close();
  }

  public void startApp() {
    launch();
  }

  /**
   * (Help from AI: help with generics)
   */
  @Override
  public <T> void addEventListener(GameEvent<T> event, GameEventListener<T> listener) {
    if (!eventListeners.containsKey(event)) {
      eventListeners.put(event, new ArrayList<>());
    }
    eventListeners.get(event).add(listener);
  }

  /**
   * (Help from AI: help with generics)
   */
  @Override
  public <T> void removeEventListener(GameEvent<T> event, GameEventListener<T> listener) {
    if (eventListeners.containsKey(event)) {
      eventListeners.get(event).remove(listener);
    }
  }

  /**
   * (Help from AI: help with generics)
   */
  @SuppressWarnings("unchecked")
  @Override
  public <T> void emitEvent(GameEvent<T> event, T data) {
    if (eventListeners.containsKey(event)) {
      for (GameEventListener<?> listener : eventListeners.get(event)) {
        ((GameEventListener<T>) listener).onEvent(data);
      }
    }
  }

  /**
   * (Help from AI: help with generics)
   */
  @Override
  public void emitEvent(GameEvent<Void> event) {
    UIApp.super.emitEvent(event);
  }

  @Override
  public void showMessage(String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Information");
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }
}