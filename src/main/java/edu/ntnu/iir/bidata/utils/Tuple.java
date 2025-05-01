package edu.ntnu.iir.bidata.utils;

import java.util.Objects;

/**
 * A simple container class that holds two objects of different types.
 *
 * @param <A> Type of the first element
 * @param <B> Type of the second element
 */
public record Tuple<A, B>(A first, B second) {
  /**
   * Constructs a new tuple with the given elements.
   *
   * @param first  The first element
   * @param second The second element
   */
  public Tuple {
  }

  /**
   * Returns the first element of this tuple.
   *
   * @return the first element
   */
  @Override
  public A first() {
    return first;
  }

  /**
   * Returns the second element of this tuple.
   *
   * @return the second element
   */
  @Override
  public B second() {
    return second;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    Tuple<?, ?> tuple = (Tuple<?, ?>) obj;

    if (!Objects.equals(first, tuple.first)) {
      return false;
    }
    return Objects.equals(second, tuple.second);
  }

  @Override
  public String toString() {
    return "(" + first + ", " + second + ")";
  }
} 