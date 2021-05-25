package com.webcheckers.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;

import static org.mockito.Mockito.mock;

/**
 * The unit test suite for the {@link Row} component.
 *
 * @author <a href='mailto:vm2961@rit.edu'>Vishnu Muthuswamy</a>
 */
@Tag("Model-tier")
public class RowTest {

    ArrayList<Space> spaces = new ArrayList<>();
    Iterator<Space> expectedIterator = spaces.iterator();

    /**
     * Test that getIndex() returns correct value.
     */
    @Test
    public void testGetIndex() {
        final int idx = 4;
        final Row CuT = new Row(idx);

        Assertions.assertEquals(idx, CuT.getIndex());
    }

    /**
     * Test that all spaces are empty in the iterator.
     */
    @Test
    public void testEmptySpacesIterator() {
        final Row CuT = new Row(0);
        final Iterator<Space> actualIterator = CuT.iterator();

        // Loop through the iterators until the end(s) of one or both is/are reached
        while(expectedIterator.hasNext() && actualIterator.hasNext()) {
            Space expectedSpace = expectedIterator.next();
            Space actualSpace = actualIterator.next();
            Assertions.assertEquals(expectedSpace, actualSpace);
        }
        if (expectedIterator.hasNext() || actualIterator.hasNext()) {
            // If one of the iterators has more elements than the other
            Assertions.fail();
        }
    }

    /**
     * Test that equals method works properly.
     */
    @Test
    public void testEquals() {
        Row testRow1 = new Row(0);

        Assertions.assertTrue(testRow1.equals(testRow1));
        Assertions.assertFalse(testRow1.equals(null));

        Space testSpace = mock(Space.class);

        Assertions.assertFalse(testRow1.equals(testSpace));

        Row testRow2 = new Row(0);

        Assertions.assertTrue(testRow1.equals(testRow2));

        Row testRow3 = new Row(1);

        Assertions.assertFalse(testRow1.equals(testRow3));

        Row testRow4 = new Row(1);

        Space testRow4Space = new Space(Space.State.EMPTY, Space.SpaceColor.BLACK, 0, null);

        testRow4.getSpaces().add(testRow4Space);

        Assertions.assertFalse(testRow1.equals(testRow4));

        Row testRow5 = new Row(0);

        Space testRow5Space = new Space(Space.State.EMPTY, Space.SpaceColor.BLACK, 0, null);

        testRow5.getSpaces().add(testRow5Space);

        Assertions.assertFalse(testRow1.equals(testRow5));
    }

}
