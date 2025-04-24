package edu.ntnu.iir.bidata.controller;

import edu.ntnu.iir.bidata.model.*;
import edu.ntnu.iir.bidata.model.games.Game;
import edu.ntnu.iir.bidata.model.games.MonopolyGame;
import edu.ntnu.iir.bidata.model.games.SnakesAndLaddersGame;
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
  private Game game;
  private GameplayScreen currentGameScreen;
  private Map<String, Integer> playerPreviousPositions = new HashMap<>();

  public void setup() {
    GUIApp.getInstance().addEventListener(AppEvent.QUIT, event -> {
      System.out.println("quitted");
    });

    GUIApp.getInstance().addEventListener(AppEvent.GAME_CHOSEN, gameType -> {
      // TODO: maybe use a factory pattern to create the game
      switch (gameType) {
        case SNAKES_AND_LADDERS -> game = new SnakesAndLaddersGame();
        case MONOPOLY -> game = new MonopolyGame();
        default -> throw new IllegalArgumentException("Invalid game type: " + gameType);
      }
      // update view
      GameType retrievedGameType = game.getGameType();
      List<Board> boards = BoardFactory.getAllBoardsForGameType(gameType);
      GUIApp.setContent(new ChooseBoardScreen(retrievedGameType, boards), true, true);
    });

    GUIApp.getInstance().addEventListener(AppEvent.BOARD_CHOSEN, board -> {
      // update model
      game.setBoard(board);
      // update view

      // TODO: players should be fetched from local storage
      List<Player> players = List.of(
              new Player("Atas"),
              new Player("Stian")
      );
      GUIApp.setContent(new ChoosePlayerScreen(players, game.getAllPlayingPieces()), true, true);
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
      game.start();

      // update view
      goToAndUpdateGameScreen();
    });

    GUIApp.getInstance().addEventListener(AppEvent.IN_GAME_EVENT, event -> {
      System.out.println("IN_GAME_EVENT");
      game.handleEvent(event);
      goToAndUpdateGameScreen();
    });


    GUIApp.getInstance().addEventListener(AppEvent.DICE_ROLLED, diceRolls -> {
    });
  }

  private void goToAndUpdateGameScreen() {
    // Create a new screen with the current game state and previous positions
    Map<String, Integer> positionsCopy = new HashMap<>(playerPreviousPositions);

    currentGameScreen = new GameplayScreen(
            game.getGameType(),
            game.getPlayers(),
            game.getBoard(),
            game.getDiceCounts(),
            game.getCurrentPlayer(),
            positionsCopy // Pass previous positions to the screen
    );
    GUIApp.setContent(currentGameScreen, true, false);
  }

  public void run() {
    GUIApp.getInstance().startApp();
  }
}
