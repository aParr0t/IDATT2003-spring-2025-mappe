package edu.ntnu.iir.bidata.utils;

import java.util.List;
import java.util.Optional;

/**
 * Utility class providing various random number generation and random selection functions.
 */
public class RandomMath {
  /**
   * Generates a random integer between the specified minimum and maximum values (inclusive of min,
   * exclusive of max).
   *
   * @param min The minimum value (inclusive)
   * @param max The maximum value (exclusive)
   * @return A random integer between min (inclusive) and max (exclusive)
   */
  public static int getRandomNumber(int min, int max) {
    return (int) ((Math.random() * (max - min)) + min);
  }

  /**
   * Picks a random element from a list.
   *
   * @param <T>  The type of elements in the list
   * @param list The list to pick from
   * @return An Optional containing a random element from the list, or empty if the list is empty
   */
  public static <T> Optional<T> randomPick(List<T> list) {
    if (list.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(list.get(getRandomNumber(0, list.size())));
  }
}
