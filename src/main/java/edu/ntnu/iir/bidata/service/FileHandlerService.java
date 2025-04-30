package edu.ntnu.iir.bidata.service;

import edu.ntnu.iir.bidata.filehandling.*;
import edu.ntnu.iir.bidata.model.Board;
import edu.ntnu.iir.bidata.model.Player;
import edu.ntnu.iir.bidata.view.gui.GUIApp;

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
     * @param board The board to save
     * @param filePath The path to save the board to
     * @throws IOException If an error occurs during saving
     */
    public void saveBoard(Board board, Path filePath) throws IOException {
        // Ensure the board exists
        if (board == null) {
            throw new IllegalArgumentException("No board to save");
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
     * @throws IOException If an error occurs during loading
     */
    public Board loadBoard(Path filePath) throws IOException {
        // Create the file handler
        BoardFileReader reader = FileHandlerFactory.createBoardFileReader();
        
        // Load and return the board
        return reader.readBoard(filePath);
    }
    
    /**
     * Saves players to the specified file path.
     * 
     * @param players The players to save
     * @param filePath The path to save the players to
     * @throws IOException If an error occurs during saving
     */
    public void savePlayers(List<Player> players, Path filePath) throws IOException {
        // Ensure players exist
        if (players == null || players.isEmpty()) {
            throw new IllegalArgumentException("No players to save");
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
     * @throws IOException If an error occurs during loading
     */
    public List<Player> loadPlayers(Path filePath) throws IOException {
        // Create the file handler
        PlayerFileReader reader = FileHandlerFactory.createPlayerFileReader();
        
        // Load and return the players
        return reader.readPlayers(filePath);
    }
    
    /**
     * Ensures that the boards directory exists.
     * 
     * @throws IOException If an error occurs while creating the directory
     */
    public void ensureBoardsDirectoryExists() throws IOException {
        FileUtils.ensureDirectoryExists(FileConstants.BOARDS_DIR);
    }
    
    /**
     * Ensures that the players directory exists.
     * 
     * @throws IOException If an error occurs while creating the directory
     */
    public void ensurePlayersDirectoryExists() throws IOException {
        FileUtils.ensureDirectoryExists(FileConstants.PLAYERS_DIR);
    }
}
