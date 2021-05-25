package com.webcheckers.model;

/**
 * Move class which holds a starting and end position of a piece
 *
 * @author <a href='mailto:gjv9138@rit.edu'>Geran Vaughn</a>
 */
public class Move {

    /**
     * Attributes
     */
    private final Position start;
    private final Position end;

    /**
     * Creates the {@link Move} object that holds {@link Position} objects
     *
     * @param start
     *      Holds the starting position of the move
     * @param end
     *      Holds the ending position of the move
     */
    public Move(Position start, Position end) {
        this.start = start;
        this.end = end;
    }

    /**
     * Retrieves the starting position of the move
     *
     * @return {@link Position}
     */
    public Position getStart() {
        return start;
    }

    /**
     * Retrieves the ending position of the move
     *
     * @return {@link Position}
     */
    public Position getEnd() {
        return end;
    }

}
