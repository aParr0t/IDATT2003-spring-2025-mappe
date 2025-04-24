package edu.ntnu.iir.bidata.controller;

import edu.ntnu.iir.bidata.model.*;
import edu.ntnu.iir.bidata.view.gui.screens.ChooseBoardScreen;
import edu.ntnu.iir.bidata.view.gui.screens.ChoosePlayerScreen;
import edu.ntnu.iir.bidata.view.gui.GUIApp;
import edu.ntnu.iir.bidata.view.gui.screens.GameplayScreen;
import edu.ntnu.iir.bidata.view.AppEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Runs the game in a text-based version.
 */
public class BoardGameController {
  private BoardGame game;
  private GameplayScreen currentGameScreen;
  private Map<String, Integer> playerPreviousPositions = new HashMap<>();

  public void setup() {
    game = new BoardGame();

    GUIApp.getInstance().addEventListener(AppEvent.QUIT, event -> {
      System.out.println("quitted");
    });

    GUIApp.getInstance().addEventListener(AppEvent.GAME_CHOSEN, gameType -> {
      // update model
      game.setGameType(gameType);
      // update view
      GameType retrievedGameType = game.getGameType();
      List<Board> boards = game.getAllBoardsForGameType(gameType);
      GUIApp.setContent(new ChooseBoardScreen(retrievedGameType, boards));
    });

    GUIApp.getInstance().addEventListener(AppEvent.BOARD_CHOSEN, board -> {
      // update model
      game.setBoard(board);
      // update view
      List<Player> players = game.getPlayers();
      GUIApp.setContent(new ChoosePlayerScreen(players, game.getAllPlayingPieces()));
    });

    GUIApp.getInstance().addEventListener(AppEvent.PLAYERS_CHOSEN, players -> {
      PlayerConfigResponse response = game.isPlayerConfigOk(players);
      if (!response.isPlayerConfigOk()) {
        GUIApp.getInstance().showMessage(response.getErrorMessage());
        return;
      }
      // update model
      game.setPlayers(players);
      game.startGame();

      // Store initial positions
      updatePreviousPositions();

      // update view
      goToAndUpdateGameScreen();
    });

    GUIApp.getInstance().addEventListener(AppEvent.DICE_ROLLED, diceRolls -> {
      // Store previous positions before making the turn
      updatePreviousPositions();

      // Make the turn in the game model
      game.makeTurn();

      // Update the game screen with the new state and previous positions
      // The animation will be handled by the GameplayScreen
      goToAndUpdateGameScreen();
    });

    // Create players
    List<Player> players = List.of(
            new Player("Atas"),
            new Player("Stian")
    );
    game.setPlayers(players);
  }

  private void updatePreviousPositions() {
    // Make a copy of the current positions before they change
    playerPreviousPositions.clear();
    for (Player player : game.getPlayers()) {
      playerPreviousPositions.put(player.getName(), player.getPosition());
    }
  }

  private void goToAndUpdateGameScreen() {
    // Create a new screen with the current game state and previous positions
    Map<String, Integer> positionsCopy = new HashMap<>(playerPreviousPositions);

    if (currentGameScreen == null) {
      // Create a new screen if it doesn't exist yet
      currentGameScreen = new GameplayScreen(
              game.getGameType(),
              game.getPlayers(),
              game.getBoard(),
              game.getDiceCounts(),
              game.getCurrentPlayerTurn(),
              positionsCopy // Pass previous positions to the screen
      );
      GUIApp.setContent(currentGameScreen);
    } else {
      // Update the existing screen
//      currentGameScreen.updateGameState(
//              game.getPlayers(),
//              game.getDiceCounts(),
//              game.getCurrentPlayerTurn(),
//              positionsCopy
//      );
    }
  }

  public void run() {
    GUIApp.getInstance().startApp();
  }
}
