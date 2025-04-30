package edu.ntnu.iir.bidata;

import edu.ntnu.iir.bidata.controller.BoardGameController;
import edu.ntnu.iir.bidata.exceptions.DirectoryCreationException;
import edu.ntnu.iir.bidata.filehandling.FileConstants;
import edu.ntnu.iir.bidata.filehandling.FileUtils;

import java.io.IOException;

public class Main {
  public static void main(String[] args) {
    // Create necessary directories for file storage
    try {
      FileUtils.ensureDirectoryExists(FileConstants.USER_FILES_DIR);
      FileUtils.ensureDirectoryExists(FileConstants.BOARDS_DIR);
      FileUtils.ensureDirectoryExists(FileConstants.PLAYERS_DIR);
    } catch (DirectoryCreationException e) {
      System.err.println("Error creating directories: " + e.getMessage());
    }

    // Start the application
    BoardGameController app = new BoardGameController();
    app.setup();
    app.run();
  }
}
