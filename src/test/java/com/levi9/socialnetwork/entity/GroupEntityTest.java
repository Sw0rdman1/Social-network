package com.levi9.socialnetwork.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GroupEntityTest {

    private GroupEntity group1;
    private GroupEntity group2;

    @BeforeEach
    void setUp() {
        group1 = new GroupEntity();
        group2 = new GroupEntity();
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(group1);
        assertNull(group1.getId());
        assertNull(group1.getName());
        assertFalse(group1.isClosed());
        assertNull(group1.getAdmin());
    }

    @Test
    void testSetNameValid() {
        group1.setName("My Group");
        assertEquals("My Group", group1.getName());
    }

    @Test
    void testSetNameBelowMinLength() {
        assertThrows(IllegalArgumentException.class, () -> group1.setName("A"));
    }

    @Test
    void testEqualsWithSameInstance() {
        assertEquals(group1, group1);
    }


    @Test
    void testEqualsWithDifferentIds() {
        group1.setId(1L);
        group2.setId(2L);
        assertNotEquals(group1, group2);
    }

    @Test
    void testEqualsWithNullObject() {
        assertNotEquals(null, group1);
    }

    @Test
    void testToString() {
        group1.setId(1L);
        group1.setName("My Group");
        group1.setClosed(true);

        String expected = "GroupEntity(id=1, name=My Group, closed=true, admin=null)";
        assertEquals(expected, group1.toString());
    }
}
