package edu.ntnu.iir.bidata;

import java.util.Arrays;
import java.util.List;

/**
 * Runs the game in a text-based version.
 */
public class BoardGameApp {
    public static void main(String[] args) {
        // Create a game board with 90 tiles
        Board board = new Board(90);

        // Create players
        Player p1 = new Player("Atas");
        Player p2 = new Player("Stian");
        Player p3 = new Player("aParrot");
        Player p4 = new Player("Myrra");

        List<Player> players = Arrays.asList(p1, p2, p3, p4);

        // Create 2 dice with 6 sides each
        Dice dice = new Dice(2, 6);

        // Create the game
        BoardGame game = new BoardGame(board, players, dice);

        // Start the game in the console
        game.startGame();
    }
}
