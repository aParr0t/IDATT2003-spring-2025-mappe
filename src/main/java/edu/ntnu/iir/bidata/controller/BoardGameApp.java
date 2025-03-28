package edu.ntnu.iir.bidata.controller;

import edu.ntnu.iir.bidata.model.*;
import edu.ntnu.iir.bidata.view.GUI.ChooseBoardScreen;
import edu.ntnu.iir.bidata.view.GUI.ChoosePlayerScreen;
import edu.ntnu.iir.bidata.view.GUI.GUIApp;
import edu.ntnu.iir.bidata.view.GUI.GameplayScreen;
import edu.ntnu.iir.bidata.view.GameEvent;

import java.util.List;

/**
 * Runs the game in a text-based version.
 */
public class BoardGameApp {
  private BoardGame game;

  public void setup() {
    game = new BoardGame();

    GUIApp.getInstance().addEventListener(GameEvent.QUIT, event -> {
      System.out.println("quitted");
    });

    GUIApp.getInstance().addEventListener(GameEvent.GAME_CHOSEN, gameType -> {
      // update model
      game.setGameType(gameType);
      // update view
      GameType retrievedGameType = game.getGameType();
      List<Board> boards = game.getAllBoardsForGameType(gameType);
      GUIApp.setContent(new ChooseBoardScreen(retrievedGameType, boards));
    });

    GUIApp.getInstance().addEventListener(GameEvent.BOARD_CHOSEN, board -> {
      // update model
      game.setBoard(board);
      // update view
      List<Player> players = game.getPlayers();
      GUIApp.setContent(new ChoosePlayerScreen(players, game.getAllPlayingPieces()));
    });

    GUIApp.getInstance().addEventListener(GameEvent.PLAYERS_CHOSEN, players -> {
      PlayerConfigResponse response = game.isPlayerConfigOk(players);
      if (!response.isPlayerConfigOk()) {
        GUIApp.getInstance().showMessage(response.getErrorMessage());
        return;
      }
      // update model
      game.setPlayers(players);
      game.startGame();
      // update view
      GUIApp.setContent(new GameplayScreen());
    });

    // Create players
    List<Player> players = List.of(
            new Player("Atas"),
            new Player("Stian")
    );
    game.setPlayers(players);

    // Create 2 dice with 6 sides each
    Dice dice = new Dice(2, 6);

    // Create the game
//    game = new BoardGame(board, players, dice);
  }

  public void run() {
    // game.startGame();  // Start the game in the console
    GUIApp.getInstance().startApp();
  }
}
