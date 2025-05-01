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

/**
 * JavaFX application class that implements the UIApp interface.
 * Manages the GUI components, screen navigation, and event handling for the application.
 */
public class GUIApp extends Application implements UIApp {
  private static final Map<AppEvent<?>, List<GameEventListener<?>>> eventListeners = new HashMap<>();  // (Help from AI)
  private static BorderPane sceneContent;
  private static Stage stage;
  private static final Stack<Node> screenHistory = new Stack<>();  // Keep track of navigation history
  private static GUIApp instance;
  private static boolean showBackButton = true;  // Flag to control back button visibility

  /**
   * Creates a new GUIApp instance and sets it as the singleton instance.
   */
  public GUIApp() {
    instance = this;
  }

  /**
   * Initializes the JavaFX application with the primary stage.
   * Sets up the main scene, key event handlers, and loads the initial screen.
   *
   * @param stage the primary stage for this application
   */
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

  /**
   * Returns the singleton instance of GUIApp.
   * Creates a new instance if none exists.
   *
   * @return the singleton instance of GUIApp
   */
  public static GUIApp getInstance() {
    if (instance == null) {
      instance = new GUIApp();
    }
    return instance;
  }

  /**
   * Sets the content of the application with the given node.
   * Adds the current screen to the navigation history.
   *
   * @param content the node to set as the content
   */
  public static void setContent(Node content) {
    GUIApp.setContent(content, true);
  }

  /**
   * Sets the content of the application with the given node.
   * Controls whether to add the current screen to the navigation history.
   *
   * @param content the node to set as the content
   * @param addToHistory whether to add the current screen to history
   */
  public static void setContent(Node content, boolean addToHistory) {
    setContent(content, addToHistory, true);
  }

  /**
   * Sets the content of the application with the given node.
   * Controls navigation history and back button visibility.
   *
   * @param content the node to set as the content
   * @param addToHistory whether to add the current screen to history
   * @param showBackButton whether to show the back navigation button
   */
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

  /**
   * Navigates back to the previous screen in the history stack.
   * Does nothing if the history is empty.
   */
  public static void goBack() {
    if (!screenHistory.isEmpty()) {
      Node previousScreen = screenHistory.pop();
      sceneContent.setCenter(previousScreen);
      updateBackButton();
    }
  }

  /**
   * Updates the visibility and state of the back button based on
   * the navigation history and the showBackButton flag.
   */
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

  /**
   * Quits the application by emitting a quit event and closing the stage.
   */
  public void quitApp() {
    emitEvent(AppEvent.QUIT);
    stage.close();
  }

  /**
   * Starts the JavaFX application by calling the launch method.
   */
  public void startApp() {
    launch();
  }

  /**
   * Adds an event listener for the specified event type.
   *
   * @param <T> the type of data associated with the event
   * @param event the event to listen for
   * @param listener the listener to be notified when the event occurs
   */
  @Override
  public <T> void addEventListener(AppEvent<T> event, GameEventListener<T> listener) {
    if (!eventListeners.containsKey(event)) {
      eventListeners.put(event, new ArrayList<>());
    }
    eventListeners.get(event).add(listener);
  }

  /**
   * Removes an event listener for the specified event type.
   *
   * @param <T> the type of data associated with the event
   * @param event the event to stop listening for
   * @param listener the listener to be removed
   */
  @Override
  public <T> void removeEventListener(AppEvent<T> event, GameEventListener<T> listener) {
    if (eventListeners.containsKey(event)) {
      eventListeners.get(event).remove(listener);
    }
  }

  /**
   * Emits an event with associated data to all registered listeners.
   *
   * @param <T> the type of data associated with the event
   * @param event the event to emit
   * @param data the data to pass to listeners
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
   * Emits an event without data to all registered listeners.
   *
   * @param event the event to emit
   */
  @Override
  public void emitEvent(AppEvent<Void> event) {
    UIApp.super.emitEvent(event);
  }

  /**
   * Displays an information message dialog to the user.
   *
   * @param message the message to display
   */
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
   * @return the current content node
   */
  public static Node getCurrentContent() {
    return sceneContent.getCenter();
  }
}
