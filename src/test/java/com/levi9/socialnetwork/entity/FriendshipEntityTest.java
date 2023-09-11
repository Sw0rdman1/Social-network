package com.levi9.socialnetwork.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class FriendshipEntityTest {

    private FriendshipEntity friendship1;
    private FriendshipEntity friendship2;

    @BeforeEach
    void setUp() {
        friendship1 = new FriendshipEntity();
        friendship2 = new FriendshipEntity();
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(friendship1);
        assertNull(friendship1.getId());
        assertNull(friendship1.getUser1());
        assertNull(friendship1.getUser2());
        assertNull(friendship1.getDateConnected());
    }

    @Test
    void testSetDateConnectedValid() {
        LocalDateTime pastDateTime = LocalDateTime.now().minusDays(1);
        friendship1.setDateConnected(pastDateTime);
        assertEquals(pastDateTime, friendship1.getDateConnected());
    }

    @Test
    void testSetDateConnectedInFuture() {
        LocalDateTime futureDateTime = LocalDateTime.now().plusDays(1);
        assertThrows(IllegalArgumentException.class, () -> friendship1.setDateConnected(futureDateTime));
    }

    @Test
    void testEqualsWithSameInstance() {
        assertTrue(friendship1.equals(friendship1));
    }


    @Test
    void testEqualsWithDifferentIds() {
        friendship1.setId(1L);
        friendship2.setId(2L);
        assertFalse(friendship1.equals(friendship2));
    }

    @Test
    void testEqualsWithNullObject() {
        assertFalse(friendship1.equals(null));
    }

    @Test
    void testToString() {
        friendship1.setId(1L);
        friendship1.setDateConnected(LocalDateTime.of(2022, 9, 12, 14, 30));
        UserEntity user1 = new UserEntity();
        UserEntity user2 = new UserEntity();
        user1.setUsername("Bozidar");
        user2.setUsername("Tamara");

        friendship1.setUser1(user1);
        friendship1.setUser2(user2);

        String expected = "FriendshipEntity{id=1, user1=Bozidar, user2=Tamara, dateConnected=2022-09-12T14:30}";
        assertEquals(expected, friendship1.toString());
    }
}
