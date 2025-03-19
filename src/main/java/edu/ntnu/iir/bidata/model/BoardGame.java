package edu.ntnu.iir.bidata.model;

import java.util.List;

/**
 * Manages the main game logic.
 */
public class BoardGame {
    private final Board board;
    private final List<Player> players;
    private final Dice dice;
    private boolean isGameOver;

    /**
     * Initializes the board game with a given board, players, and dice.
     *
     * @param board   the game board
     * @param players the list of players
     * @param dice    the dice used in the game
     */
    public BoardGame(Board board, List<Player> players, Dice dice) {
        this.board = board;
        this.players = players;
        this.dice = dice;
        this.isGameOver = false;
    }

    /**
     * Starts the game and prints the results to the console.
     */
    public void startGame() {
        int round = 1;
        while (!isGameOver) {
            System.out.println("\nRunde " + round); // Fjerner eventuelle spesialtegn

            for (Player player : players) {
                int roll = dice.rollAll();

                // Skriv ut kastet på terningen
                System.out.println(player.getName() + " kastet " + roll + " på terningene.");

                // Beveg spilleren
                player.move(roll);

                // Skriv ut spillerens nye posisjon
                System.out.println(player.getName() + " flyttet til rute " + player.getPosition() + ".");

                // Sjekk om spilleren har vunnet
                if (player.getPosition() >= board.getSize() - 1) {
                    System.out.println("\n" + player.getName() + " har nådd siste rute og vant spillet!");
                    isGameOver = true;
                    return; // Avslutt spillet umiddelbart etter en vinner er funnet
                }
            }
            round++;
        }
    }
}
