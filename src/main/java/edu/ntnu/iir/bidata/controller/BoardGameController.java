package edu.ntnu.iir.bidata.controller;

import edu.ntnu.iir.bidata.model.*;
import edu.ntnu.iir.bidata.service.FileHandlerService;
import edu.ntnu.iir.bidata.model.games.Game;
import edu.ntnu.iir.bidata.model.games.MonopolyGame;
import edu.ntnu.iir.bidata.model.games.SnakesAndLaddersGame;
import edu.ntnu.iir.bidata.view.gui.screens.*;
import edu.ntnu.iir.bidata.view.gui.GUIApp;
import edu.ntnu.iir.bidata.view.AppEvent;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Runs the game in a text-based version.
 */
public class BoardGameController {
  private Game game;
  private StackPane gameplayScreen;
  private final FileHandlerService fileHandlerService;

  public BoardGameController() {
    this.fileHandlerService = new FileHandlerService();
  }

  public void setup() {
    GUIApp.getInstance().addEventListener(AppEvent.QUIT, event -> {
      System.out.println("quitted");
    });

    GUIApp.getInstance().addEventListener(AppEvent.GAME_CHOSEN, gameType -> {
      // TODO: maybe use a factory pattern to create the game
      if (gameType == GameType.SNAKES_AND_LADDERS) {
        game = new SnakesAndLaddersGame();
      } else if (gameType == GameType.MONOPOLY) {
        game = new MonopolyGame();
      } else {
        throw new IllegalArgumentException("Invalid game type: " + gameType);
      }
      // update view
      List<Board> boards = BoardFactory.getAllBoardsForGameType(gameType);
      GUIApp.setContent(new ChooseBoardScreen(gameType, boards), true, true);
    });

    GUIApp.getInstance().addEventListener(AppEvent.BOARD_SELECTED, board -> {
      // Update model immediately when a board is selected
      game.setBoard(board);
    });

    GUIApp.getInstance().addEventListener(AppEvent.BOARD_CHOSEN, board -> {
      // Board is already set in the game model from BOARD_SELECTED event
      // Just proceed to the next screen
      // TODO: players should be fetched from local storage
      List<Player> players = List.of(
              new Player("Atas"),
              new Player("Stian")
      );
      GUIApp.setContent(new ChoosePlayerScreen(players, game.getAllPlayingPieces(), game.getMaxPlayers()), true, true);
    });

    GUIApp.getInstance().addEventListener(AppEvent.PLAYERS_CHOSEN, players -> {
      PlayerConfigResponse response = game.isPlayerConfigOk(players);
      if (!response.isPlayerConfigOk()) {
        GUIApp.getInstance().showMessage(response.getErrorMessage());
        return;
      }
      // update model
      game.setPlayers(players);

      // start game
      // TODO: maybe use a factory pattern to create the game screen
      game.start();
      switch (game.getGameType()) {
        case SNAKES_AND_LADDERS:
          gameplayScreen = new SnakesAndLaddersScreen(game.getBoard());
          break;
        case MONOPOLY:
          gameplayScreen = new MonopolyScreen(
                  game.getBoard()
          );
          break;
        default:
          throw new IllegalArgumentException("Invalid game type: " + game.getGameType());
      }
      GUIApp.setContent(gameplayScreen, true, false);
      updateGameScreen();
    });

    GUIApp.getInstance().addEventListener(AppEvent.IN_GAME_EVENT, event -> {
      game.handleEvent(event);
      updateGameScreen();
      if (game.isGameOver()) {
        GUIApp.setContent(new GameOverScreen(game.getWinner().getName()), false, false);
      }
    });

    GUIApp.getInstance().addEventListener(AppEvent.PLAY_AGAIN, event -> {
      GUIApp.setContent(new HomeScreen(), false, false);
    });

    // File handling event listeners
    GUIApp.getInstance().addEventListener(AppEvent.SAVE_BOARD, filePath -> {
      try {
        // Ensure the board exists
        if (game == null || game.getBoard() == null) {
          GUIApp.getInstance().showMessage("No board to save");
          return;
        }

        // Use the file handler service to save the board
        fileHandlerService.saveBoard(game.getBoard(), filePath);

        GUIApp.getInstance().showMessage("Board saved successfully to " + filePath);
      } catch (IOException e) {
        GUIApp.getInstance().showMessage("Error saving board: " + e.getMessage());
      }
    });

    GUIApp.getInstance().addEventListener(AppEvent.LOAD_BOARD, filePath -> {
      try {
        // Use the file handler service to load the board
        Board board = fileHandlerService.loadBoard(filePath);
        
        // Set a fixed name for the loaded board
        board.setName("Loaded board");

        // Update the game with the loaded board
        if (game != null) {
          game.setBoard(board);
          
          // Get the current screen
          if (GUIApp.getCurrentContent() instanceof ChooseBoardScreen) {
            ChooseBoardScreen chooseBoardScreen = (ChooseBoardScreen) GUIApp.getCurrentContent();
            chooseBoardScreen.addLoadedBoard(board);
          }
          
          GUIApp.getInstance().showMessage("Board loaded successfully from " + filePath);
        } else {
          GUIApp.getInstance().showMessage("No game selected to load the board into");
        }
      } catch (IOException e) {
        GUIApp.getInstance().showMessage("Error loading board: " + e.getMessage());
      }
    });

    GUIApp.getInstance().addEventListener(AppEvent.SAVE_PLAYERS, filePath -> {
      try {
        // Ensure players exist
        if (game == null || game.getPlayers() == null || game.getPlayers().isEmpty()) {
          GUIApp.getInstance().showMessage("No players to save");
          return;
        }

        // Use the file handler service to save the players
        fileHandlerService.savePlayers(game.getPlayers(), filePath);

        GUIApp.getInstance().showMessage("Players saved successfully to " + filePath);
      } catch (IOException e) {
        GUIApp.getInstance().showMessage("Error saving players: " + e.getMessage());
      }
    });

    GUIApp.getInstance().addEventListener(AppEvent.LOAD_PLAYERS, filePath -> {
      try {
        // Use the file handler service to load the players
        List<Player> players = fileHandlerService.loadPlayers(filePath);

        // Update the game with the loaded players or update the player selection screen
        if (game != null) {
          // Check if the player configuration is valid
          PlayerConfigResponse response = game.isPlayerConfigOk(players);
          if (response.isPlayerConfigOk()) {
            game.setPlayers(players);
            GUIApp.getInstance().showMessage("Players loaded successfully from " + filePath);
          } else {
            GUIApp.getInstance().showMessage("Invalid player configuration: " + response.getErrorMessage());
          }
        } else {
          GUIApp.getInstance().showMessage("No game selected to load the players into");
        }
      } catch (IOException e) {
        GUIApp.getInstance().showMessage("Error loading players: " + e.getMessage());
      }
    });
  }

  private void updateGameScreen() {
    // TODO: may need different controller for each game screen
    if (gameplayScreen instanceof SnakesAndLaddersScreen) {
      ((SnakesAndLaddersScreen) gameplayScreen).update(
              game.getPlayers(),
              game.getCurrentPlayer(),
              game.getDiceCounts()
      );
    } else if (gameplayScreen instanceof MonopolyScreen) {
      List<Integer> playerMoney = new ArrayList<>();

      // Get player money if this is a MonopolyGame
      if (game instanceof MonopolyGame) {
        MonopolyGame monopolyGame = (MonopolyGame) game;
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

  public void run() {
    GUIApp.getInstance().startApp();
  }
}
