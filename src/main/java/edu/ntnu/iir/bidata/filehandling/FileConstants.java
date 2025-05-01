package edu.ntnu.iir.bidata.filehandling;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Constants for file handling.
 */
public class FileConstants {
  /**
   * The directory where user files are stored.
   * Files are stored in the project directory as per requirements.
   */
  public static final Path USER_FILES_DIR = Paths.get("data");

  /**
   * The directory where board files are stored.
   */
  public static final Path BOARDS_DIR = USER_FILES_DIR.resolve("boards");

  /**
   * The directory where player files are stored.
   */
  public static final Path PLAYERS_DIR = USER_FILES_DIR.resolve("players");

  private FileConstants() {
    // Private constructor to prevent instantiation
  }
}
