package com.webcheckers.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Testing proper creation of a Position object and its methods
 * @author <a href='mailto:gjv9138@rit.edu'>Geran Vaughn</a>
 */
@Tag("Model-tier")
public class PositionTest {

    private static final int ROW = 0;
    private static final int CELL = 0;

    /**
     * Tests the creation of a Position object
     */
    @Test
    public void testPositionCreation() {

        //Creates a test position object
        Position testPosition = new Position(ROW, CELL);

        //Tests proper creation of the Position object
        Assertions.assertNotNull(testPosition);
    }
    /**
     * Tests the return of the row
     */
    @Test
    public void testGetRow(){

        Position testPosition = new Position(ROW, CELL);

        //Testing the return of the row attribute
        Assertions.assertEquals(ROW, testPosition.getRow());
    }

    /**
     * Tests the return of the cell
     */
    @Test
    public void testGetEnd() {

        Position testPosition = new Position(ROW, CELL);

        //Testing the return of the cell attribute
        Assertions.assertEquals(CELL, testPosition.getCell());
    }

}
