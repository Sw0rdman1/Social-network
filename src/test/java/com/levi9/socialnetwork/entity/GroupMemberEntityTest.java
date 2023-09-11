package com.levi9.socialnetwork.entity;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class GroupMemberEntityTest {

    private GroupMemberEntity groupMember1;
    private GroupMemberEntity groupMember2;

    @BeforeEach
    void setUp() {
        groupMember1 = new GroupMemberEntity();
        groupMember2 = new GroupMemberEntity();
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(groupMember1);
        assertNull(groupMember1.getId());
        assertNull(groupMember1.getMember());
        assertNull(groupMember1.getGroup());
        assertNull(groupMember1.getDateTimeJoined());
    }

    @Test
    void testSetDateTimeJoinedValid() {
        LocalDateTime pastDateTime = LocalDateTime.now().minusDays(1);
        groupMember1.setDateTimeJoined(pastDateTime);
        assertEquals(pastDateTime, groupMember1.getDateTimeJoined());
    }

    @Test
    void testSetDateTimeJoinedInFuture() {
        LocalDateTime futureDateTime = LocalDateTime.now().plusDays(1);
        assertThrows(IllegalArgumentException.class, () -> groupMember1.setDateTimeJoined(futureDateTime));
    }

    @Test
    void testEqualsWithSameInstance() {
        assertTrue(groupMember1.equals(groupMember1));
    }


    @Test
    void testEqualsWithDifferentIds() {
        groupMember1.setId(1L);
        groupMember2.setId(2L);
        assertFalse(groupMember1.equals(groupMember2));
    }

    @Test
    void testEqualsWithNullObject() {
        assertFalse(groupMember1.equals(null));
    }

    @Test
    void testToString() {
        groupMember1.setId(1L);
        groupMember1.setDateTimeJoined(LocalDateTime.of(2022, 9, 12, 14, 30));
        GroupEntity group = new GroupEntity();
        group.setName("Grupa");
        UserEntity user = new UserEntity();
        user.setUsername("Bozidar");
        groupMember1.setMember(user);
        groupMember1.setGroup(group);

        String expected = "GroupMemberEntity{id=1, member=Bozidar, group=Grupa, dateTimeJoined=2022-09-12T14:30}";
        assertEquals(expected, groupMember1.toString());
    }
}
