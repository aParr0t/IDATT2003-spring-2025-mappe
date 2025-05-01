package edu.ntnu.iir.bidata.view;

/**
 * A functional interface for handling game events.
 *
 * <p>This interface allows components to respond to game events by implementing
 * a single method that receives data related to the event.
 *
 * @param <T> the type of data this event listener handles
 */
@FunctionalInterface
public interface GameEventListener<T> {
  /**
   * Called when a game event occurs.
   *
   * @param data the event data to process
   */
  void onEvent(T data);
}
