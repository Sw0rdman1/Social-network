package com.levi9.socialnetwork.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EventInvitationEntityTest {

    private EventInvitationEntity invitation1;
    private EventInvitationEntity invitation2;

    @BeforeEach
    void setUp() {
        invitation1 = new EventInvitationEntity();
        invitation2 = new EventInvitationEntity();
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(invitation1);
        assertNull(invitation1.getId());
        assertNull(invitation1.getInvitee());
        assertNull(invitation1.getEvent());
        assertNull(invitation1.getStatus());
    }

    @Test
    void testEqualsWithSameInstance() {
        assertTrue(invitation1.equals(invitation1));
    }


    @Test
    void testEqualsWithDifferentIds() {
        invitation1.setId(1L);
        invitation2.setId(2L);
        assertFalse(invitation1.equals(invitation2));
    }

    @Test
    void testEqualsWithNullObject() {
        assertFalse(invitation1.equals(null));
    }

    @Test
    void testToString() {
        GroupMemberEntity invitee = new GroupMemberEntity();
        UserEntity user = new UserEntity();
        user.setUsername("Bozidar");
        invitee.setMember(user);

        EventEntity event = new EventEntity();
        event.setId(1L);

        invitation1.setId(1L);
        invitation1.setInvitee(invitee);
        invitation1.setEvent(event);
        invitation1.setStatus(EventInvitationStatusEntity.COMING);

        String expected = "EventInvitationEntity{id=1, invitee=Bozidar, event=1, status=COMING}";
        assertEquals(expected, invitation1.toString());
    }
}
