package edu.ntnu.iir.bidata.controller;

import edu.ntnu.iir.bidata.exceptions.BoardDataException;
import edu.ntnu.iir.bidata.exceptions.CsvParsingException;
import edu.ntnu.iir.bidata.exceptions.DirectoryCreationException;
import edu.ntnu.iir.bidata.exceptions.FileNotFoundException;
import edu.ntnu.iir.bidata.exceptions.InvalidConfigurationException;
import edu.ntnu.iir.bidata.exceptions.JsonParsingException;
import edu.ntnu.iir.bidata.exceptions.PlayerDataException;
import edu.ntnu.iir.bidata.model.Board;
import edu.ntnu.iir.bidata.model.BoardFactory;
import edu.ntnu.iir.bidata.model.Player;
import edu.ntnu.iir.bidata.model.PlayerConfigResponse;
import edu.ntnu.iir.bidata.model.games.Game;
import edu.ntnu.iir.bidata.model.games.GameFactory;
import edu.ntnu.iir.bidata.model.games.MonopolyGame;
import edu.ntnu.iir.bidata.service.FileHandlerService;
import edu.ntnu.iir.bidata.view.AppEvent;
import edu.ntnu.iir.bidata.view.gui.GuiApp;
import edu.ntnu.iir.bidata.view.gui.screens.ChooseBoardScreen;
import edu.ntnu.iir.bidata.view.gui.screens.ChoosePlayerScreen;
import edu.ntnu.iir.bidata.view.gui.screens.GameOverScreen;
import edu.ntnu.iir.bidata.view.gui.screens.GameScreenFactory;
import edu.ntnu.iir.bidata.view.gui.screens.HomeScreen;
import edu.ntnu.iir.bidata.view.gui.screens.MonopolyScreen;
import edu.ntnu.iir.bidata.view.gui.screens.SnakesAndLaddersScreen;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.StackPane;

/**
 * Controller class for managing board game logic and GUI interactions.
 * Handles game initialization, player configuration, board management, and event processing.
 * Serves as the main controller linking the game model with the user interface.
 */
public class BoardGameController {
  private Game game;
  private StackPane gameplayScreen;
  private final FileHandlerService fileHandlerService;

  /**
   * Constructs a new BoardGameController instance.
   * Initializes the file handler service for managing game data persistence.
   */
  public BoardGameController() {
    this.fileHandlerService = new FileHandlerService();
  }

