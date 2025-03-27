package edu.ntnu.iir.bidata.controller;

import edu.ntnu.iir.bidata.model.*;
import edu.ntnu.iir.bidata.view.GUI.ChoosePlayerScreen;
import edu.ntnu.iir.bidata.view.GUI.GUIApp;
import edu.ntnu.iir.bidata.view.GameEvent;
import edu.ntnu.iir.bidata.view.UIApp;

import java.util.Arrays;
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

    GUIApp.getInstance().addEventListener(GameEvent.GAME_CHOSEN, gameName -> {
      System.out.println("Game chosen: " + gameName);
      game.setGameType(gameName);
      // navigate to next screen
      List<Player> players = game.getPlayers();
      List<PlayingPiece> allPlayingPieces = game.getAllPlayingPieces();
      GUIApp.setContent(new ChoosePlayerScreen(players, allPlayingPieces));
    });

    // Create players
    List<Player> players = List.of(
            new Player("Atas"),
            new Player("Stian"),
            new Player("aParrot"),
            new Player("Myrra")
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
