package com.webcheckers.model;


import java.util.ArrayList;
import java.util.Iterator;

/**
 * Row, holds the eight Space objects that make up a row on a checker board
 *
 * @author <a href='mailto:vm2961@rit.edu'>Vishnu Muthuswamy</a>
 * @author <a href='mailto:gjv9138@rit.edu'>Geran Vaughn</a>
 */
public class Row implements Iterable<Space>{

    /**
     * Attributes
     */
    private final int index;
    private final ArrayList<Space> spaces;

    /**
     * Creates a {@link Row} object that holds the {@link Space} objects that make up the initial state of a checker game
     *
     * @param index
     *      The index for each row (0 - 7)
     */
    public Row(final int index) {
        this.index = index;
        this.spaces = new ArrayList<>();
    }

    /**
     * Returns the index of the {@link Row} object
     *
     * @return index of Row object
     */
    public int getIndex() {
        return index;
    }

    /**
     * Return number of spaces in board.
     *
     *  @return number of spaces in board
     */
    public ArrayList<Space> getSpaces() {
        return spaces;
    }

    /**
     * Returns the {@link Space} iterator associated with a given Space
     *
     * @return {@link Iterator<Space>}
     */
    public Iterator<Space> iterator() {
        return spaces.iterator();
    }

    /**
     * Compares two {@link Row} objects.
     *
     * @param obj  a {@link Row} object
     *
     * @return true if both objects are the same, and false, otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }

        Row row = (Row) obj;

        return this.getIndex() == row.getIndex() && this.getSpaces().equals(row.getSpaces());
    }

}
