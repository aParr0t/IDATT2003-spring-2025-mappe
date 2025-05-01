package edu.ntnu.iir.bidata.view.gui;

import edu.ntnu.iir.bidata.view.UIApp;
import edu.ntnu.iir.bidata.view.gui.screens.HomeScreen;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.geometry.Pos;
import javafx.stage.Stage;

import java.util.*;

import edu.ntnu.iir.bidata.view.AppEvent;
import edu.ntnu.iir.bidata.view.GameEventListener;

public class GUIApp extends Application implements UIApp {
  private static final Map<AppEvent<?>, List<GameEventListener<?>>> eventListeners = new HashMap<>();  // (Help from AI)
  private static BorderPane sceneContent;
  private static Stage stage;
  private static final Stack<Node> screenHistory = new Stack<>();  // Keep track of navigation history
  private static GUIApp instance;
  private static boolean showBackButton = true;  // Flag to control back button visibility

  public GUIApp() {
    instance = this;
  }

  @Override
  public void start(Stage stage) {
    GUIApp.sceneContent = new BorderPane();
    GUIApp.stage = stage;
    Scene scene = new Scene(sceneContent, 1000, 800);
    scene.setOnKeyPressed(event -> {
      if (Objects.requireNonNull(event.getCode()) == KeyCode.ESCAPE) {
        quitApp();
      }
    });
    stage.setScene(scene);
    stage.show();

    // Set initial screen
    setContent(new HomeScreen(), false); // Don't add to history since it's first screen
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
    setContent(content, addToHistory, true);
  }

  public static void setContent(Node content, boolean addToHistory, boolean showBackButton) {
    // Add current screen to history before changing
    if (addToHistory && sceneContent.getCenter() != null) {
      screenHistory.push(sceneContent.getCenter());
    }

    // Change content
    sceneContent.setCenter(content);

    // Set the back button visibility flag
    GUIApp.showBackButton = showBackButton;

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
    if (screenHistory.isEmpty() || !showBackButton) {
      sceneContent.setTop(null); // No back history or back button disabled
    } else {
      Button backButton = new Button("Go back");
      backButton.setOnAction(e -> goBack());
      BorderPane.setAlignment(backButton, Pos.TOP_LEFT);
      sceneContent.setTop(backButton);
    }
  }

  public void quitApp() {
    emitEvent(AppEvent.QUIT);
    stage.close();
  }

  public void startApp() {
    launch();
  }

  /**
   * (Help from AI: help with generics)
   */
  @Override
  public <T> void addEventListener(AppEvent<T> event, GameEventListener<T> listener) {
    if (!eventListeners.containsKey(event)) {
      eventListeners.put(event, new ArrayList<>());
    }
    eventListeners.get(event).add(listener);
  }

  /**
   * (Help from AI: help with generics)
   */
  @Override
  public <T> void removeEventListener(AppEvent<T> event, GameEventListener<T> listener) {
    if (eventListeners.containsKey(event)) {
      eventListeners.get(event).remove(listener);
    }
  }

  /**
   * (Help from AI: help with generics)
   */
  @SuppressWarnings("unchecked")
  @Override
  public <T> void emitEvent(AppEvent<T> event, T data) {
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
  public void emitEvent(AppEvent<Void> event) {
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

  /**
   * Gets the current content displayed in the application.
   *
   * @return The current content node
   */
  public static Node getCurrentContent() {
    return sceneContent.getCenter();
  }
}
