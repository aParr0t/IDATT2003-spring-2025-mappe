package edu.ntnu.iir.bidata.controller;

import edu.ntnu.iir.bidata.model.*;
import edu.ntnu.iir.bidata.model.games.Game;
import edu.ntnu.iir.bidata.model.games.MonopolyGame;
import edu.ntnu.iir.bidata.model.games.SnakesAndLaddersGame;
import edu.ntnu.iir.bidata.view.gui.screens.*;
import edu.ntnu.iir.bidata.view.gui.GUIApp;
import edu.ntnu.iir.bidata.view.AppEvent;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Runs the game in a text-based version.
 */
public class BoardGameController {
  private Game game;
  private StackPane gameplayScreen;

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
    });


    GUIApp.getInstance().addEventListener(AppEvent.DICE_ROLLED, diceRolls -> {
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
