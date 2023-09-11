package com.levi9.socialnetwork.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class CommentReplyEntityTest {

    private CommentReplyEntity commentReply1;
    private CommentReplyEntity commentReply2;

    @BeforeEach
    void setUp() {
        commentReply1 = new CommentReplyEntity();
        commentReply2 = new CommentReplyEntity();
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(commentReply1);
        assertNull(commentReply1.getId());
        assertNull(commentReply1.getText());
        assertNull(commentReply1.getComment());
        assertNull(commentReply1.getCreator());
        assertNull(commentReply1.getDateTimeCreated());
    }

    @Test
    void testSetTextValid() {
        commentReply1.setText("Nice reply!");
        assertEquals("Nice reply!", commentReply1.getText());
    }

    @Test
    void testSetTextBelowMinLength() {
        assertThrows(IllegalArgumentException.class, () -> commentReply1.setText("Hi"));
    }

    @Test
    void testEqualsWithSameInstance() {
        assertTrue(commentReply1.equals(commentReply1));
    }


    @Test
    void testEqualsWithDifferentIds() {
        commentReply1.setId(1L);
        commentReply2.setId(2L);
        assertFalse(commentReply1.equals(commentReply2));
    }

    @Test
    void testEqualsWithNullObject() {
        assertFalse(commentReply1.equals(null));
    }

    @Test
    void testToString() {
        commentReply1.setId(1L);
        commentReply1.setText("Great reply!");
        commentReply1.setDateTimeCreated(LocalDateTime.of(2023, 9, 12, 14, 30));
        commentReply1.setComment(new CommentEntity());
        commentReply1.setCreator(new UserEntity());

        String expected = "CommentReplyEntity{id=1, text='Great reply!', comment=null, creator=null, dateTimeCreated=2023-09-12T14:30}";
        assertEquals(expected, commentReply1.toString());
    }
}
