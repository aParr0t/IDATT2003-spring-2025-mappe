package edu.ntnu.iir.bidata.service;

import edu.ntnu.iir.bidata.exceptions.BoardDataException;
import edu.ntnu.iir.bidata.exceptions.CsvParsingException;
import edu.ntnu.iir.bidata.exceptions.DirectoryCreationException;
import edu.ntnu.iir.bidata.exceptions.FileNotFoundException;
import edu.ntnu.iir.bidata.exceptions.InvalidConfigurationException;
import edu.ntnu.iir.bidata.exceptions.JsonParsingException;
import edu.ntnu.iir.bidata.exceptions.PlayerDataException;
import edu.ntnu.iir.bidata.filehandling.BoardFileReader;
import edu.ntnu.iir.bidata.filehandling.BoardFileWriter;
import edu.ntnu.iir.bidata.filehandling.FileConstants;
import edu.ntnu.iir.bidata.filehandling.FileHandlerFactory;
import edu.ntnu.iir.bidata.filehandling.FileUtils;
import edu.ntnu.iir.bidata.filehandling.PlayerFileReader;
import edu.ntnu.iir.bidata.filehandling.PlayerFileWriter;
import edu.ntnu.iir.bidata.model.Board;
import edu.ntnu.iir.bidata.model.Player;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Service class for handling file operations related to boards and players.
 * This class centralizes all file handling logic to separate it from the GUI.
 */
public class FileHandlerService {

  /**
   * Saves a board to the specified file path.
   *
   * @param board    The board to save
   * @param filePath The path to save the board to
   * @throws IOException                   If an error occurs during saving
   * @throws DirectoryCreationException    If the directory cannot be created
   * @throws InvalidConfigurationException If the board configuration is invalid
   */
  public void saveBoard(Board board, Path filePath) throws
          IOException, DirectoryCreationException, InvalidConfigurationException {
    // Ensure the board exists
    if (board == null) {
      throw new InvalidConfigurationException("No board to save");
    }

    // Create the file handler
    BoardFileWriter writer = FileHandlerFactory.createBoardFileWriter();

    // Ensure directories exist
    FileUtils.ensureDirectoryExists(FileConstants.BOARDS_DIR);

    // Save the board
    writer.writeBoard(board, filePath);
  }

  /**
   * Loads a board from the specified file path.
   *
   * @param filePath The path to load the board from
   * @return The loaded board
   * @throws IOException           If an error occurs during loading
   * @throws FileNotFoundException If the file does not exist
   * @throws JsonParsingException  If there is an error parsing the JSON
   * @throws BoardDataException    If there is an error with the board data
   */
  public Board loadBoard(Path filePath) throws
          IOException, FileNotFoundException, JsonParsingException, BoardDataException {
    // Create the file handler
    BoardFileReader reader = FileHandlerFactory.createBoardFileReader();

    // Load and return the board
    return reader.readBoard(filePath);
  }

  /**
   * Saves players to the specified file path.
   *
   * @param players  The players to save
   * @param filePath The path to save the players to
   * @throws IOException                   If an error occurs during saving
   * @throws DirectoryCreationException    If the directory cannot be created
   * @throws InvalidConfigurationException If the player configuration is invalid
   */
  public void savePlayers(List<Player> players, Path filePath) throws
          IOException, DirectoryCreationException, InvalidConfigurationException {
    // Ensure players exist
    if (players == null || players.isEmpty()) {
      throw new InvalidConfigurationException("No players to save");
    }

    // Create the file handler
    PlayerFileWriter writer = FileHandlerFactory.createPlayerFileWriter();

    // Ensure directories exist
    FileUtils.ensureDirectoryExists(FileConstants.PLAYERS_DIR);

    // Save the players
    writer.writePlayers(players, filePath);
  }

  /**
   * Loads players from the specified file path.
   *
   * @param filePath The path to load the players from
   * @return The loaded players
   * @throws IOException           If an error occurs during loading
   * @throws FileNotFoundException If the file does not exist
   * @throws CsvParsingException   If there is an error parsing the CSV
   * @throws PlayerDataException   If there is an error with the player data
   */
  public List<Player> loadPlayers(Path filePath) throws
          IOException, FileNotFoundException, CsvParsingException, PlayerDataException {
    // Create the file handler
    PlayerFileReader reader = FileHandlerFactory.createPlayerFileReader();

    // Load and return the players
    return reader.readPlayers(filePath);
  }
}
