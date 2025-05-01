package edu.ntnu.iir.bidata.utils;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

public class RandomMath {
  public static int getRandomNumber(int min, int max) {
    return (int) ((Math.random() * (max - min)) + min);
  }

  /**
   * (Help from AI)
   * Picks a random element from a list.
   *
   * @param list The list to pick from.
   * @return A random element from the list.
   */
  public static <T> Optional<T> randomPick(List<T> list) {
    if (list.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(list.get(getRandomNumber(0, list.size())));
  }
}
