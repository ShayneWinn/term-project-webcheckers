package com.webcheckers.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

/**
 * Testing proper creation of a Space object and its methods
 * @author <a href='mailto:gjv9138@rit.edu'>Geran Vaughn</a>
 * @author <a href='mailto:vm2961@rit.edu'>Vishnu Muthuswamy</a>
 */
@Tag("Model-tier")
public class SpaceTest {

    /**
     * Tests the creation of a Space object
     */
    @Test
    public void testSpaceCreation() {

        // Creates a test Space object
        Space testSpace = new Space(Space.State.EMPTY, Space.SpaceColor.BLACK, 0, null);

        // Checking proper creation of the Space
        Assertions.assertEquals(Space.State.EMPTY, testSpace.getState());
        Assertions.assertEquals(Space.SpaceColor.BLACK, testSpace.getSpaceColor());
        Assertions.assertEquals(0, testSpace.getCellIdx());
        Assertions.assertNull(testSpace.getPiece());

    }

    /**
     * Tests the modification of a Space object
     */
    @Test
    public void testSpaceModifications() {

        // Creates a test Space object
        Space testSpace = new Space(Space.State.EMPTY, Space.SpaceColor.BLACK, 0, null);

        // Testing changing the state of the Space
        testSpace.changeState(Space.State.NOT_EMPTY);
        Assertions.assertEquals(Space.State.NOT_EMPTY, testSpace.getState());

        // Testing adding a Piece
        testSpace.addPiece(new Piece(Piece.Type.SINGLE, Piece.PieceColor.RED));
        Assertions.assertEquals(Space.State.NOT_EMPTY, testSpace.getState());
        Assertions.assertNotNull(testSpace.getPiece());

        // Testing removing a Piece
        testSpace.removePiece();
        Assertions.assertEquals(Space.State.EMPTY, testSpace.getState());
        Assertions.assertNull(testSpace.getPiece());

    }

    /**
     * Tests move validation for a Space Object
     */
    @Test
    public void testMoveValidation() {

        // Creates a test Space object
        Space testSpace = new Space(Space.State.EMPTY, Space.SpaceColor.BLACK, 0, null);

        // Testing validity of a move
        Assertions.assertTrue(testSpace.isValid());

        testSpace = new Space(Space.State.EMPTY, Space.SpaceColor.WHITE, 0, null);
        Assertions.assertFalse(testSpace.isValid());

        testSpace = new Space(Space.State.NOT_EMPTY, Space.SpaceColor.BLACK, 0, null);
        Assertions.assertFalse(testSpace.isValid());
    }

    /**
     * Test that equals method works properly.
     */
    @Test
    public void testEquals() {
        Space testSpace1 = new Space(Space.State.EMPTY, Space.SpaceColor.BLACK, 0, null);

        Assertions.assertTrue(testSpace1.equals(testSpace1));
        Assertions.assertFalse(testSpace1.equals(null));

        Row testRow = mock(Row.class);

        Assertions.assertFalse(testSpace1.equals(testRow));

        Space testSpace2 = new Space(Space.State.EMPTY, Space.SpaceColor.BLACK, 0, null);

        Assertions.assertTrue(testSpace1.equals(testSpace2));

        Space testSpace3 = new Space(Space.State.EMPTY, Space.SpaceColor.WHITE, 0, null);

        Assertions.assertFalse(testSpace1.equals(testSpace3));

        Space testSpace4 = new Space(Space.State.NOT_EMPTY, Space.SpaceColor.BLACK, 0, null);

        Assertions.assertFalse(testSpace1.equals(testSpace4));

        Space testSpace5 = new Space(Space.State.EMPTY, Space.SpaceColor.BLACK, 1, null);

        Assertions.assertFalse(testSpace1.equals(testSpace5));
    }

}
