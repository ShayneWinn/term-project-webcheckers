package com.webcheckers.model;

/**
 * Piece, holds the information associated with the Piece object
 *
 * @author <a href='mailto:vm2961@rit.edu'>Vishnu Muthuswamy</a>
 * @author <a href='mailto:gjv9138@rit.edu'>Geran Vaughn</a>
 */
public class Piece {

    /**
     * Constants
     */
    public enum Type {
        SINGLE,
        KING
    }

    public enum PieceColor {
        RED,
        WHITE
    }

    /**
     * Attributes
     */
    private Type type;
    private final PieceColor color;

    /**
     * Creates the {@link Piece} object
     *
     * @param type
     *      Holds whether piece is SINGLE or KING
     * @param color
     *      Holds the color of the piece
     */
    public Piece(final Type type, final PieceColor color) {
        this.type = type;
        this.color = color;
    }

    public Piece(Piece other) {
        this.type = other.type;
        this.color = other.color;
    }

    /**
     * Returns the type of the {@link Piece} object
     *
     * @return {@link Type} object
     */
    public Type getType() {
        return type;
    }

    /**
     * Returns the color of the {@link Piece} object
     *
     * @return {@link PieceColor} object
     */
    public PieceColor getColor() {
        return color;
    }

    /**
     * Changes {@link Piece.Type} element of a {@link Piece } to KING
     */
    public void modifyTypeToKING() {
        this.type = Type.KING;
    }
}
