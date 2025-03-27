package edu.ntnu.iir.bidata.view;

@FunctionalInterface
public interface GameEventListener<T> {
    void onEvent(T data);
}
