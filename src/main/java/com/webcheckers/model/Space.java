package com.webcheckers.model;


/**
 * Space, modifies Piece objects that are associated with each space
 *
 * @author <a href='mailto:vm2961@rit.edu'>Vishnu Muthuswamy</a>
 * @author <a href='mailto:gjv9138@rit.edu'>Geran Vaughn</a>
 */
public class Space {

    /**
     * Constants
     */
    public enum State {
        EMPTY,
        NOT_EMPTY
    }

    public enum SpaceColor {
        WHITE,
        BLACK
    }

    /**
     * Attributes
     */
    private State state;
    private final SpaceColor color;
    private final int cellIdx;

    private Piece piece;

    /**
     * Creates the {@link Space} object that holds a {@link Piece} object
     *
     * @param state
     *      Holds whether a Space has a Piece associated with it
     * @param color
     *      The color of the Space object
     * @param cellIdx
     *      The index of the Space object in a Row Object
     * @param piece
     *      Holds a Piece object
     */
    public Space(final State state, final SpaceColor color, final int cellIdx, Piece piece) {
        this.state = state;
        this.color = color;
        this.cellIdx = cellIdx;
        this.piece = piece;
    }

    public Space(Space other) {
        this.state = other.state;
        this.color = other.color;
        this.cellIdx = other.cellIdx;

        if (other.piece == null) {
            this.piece = other.piece;
        }
        else {
            this.piece = new Piece(other.piece);
        }
    }

    /**
     * Returns state of the {@link Space} object
     *
     * @return State
     */
    public State getState() {
        return this.state;
    }

    /**
     * Returns color of the {@link Space} object
     *
     * @return SpaceColor
     */
    public SpaceColor getSpaceColor() {
        return this.color;
    }

    /**
     * Returns index of the {@link Space} object
     *
     * @return int
     */
    public int getCellIdx() {
        return cellIdx;
    }

    /**
     * Returns the {@link Piece} object held by the {@link Space} object
     *
     * @return Piece
     */
    public Piece getPiece() {
        return piece;
    }

    /**
     * Changes the state of a {@link Space} object between EMPTY and NOT_EMPTY
     *
     * @param state
     *      The state that the {@link Space} object will be changed to
     */
    public void changeState(State state) {
        this.state = state;
    }

    /**
     * Associates a {@link Piece} object with the {@link Space} object
     *
     * @param piece
     *      The Piece object that is being associated with the Space object
     */
    public void addPiece(Piece piece) {
        this.state = State.NOT_EMPTY;
        this.piece = piece;
    }

    /**
     * Removes the association of a {@link Piece} object with the {@link Space} object
     */
    public void removePiece() {
        this.state = State.EMPTY;
        this.piece = null;
    }

    /**
     * (WIP) Returns a boolean for whether a move was legal or not
     *
     * @return boolean
     */
    public boolean isValid() {
        if (state.equals(state.EMPTY) && color.equals(color.BLACK)) {
            return true;
        }
        return false;
    }

    /**
     * Compares two {@link Space} objects.
     *
     * @param obj  a {@link Space} object
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

        Space space = (Space) obj;

        return this.state == space.getState() && this.color == space.getSpaceColor() &&
                this.cellIdx == space.getCellIdx() && this.piece == space.getPiece();
    }

}
