package com.levi9.socialnetwork.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GroupRequestEntityTest {

    private GroupRequestEntity groupRequest1;
    private GroupRequestEntity groupRequest2;

    @BeforeEach
    void setUp() {
        groupRequest1 = new GroupRequestEntity();
        groupRequest2 = new GroupRequestEntity();
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(groupRequest1);
        assertNull(groupRequest1.getId());
        assertNull(groupRequest1.getUser());
        assertNull(groupRequest1.getGroup());
        assertNull(groupRequest1.getStatus());
        assertFalse(groupRequest1.isAdminSentRequest());
        assertNull(groupRequest1.getRequestCounter());
    }

    @Test
    void testSetRequestCounterValid() {
        groupRequest1.setRequestCounter(2);
        assertEquals(2, groupRequest1.getRequestCounter());
    }

    @Test
    void testSetRequestCounterLowerBound() {
        groupRequest1.setRequestCounter(0);
        assertEquals(0, groupRequest1.getRequestCounter());
    }

    @Test
    void testSetRequestCounterUpperBound() {
        groupRequest1.setRequestCounter(3);
        assertEquals(3, groupRequest1.getRequestCounter());
    }

    @Test
    void testSetRequestCounterBelowLowerBound() {
        assertThrows(IllegalArgumentException.class, () -> groupRequest1.setRequestCounter(-1));
    }

    @Test
    void testSetRequestCounterAboveUpperBound() {
        assertThrows(IllegalArgumentException.class, () -> groupRequest1.setRequestCounter(4));
    }

    @Test
    void testSetRequestCounterWithNull() {
        assertThrows(IllegalArgumentException.class, () -> groupRequest1.setRequestCounter(null));
    }

    @Test
    void testSetRequestCounterWithNegativeValue() {
        assertThrows(IllegalArgumentException.class, () -> groupRequest1.setRequestCounter(-2));
    }

    @Test
    void testSetRequestCounterWithGreaterThanUpperBoundValue() {
        assertThrows(IllegalArgumentException.class, () -> groupRequest1.setRequestCounter(5));
    }

    @Test
    void testEqualsWithSameInstance() {
        assertTrue(groupRequest1.equals(groupRequest1));
    }



    @Test
    void testEqualsWithDifferentIds() {
        groupRequest1.setId(1L);
        groupRequest2.setId(2L);
        assertFalse(groupRequest1.equals(groupRequest2));
    }

    @Test
    void testEqualsWithNullObject() {
        assertFalse(groupRequest1.equals(null));
    }

    @Test
    void testToString() {
        groupRequest1.setId(1L);
        groupRequest1.setRequestCounter(2);
        groupRequest1.setStatus(RequestStatusEntity.PENDING);
        groupRequest1.setAdminSentRequest(true);

        String expected = "GroupRequestEntity(id=1, user=null, group=null, status=PENDING, adminSentRequest=true, requestCounter=2)";
        assertEquals(expected, groupRequest1.toString());
    }
}
