package model.eventloggingtest;

import model.eventlogging.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the Event class
 */
public class EventTest {
    private Event e;
    private Date d;

    @BeforeEach
    public void runBefore() {
        e = new Event("Added word 'hola'");   // (1)
        d = Calendar.getInstance().getTime();   // (2)
    }

    @Test
    public void testEvent() {
        e = new Event("Added word 'hola'");   // (1)
        d = Calendar.getInstance().getTime();   // (2)
        assertEquals("Added word 'hola'", e.getDescription());
        assertEquals(d, e.getDate());
    }

    @Test
    public void testToString() {
        assertEquals(d.toString() + "\n" + "Added word 'hola'", e.toString());
    }
}
