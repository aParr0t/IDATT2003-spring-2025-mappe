package edu.ntnu.iir.bidata.utils;

/**
 * A simple container class that holds two objects of different types.
 *
 * @param <A> Type of the first element
 * @param <B> Type of the second element
 */
public class Tuple<A, B> {
    private final A first;
    private final B second;

    /**
     * Constructs a new tuple with the given elements.
     *
     * @param first The first element
     * @param second The second element
     */
    public Tuple(A first, B second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Returns the first element of this tuple.
     *
     * @return the first element
     */
    public A getFirst() {
        return first;
    }

    /**
     * Returns the second element of this tuple.
     *
     * @return the second element
     */
    public B getSecond() {
        return second;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Tuple<?, ?> tuple = (Tuple<?, ?>) obj;

        if (first != null ? !first.equals(tuple.first) : tuple.first != null) return false;
        return second != null ? second.equals(tuple.second) : tuple.second == null;
    }

    @Override
    public int hashCode() {
        int result = first != null ? first.hashCode() : 0;
        result = 31 * result + (second != null ? second.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ")";
    }
} 