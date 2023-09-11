package com.levi9.socialnetwork.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class EventEntityTest {

    private EventEntity event1;
    private EventEntity event2;

    @BeforeEach
    void setUp() {
        event1 = new EventEntity();
        event2 = new EventEntity();
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(event1);
        assertNull(event1.getId());
        assertNull(event1.getDateTime());
        assertNull(event1.getLocation());
        assertNull(event1.getGroup());
        assertNull(event1.getCreator());
    }

    @Test
    void testSetDateTimeValid() {
        LocalDateTime futureDateTime = LocalDateTime.now().plusDays(1);
        event1.setDateTime(futureDateTime);
        assertEquals(futureDateTime, event1.getDateTime());
    }

    @Test
    void testSetDateTimeInPast() {
        LocalDateTime pastDateTime = LocalDateTime.now().minusDays(1);
        assertThrows(IllegalArgumentException.class, () -> event1.setDateTime(pastDateTime));
    }

    @Test
    void testEqualsWithSameInstance() {
        assertTrue(event1.equals(event1));
    }



    @Test
    void testEqualsWithDifferentIds() {
        event1.setId(1L);
        event2.setId(2L);
        assertFalse(event1.equals(event2));
    }

    @Test
    void testEqualsWithNullObject() {
        assertFalse(event1.equals(null));
    }

    @Test
    void testToString() {
        event1.setId(1L);
        event1.setDateTime(LocalDateTime.of(2023, 9, 12, 14, 30));
        event1.setLocation("Meeting Room");
        event1.setGroup(new GroupEntity());
        event1.setCreator(new GroupMemberEntity());

        String expected = "EventEntity{id=1, dateTime=2023-09-12T14:30, location='Meeting Room', group=null}";
        assertEquals(expected, event1.toString());
    }
}
