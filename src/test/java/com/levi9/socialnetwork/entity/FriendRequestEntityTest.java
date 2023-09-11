package com.levi9.socialnetwork.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FriendRequestEntityTest {

    private FriendRequestEntity friendRequest;

    @BeforeEach
    void setUp() {
        friendRequest = new FriendRequestEntity();
    }

    @Test
    void testSetRequestCounterValid() {
        friendRequest.setRequestCounter(2);
        assertEquals(2, friendRequest.getRequestCounter());
    }

    @Test
    void testSetRequestCounterLowerMargin() {
        friendRequest.setRequestCounter(0);
        assertEquals(0, friendRequest.getRequestCounter());
    }

    @Test
    void testSetRequestCounterUpperMargin() {
        friendRequest.setRequestCounter(3);
        assertEquals(3, friendRequest.getRequestCounter());
    }

    @Test
    void testSetRequestCounterBelowLowerMargin() {
        assertThrows(IllegalArgumentException.class, () -> friendRequest.setRequestCounter(-1));
    }

    @Test
    void testSetRequestCounterAboveUpperMargin() {
        assertThrows(IllegalArgumentException.class, () -> friendRequest.setRequestCounter(4));
    }
    @Test
    void testDefaultConstructor() {
        assertNotNull(friendRequest);
        assertNull(friendRequest.getId());
        assertNull(friendRequest.getSender());
        assertNull(friendRequest.getReceiver());
        assertNull(friendRequest.getStatus());
        assertNull(friendRequest.getRequestCounter());
    }

    @Test
    void testEqualsWithSameInstance() {
        assertEquals(friendRequest, friendRequest);
    }

    @Test
    void testEqualsWithDifferentIds() {
        FriendRequestEntity friendRequest1 = new FriendRequestEntity();
        friendRequest.setId(1L);
        friendRequest1.setId(2L);
        assertNotEquals(friendRequest, friendRequest1);
    }

    @Test
    void testEqualsWithNullObject() {
        assertNotEquals(null, friendRequest);
    }

    @Test
    void testToString() {
        friendRequest.setId(1L);
        friendRequest.setRequestCounter(2);
        friendRequest.setStatus(RequestStatusEntity.PENDING);

        String expected = "FriendRequestEntity(id=1, sender=null, receiver=null, status=PENDING, requestCounter=2)";
        assertEquals(expected, friendRequest.toString());
    }
}
