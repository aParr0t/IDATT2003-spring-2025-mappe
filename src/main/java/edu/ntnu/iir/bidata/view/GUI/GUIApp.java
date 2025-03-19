package edu.ntnu.iir.bidata.view.GUI;

import edu.ntnu.iir.bidata.view.UIApp;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class GUIApp extends Application implements UIApp {
  BorderPane sceneContent;
  Stage stage;

  @Override
  public void start(Stage stage) {
    sceneContent = new BorderPane();
    this.stage = stage;
    Scene scene = new Scene(sceneContent, 1000, 800);
    scene.setOnKeyPressed(event -> {
      switch (event.getCode()) {
        case ESCAPE -> quitApp();
      }
    });
    stage.setScene(scene);
    stage.show();

    setContent(new HomeScreen());
  }

  public void setContent(Node content) {
    this.sceneContent.setCenter(content);
  }

  public void quitApp() {
    stage.close();
  }

  public void startApp() {
    launch();
  }
}