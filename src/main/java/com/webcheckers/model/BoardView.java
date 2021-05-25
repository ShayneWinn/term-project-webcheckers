package com.webcheckers.model;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * BoardView, builds the initial state of the checkers board
 *
 * @author <a href='mailto:vm2961@rit.edu'>Vishnu Muthuswamy</a>
 */
public class BoardView implements Iterable<Row> {

    /**
     * Attributes
     */
    private final ArrayList<Row> rows;

    /**
     * Creates a {@link BoardView} object that holds the {@link Row} objects that make up the initial state of a checkers game
     */
    public BoardView() {
        this.rows = new ArrayList<>();
    }

    /**
     * Return number of rows in board.
     *
     *  @return number of rows in board
     */
    public ArrayList<Row> getRows() {
        return rows;
    }

    /**
     * Returns the iterator associated with the {@link Row} object
     *
     * @return {@link Iterator<Row>}
     */
    public Iterator<Row> iterator() {
        return rows.iterator();
    }
}
