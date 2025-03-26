package edu.ntnu.iir.bidata.view;

public interface UIApp {
  void startApp();

  void quitApp();

  void addEventListener(GameEvent event, GameEventListener listener);

  void removeEventListener(GameEvent event, GameEventListener listener);

  void emitEvent(GameEvent event);
}
