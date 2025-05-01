package edu.ntnu.iir.bidata.view;

/**
 * Interface defining the core UI application functionality.
 * Provides methods for application lifecycle management and event handling.
 */
public interface UiApp {
  /**
   * Initializes and starts the application.
   */
  void startApp();

  /**
   * Terminates the application gracefully.
   */
  void quitApp();

  /**
   * Registers an event listener for the specified event type.
   * (Help from AI: We got some help from AI with generics.
   * I, aParrot, know typescript, but not java.)
   *
   * @param <T>      the type of data associated with the event
   * @param event    the event to register the listener for
   * @param listener the listener to be notified when the event occurs
   */
  <T> void addEventListener(AppEvent<T> event, GameEventListener<T> listener);

  /**
   * Unregisters an event listener for the specified event type.
   * (Help from AI: We got some help from AI with generics.
   * I, aParrot, know typescript, but not java.)
   *
   * @param <T>      the type of data associated with the event
   * @param event    the event to unregister the listener from
   * @param listener the listener to be removed
   */
  <T> void removeEventListener(AppEvent<T> event, GameEventListener<T> listener);

  /**
   * Triggers an event with associated data, notifying all registered listeners.
   * (Help from AI: We got some help from AI with generics.
   * I, aParrot, know typescript, but not java.)
   *
   * @param <T>   the type of data associated with the event
   * @param event the event to be triggered
   * @param data  the data to be passed to listeners
   */
  <T> void emitEvent(AppEvent<T> event, T data);

  /**
   * Triggers an event without data, notifying all registered listeners.
   * Convenience method for events that don't require data.
   * (Help from AI: We got some help from AI with generics.
   * I, aParrot, know typescript, but not java.)
   *
   * @param event the event to be triggered
   */
  default void emitEvent(AppEvent<Void> event) {
    emitEvent(event, null);
  }

  /**
   * Displays a message to the user through the UI.
   *
   * @param message the message to be displayed
   */
  void showMessage(String message);
}
