package edu.ntnu.iir.bidata.filehandling;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Constants for file handling.
 */
public class FileConstants {
    /**
     * The directory where user files are stored.
     */
    public static final Path USER_FILES_DIR = Paths.get(System.getProperty("user.home"), "boardgame_files");

    /**
     * The directory where board files are stored.
     */
    public static final Path BOARDS_DIR = USER_FILES_DIR.resolve("boards");

    /**
     * The directory where player files are stored.
     */
    public static final Path PLAYERS_DIR = USER_FILES_DIR.resolve("players");

    /**
     * The file extension for board files.
     */
    public static final String BOARD_FILE_EXTENSION = ".json";

    /**
     * The file extension for player files.
     */
    public static final String PLAYER_FILE_EXTENSION = ".csv";

    private FileConstants() {
        // Private constructor to prevent instantiation
    }
}
