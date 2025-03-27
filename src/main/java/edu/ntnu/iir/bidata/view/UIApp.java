package edu.ntnu.iir.bidata.view;

public interface UIApp {
  void startApp();

  void quitApp();

  /**
   * (Help from AI: help with generics)
   *
   * @param event
   * @param listener
   * @param <T>
   */
  <T> void addEventListener(GameEvent<T> event, GameEventListener<T> listener);

  /**
   * (Help from AI: help with generics)
   *
   * @param event
   * @param listener
   * @param <T>
   */
  <T> void removeEventListener(GameEvent<T> event, GameEventListener<T> listener);

  /**
   * (Help from AI: help with generics)
   *
   * @param event
   * @param data
   * @param <T>
   */
  <T> void emitEvent(GameEvent<T> event, T data);


  /**
   * For events that don't need data (like QUIT)
   * (Help from AI: help with generics)
   *
   * @param event
   */
  default void emitEvent(GameEvent<Void> event) {
    emitEvent(event, null);
  }

  void showMessage(String message);
}
