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
  AppEvent<Void> DICE_ROLLED = new AppEvent<>() {
  };
}
