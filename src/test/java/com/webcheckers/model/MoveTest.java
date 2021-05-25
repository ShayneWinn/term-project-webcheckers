package com.webcheckers.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Testing proper creation of a Move object and its methods
 * 
 * @author <a href='mailto:gjv9138@rit.edu'>Geran Vaughn</a>
 */
@Tag("Model-tier")
public class MoveTest {

    private static final Position START_POSITION = new Position(0, 0);
    private static final Position END_POSITION = new Position(1, 1);

    /**
     * Tests the creation of a Move object
     */
    @Test
    public void testMoveCreation() {

        // Create a test Move object
        Move testMove = new Move(START_POSITION, END_POSITION);

        // Test proper creation of the Move object
        Assertions.assertNotNull(testMove);
    }
    /**
     * Tests the return of the startPosition
     */
    @Test
    public void testGetStart() {

        Move testMove = new Move(START_POSITION, END_POSITION);

        // Testing the return of the startPosition attribute
        Assertions.assertEquals(START_POSITION, testMove.getStart());
    }

    /**
     * Tests the return of the endPosition
     */
    @Test
    public void testGetEnd() {

        Move testMove = new Move(START_POSITION, END_POSITION);

        // Testing the return of the endPosition attribute
        Assertions.assertEquals(END_POSITION, testMove.getEnd());
    }
}
