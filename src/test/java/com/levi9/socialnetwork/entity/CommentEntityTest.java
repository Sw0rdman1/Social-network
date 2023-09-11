package com.levi9.socialnetwork.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class CommentEntityTest {

    private CommentEntity comment1;
    private CommentEntity comment2;

    @BeforeEach
    void setUp() {
        comment1 = new CommentEntity();
        comment2 = new CommentEntity();
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(comment1);
        assertNull(comment1.getId());
        assertNull(comment1.getText());
        assertNull(comment1.getPost());
        assertNull(comment1.getCreator());
        assertNull(comment1.getDateTimeCreated());
    }

    @Test
    void testSetTextValid() {
        comment1.setText("Nice post!");
        assertEquals("Nice post!", comment1.getText());
    }

    @Test
    void testSetTextBelowMinLength() {
        assertThrows(IllegalArgumentException.class, () -> comment1.setText("Hi"));
    }

    @Test
    void testEqualsWithSameInstance() {
        assertTrue(comment1.equals(comment1));
    }



    @Test
    void testEqualsWithDifferentIds() {
        comment1.setId(1L);
        comment2.setId(2L);
        assertFalse(comment1.equals(comment2));
    }

    @Test
    void testEqualsWithNullObject() {
        assertFalse(comment1.equals(null));
    }

    @Test
    void testToString() {
        comment1.setId(1L);
        comment1.setText("Great post!");
        comment1.setDateTimeCreated(LocalDateTime.of(2023, 9, 12, 14, 30));
        PostEntity post = new PostEntity();
        post.setId(1L);
        comment1.setPost(post);
        comment1.setCreator(new UserEntity());

        String expected = "CommentEntity{id=1, text='Great post!', post=1, creator=null, dateTimeCreated=2023-09-12T14:30}";
        assertEquals(expected, comment1.toString());
    }
}
