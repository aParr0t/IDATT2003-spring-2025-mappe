package edu.ntnu.iir.bidata.controller;

import edu.ntnu.iir.bidata.model.Board;
import edu.ntnu.iir.bidata.model.BoardGame;
import edu.ntnu.iir.bidata.model.Dice;
import edu.ntnu.iir.bidata.model.Player;
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
      GUIApp.setContent(new ChoosePlayerScreen());
    });

    // Create players
    Player p1 = new Player("Atas");
    Player p2 = new Player("Stian");
    Player p3 = new Player("aParrot");
    Player p4 = new Player("Myrra");

    List<Player> players = Arrays.asList(p1, p2, p3, p4);

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
