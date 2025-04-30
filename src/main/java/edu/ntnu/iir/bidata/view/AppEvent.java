package edu.ntnu.iir.bidata.view;

import edu.ntnu.iir.bidata.model.Board;
import edu.ntnu.iir.bidata.model.GameType;
import edu.ntnu.iir.bidata.model.Player;

import java.nio.file.Path;
import java.util.List;

// Base interface for all game events

/**
 * Represents an event that can be emitted by the game.
 * (Help from AI: help with generics)
 *
 * @param <T> The type of data associated with the event.
 */
public interface AppEvent<T> {
  // Event types defined as singleton instances
  AppEvent<Void> QUIT = new AppEvent<>() {
  };
  AppEvent<Void> START = new AppEvent<>() {
  };
  AppEvent<GameType> GAME_CHOSEN = new AppEvent<>() {
  };
  AppEvent<Board> BOARD_CHOSEN = new AppEvent<>() {
  };
  AppEvent<List<Player>> PLAYERS_CHOSEN = new AppEvent<>() {
  };
  AppEvent<String> IN_GAME_EVENT = new AppEvent<>() {
  };
  AppEvent<Void> PLAY_AGAIN = new AppEvent<>() {
  };
  
  // File handling events
  AppEvent<Path> SAVE_BOARD = new AppEvent<>() {
  };
  AppEvent<Path> LOAD_BOARD = new AppEvent<>() {
  };
  AppEvent<Path> SAVE_PLAYERS = new AppEvent<>() {
  };
  AppEvent<Path> LOAD_PLAYERS = new AppEvent<>() {
  };
}
