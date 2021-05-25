package com.webcheckers.model;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The unit test suite for the {@link BoardView} component.
 *
 * @author <a href='mailto:vm2961@rit.edu'>Vishnu Muthuswamy</a>
 */
@Tag("Model-tier")
public class BoardViewTest {
    ArrayList<Row> rows = new ArrayList<>();
    Iterator<Row> expectedIterator = rows.iterator();

    /**
     * Test that the constructor works without failure.
     */
    @Test
    public void testConstructor() {
        new BoardView();
    }

    /**
     * Test that all the rows are empty in the board.
     */
    @Test
    public void testEmptyRows() {
        final BoardView CuT = new BoardView();
        assertEquals(rows, CuT.getRows());
    }

    /**
     * Test that all rows are empty in the iterator.
     */
    @Test
    public void testEmptyRowsIterator() {
        final BoardView CuT = new BoardView();
        final Iterator<Row> actualIterator = CuT.iterator();

        // Loop through the iterators they still have values
        while(expectedIterator.hasNext() && actualIterator.hasNext()) {
            Row expectedRow = expectedIterator.next();
            Row actualRow = actualIterator.next();
            assertEquals(expectedRow, actualRow);
        }
        if (expectedIterator.hasNext() || actualIterator.hasNext()) {
            // If one of the iterators has more elements than the other
            fail();
        }
    }
}
