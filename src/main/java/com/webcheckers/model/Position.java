package com.webcheckers.model;

/**
 * Position class which holds a set of coordinates on the game board
 *
 * @author <a href='mailto:gjv9138@rit.edu'>Geran Vaughn</a>
 */
public class Position {

    /**
     * Attributes
     */
    private final int row;
    private final int cell;

    /**
     * Creates the {@link Position} object pertaining to a row and cell on the game board
     *
     * @param row
     *      An int that refers to one of the eight rows on the board
     * @param cell
     *      An int that refers to one of the eight cells in a row
     */
    public Position(int row, int cell) {
        this.row = row;
        this.cell = cell;
    }

    /**
     * Returns the row index
     *
     * @return int
     */
    public int getRow() {
        return row;
    }

    /**
     * Returns the cell index
     *
     * @return int
     */
    public int getCell() {
        return cell;
    }

}
