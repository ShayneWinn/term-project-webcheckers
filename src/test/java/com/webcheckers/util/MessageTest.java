package com.webcheckers.util;

import com.webcheckers.model.Row;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

/**
 * The unit test suite for the {@link Message} component.
 * @author <a href='mailto:vm2961@rit.edu'>Vishnu Muthuswamy</a>
 */
@Tag("Util-tier")
public class MessageTest {

    /**
     * Test that isSuccessful method works properly.
     */
    @Test
    public void testIsSuccessful() {
        Message testMessage1 = Message.info("test");

        Assertions.assertTrue(testMessage1.isSuccessful());

        Message testMessage2 = Message.error("test");

        Assertions.assertFalse(testMessage2.isSuccessful());
    }

    /**
     * Test that equals method works properly.
     */
    @Test
    public void testEquals() {
        Message testMessage1 = Message.info("test");

        Assertions.assertFalse(testMessage1.equals(null));

        Row testRow = mock(Row.class);

        Assertions.assertFalse(testMessage1.equals(testRow));

        Message testMessage2 = Message.info("test");

        Assertions.assertTrue(testMessage1.equals(testMessage2));

        Message testMessage3 = Message.info("test message");

        Assertions.assertFalse(testMessage1.equals(testMessage3));

        Message testMessage4 = Message.error("test");

        Assertions.assertFalse(testMessage1.equals(testMessage4));

        Message testMessage5 = Message.error("test message");

        Assertions.assertFalse(testMessage1.equals(testMessage5));
    }

}
