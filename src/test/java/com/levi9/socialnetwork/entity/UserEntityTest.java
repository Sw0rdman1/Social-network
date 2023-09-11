package com.levi9.socialnetwork.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserEntityTest {

    private UserEntity user1;
    private UserEntity user2;

    @BeforeEach
    void setUp() {
        user1 = new UserEntity();
        user2 = new UserEntity();
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(user1);
        assertNull(user1.getId());
        assertNull(user1.getUsername());
        assertNull(user1.getEmail());
        assertNull(user1.getPassword());
        assertNull(user1.getActive());
    }

    @Test
    void testSetUsernameValid() {
        user1.setUsername("bozidar");
        assertEquals("bozidar", user1.getUsername());
    }

    @Test
    void testSetUsernameBelowMinLength() {
        assertThrows(IllegalArgumentException.class, () -> user1.setUsername("bd"));
    }

    @Test
    void testSetEmailValid() {
        user1.setEmail("bozidar@example.com");
        assertEquals("bozidar@example.com", user1.getEmail());
    }

    @Test
    void testSetEmailBelowMinLength() {
        assertThrows(IllegalArgumentException.class, () -> user1.setEmail("b@com"));
    }

    @Test
    void testSetEmailInvalidFormat() {
        assertThrows(IllegalArgumentException.class, () -> user1.setEmail("invalid-email"));
    }

    @Test
    void testSetPasswordValid() {
        user1.setPassword("strongPassword");
        assertEquals("strongPassword", user1.getPassword());
    }

    @Test
    void testSetPasswordBelowMinLength() {
        assertThrows(IllegalArgumentException.class, () -> user1.setPassword("short"));
    }

    @Test
    void testEqualsWithSameInstance() {
        assertTrue(user1.equals(user1));
    }


    @Test
    void testEqualsWithDifferentIds() {
        user1.setId("1");
        user2.setId("2");
        assertFalse(user1.equals(user2));
    }

    @Test
    void testEqualsWithNullObject() {
        assertFalse(user1.equals(null));
    }

    @Test
    void testToString() {
        user1.setId("1");
        user1.setUsername("bozidar");
        user1.setEmail("bozidar@example.com");
        user1.setPassword("strongPassword");
        user1.setActive(true);

        String expected = "UserEntity(id=1, username=bozidar, email=bozidar@example.com, password=strongPassword, active=true)";
        assertEquals(expected, user1.toString());
    }
}