  /**
   * Sets up all the event listeners for the application.
   * Configures responses to various game events like game selection, board selection,
   * player configuration, in-game actions, and file operations.
   */
  public void setup() {
    GuiApp.getInstance().addEventListener(AppEvent.QUIT, event -> {
      System.out.println("quitted");
    });

    GuiApp.getInstance().addEventListener(AppEvent.GAME_CHOSEN, gameType -> {
      // Create game using the GameFactory
      game = GameFactory.createGame(gameType);
      // update view
      List<Board> boards = BoardFactory.getAllBoardsForGameType(gameType);
      GuiApp.setContent(new ChooseBoardScreen(gameType, boards), true, true);
    });

    GuiApp.getInstance().addEventListener(AppEvent.BOARD_SELECTED, board -> {
      // Update model immediately when a board is selected
      game.setBoard(board);
    });

    GuiApp.getInstance().addEventListener(AppEvent.BOARD_CHOSEN, board -> {
      // Board is already set in the game model from BOARD_SELECTED event
      // Just proceed to the next screen
      List<Player> players = List.of(
              new Player("Atas"),
              new Player("Stian")
      );
      GuiApp.setContent(
              new ChoosePlayerScreen(
                      players, game.getAllPlayingPieces(), game.getMaxPlayers()
              ), true, true);
    });

    GuiApp.getInstance().addEventListener(AppEvent.PLAYERS_CHOSEN, players -> {
      PlayerConfigResponse response = game.isPlayerConfigOk(players);
      if (!response.isPlayerConfigOk()) {
        GuiApp.getInstance().showMessage(response.errorMessage());
        return;
      }
      // update model
      game.setPlayers(players);

      // start game
      game.start();
      // Create game screen using the GameScreenFactory
      gameplayScreen = GameScreenFactory.createGameScreen(game.getGameType(), game.getBoard());
      GuiApp.setContent(gameplayScreen, true, false);
      updateGameScreen();
    });

    GuiApp.getInstance().addEventListener(AppEvent.IN_GAME_EVENT, event -> {
      game.handleEvent(event);
      updateGameScreen();
      if (game.isGameOver()) {
        GuiApp.setContent(new GameOverScreen(game.getWinner().getName()), false, false);
      }
    });

    GuiApp.getInstance().addEventListener(AppEvent.PLAY_AGAIN, event -> {
      GuiApp.setContent(new HomeScreen(), false, false);
    });

    // File handling event listeners
    GuiApp.getInstance().addEventListener(AppEvent.SAVE_BOARD, filePath -> {
      try {
        // Ensure the board exists
        if (game == null || game.getBoard() == null) {
          GuiApp.getInstance().showMessage("No board to save");
          return;
        }

        // Use the file handler service to save the board
        fileHandlerService.saveBoard(game.getBoard(), filePath);

        GuiApp.getInstance().showMessage("Board saved successfully to " + filePath);
      } catch (DirectoryCreationException e) {
        GuiApp.getInstance().showMessage("Error creating directory: " + e.getMessage());
      } catch (InvalidConfigurationException e) {
        GuiApp.getInstance().showMessage("Invalid board configuration: " + e.getMessage());
      } catch (IOException e) {
        GuiApp.getInstance().showMessage("Error saving board: " + e.getMessage());
      }
    });

    GuiApp.getInstance().addEventListener(AppEvent.LOAD_BOARD, filePath -> {
      try {
        // Use the file handler service to load the board
        Board board = fileHandlerService.loadBoard(filePath);

        // Set a fixed name for the loaded board
        board.setName("Loaded board");

        // Update the game with the loaded board
        if (game != null) {
          game.setBoard(board);

          // Get the current screen
          if (GuiApp.getCurrentContent() instanceof ChooseBoardScreen chooseBoardScreen) {
            chooseBoardScreen.addLoadedBoard(board);
          }

          GuiApp.getInstance().showMessage("Board loaded successfully from " + filePath);
        } else {
          GuiApp.getInstance().showMessage("No game selected to load the board into");
        }
      } catch (FileNotFoundException e) {
        GuiApp.getInstance().showMessage("Board file not found: " + e.getFilePath());
      } catch (JsonParsingException e) {
        GuiApp.getInstance().showMessage("Error parsing board file: " + e.getMessage());
      } catch (BoardDataException e) {
        GuiApp.getInstance().showMessage("Invalid board data: " + e.getMessage());
      } catch (IOException e) {
        GuiApp.getInstance().showMessage("Error loading board: " + e.getMessage());
      }
    });

    GuiApp.getInstance().addEventListener(AppEvent.SAVE_PLAYERS, tuple -> {
      try {
        Path filePath = tuple.first();
        List<Player> players = tuple.second();

        // Ensure players exist
        if (players.isEmpty()) {
          GuiApp.getInstance().showMessage("No players to save");
          return;
        }

        // Use the file handler service to save the players
        fileHandlerService.savePlayers(players, filePath);

        GuiApp.getInstance().showMessage("Players saved successfully to " + filePath);
      } catch (DirectoryCreationException e) {
        GuiApp.getInstance().showMessage("Error creating directory: " + e.getMessage());
      } catch (InvalidConfigurationException e) {
        GuiApp.getInstance().showMessage("Invalid player configuration: " + e.getMessage());
      } catch (IOException e) {
        GuiApp.getInstance().showMessage("Error saving players: " + e.getMessage());
      }
    });

    GuiApp.getInstance().addEventListener(AppEvent.LOAD_PLAYERS, filePath -> {
      try {
        // Use the file handler service to load the players
        List<Player> players = fileHandlerService.loadPlayers(filePath);

        // Update the game with the loaded players or update the player selection screen
        if (game != null) {
          // Check if the player configuration is valid
          PlayerConfigResponse response = game.isPlayerConfigOk(players);
          if (response.isPlayerConfigOk()) {
            game.setPlayers(players);

            // Update the ChoosePlayerScreen if it's the current screen
            if (GuiApp.getCurrentContent() instanceof ChoosePlayerScreen choosePlayerScreen) {
              choosePlayerScreen.updatePlayers(players);
            }

            GuiApp.getInstance().showMessage("Players loaded successfully from " + filePath);
          } else {
            GuiApp.getInstance().showMessage(
                    "Invalid player configuration: " + response.errorMessage()
            );
          }
        } else {
          GuiApp.getInstance().showMessage("No game selected to load the players into");
        }
      } catch (FileNotFoundException e) {
        GuiApp.getInstance().showMessage("Player file not found: " + e.getFilePath());
      } catch (CsvParsingException e) {
        GuiApp.getInstance().showMessage("Error parsing player file: " + e.getMessage());
      } catch (PlayerDataException e) {
        GuiApp.getInstance().showMessage("Invalid player data: " + e.getMessage());
      } catch (IOException e) {
        GuiApp.getInstance().showMessage("Error loading players: " + e.getMessage());
      }
    });
  }

  /**
   * Updates the game screen with current game state.
   * Handles different game screen types and updates them with appropriate data.
   *
   * @throws IllegalArgumentException if the game screen type is invalid
   */
  private void updateGameScreen() {
    if (gameplayScreen instanceof SnakesAndLaddersScreen) {
      ((SnakesAndLaddersScreen) gameplayScreen).update(
              game.getPlayers(),
              game.getCurrentPlayer(),
              game.getDiceCounts()
      );
    } else if (gameplayScreen instanceof MonopolyScreen) {
      List<Integer> playerMoney = new ArrayList<>();

      // Get player money if this is a MonopolyGame
      if (game instanceof MonopolyGame monopolyGame) {
        for (Player player : game.getPlayers()) {
          playerMoney.add(monopolyGame.getPlayerMoney(player));
        }
      }

      ((MonopolyScreen) gameplayScreen).update(
              game.getPlayers(),
              game.getCurrentPlayer(),
              game.getDiceCounts(),
              playerMoney
      );
    } else {
      throw new IllegalArgumentException("Invalid game screen type: " + gameplayScreen.getClass());
    }
  }

  /**
   * Starts the application.
   * Initiates the GUI application instance to begin the game.
   */
  public void run() {
    GuiApp.getInstance().startApp();
  }
}
