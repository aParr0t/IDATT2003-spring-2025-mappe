package edu.ntnu.iir.bidata.view;

import edu.ntnu.iir.bidata.model.Board;
import edu.ntnu.iir.bidata.model.GameType;
import edu.ntnu.iir.bidata.model.Player;

import java.util.List;

// Base interface for all game events

/**
 * Represents an event that can be emitted by the game.
 * (Help from AI: help with generics)
 *
 * @param <T> The type of data associated with the event.
 */
public interface GameEvent<T> {
  // Event types defined as singleton instances
  GameEvent<Void> QUIT = new GameEvent<>() {
  };
  GameEvent<Void> START = new GameEvent<>() {
  };
  GameEvent<GameType> GAME_CHOSEN = new GameEvent<>() {
  };
  GameEvent<Board> BOARD_CHOSEN = new GameEvent<>() {
  };
  GameEvent<List<Player>> PLAYERS_CHOSEN = new GameEvent<>() {
  };

  // Add other event types as needed
}
