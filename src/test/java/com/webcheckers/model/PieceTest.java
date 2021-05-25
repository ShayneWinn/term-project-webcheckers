package com.webcheckers.model;

import org.junit.jupiter.api.*;

/**
 * Testing the creation of a piece
 *
 * @author <a href='mailto:amy5049@rit.edu'>Alexander Yu</a>
 */

@Tag("Model-tier")
public class PieceTest {

    private static Piece testPiece;
    private static final Piece controlPiece = new Piece(Piece.Type.KING, Piece.PieceColor.RED);


    /**
     * Test the functionality of creating a piece
     */
    @Test
    public void testCreatePiece() {
        testPiece = new Piece(Piece.Type.SINGLE, Piece.PieceColor.WHITE);
        Assertions.assertEquals(Piece.Type.SINGLE, testPiece.getType());
        Assertions.assertEquals(Piece.PieceColor.WHITE,testPiece.getColor());
    }

    /**
     * Test the functionality of getting a piece's color
     */
    @Test
    public void testGetColor() {
        Assertions.assertEquals(Piece.PieceColor.RED,controlPiece.getColor());
    }

    /**
     * Test the functionality of getting a piece's type
     */
    @Test
    public void testGetType() {
        Assertions.assertEquals(Piece.Type.KING,controlPiece.getType());
    }


}
