package edu.ntnu.iir.bidata.view.GUI;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

public class GoBackButton {
  static void addButtonToScene(BorderPane scene, Node newContent) {
    Button goBackButton = new Button("Go back");

    // Button click handler
    goBackButton.setOnAction(event -> {
      GUIApp.setContent(newContent);
      scene.setTop(null);  // remove itself from the scene
    });

    // Add button to scene
    BorderPane.setAlignment(goBackButton, Pos.TOP_LEFT);
    scene.setTop(goBackButton);
  }
}
