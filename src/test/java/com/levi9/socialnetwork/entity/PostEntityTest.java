package com.levi9.socialnetwork.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class PostEntityTest {

    private PostEntity post1;
    private PostEntity post2;

    @BeforeEach
    void setUp() {
        post1 = new PostEntity();
        post2 = new PostEntity();
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(post1);
        assertNull(post1.getId());
        assertNull(post1.getText());
        assertNull(post1.getDateTimeCreated());
        assertFalse(post1.isClosed());
        assertFalse(post1.isDeleted());
        assertNull(post1.getGroup());
        assertNull(post1.getCreator());
    }

    @Test
    void testSetTextValid() {
        post1.setText("This is a post.");
        assertEquals("This is a post.", post1.getText());
    }

    @Test
    void testSetTextBelowMinLength() {
        assertThrows(IllegalArgumentException.class, () -> post1.setText("Short."));
    }

    @Test
    void testSetDateTimeCreatedValid() {
        LocalDateTime pastDateTime = LocalDateTime.now().minusDays(1);
        post1.setDateTimeCreated(pastDateTime);
        assertEquals(pastDateTime, post1.getDateTimeCreated());
    }

    @Test
    void testSetDateTimeCreatedInFuture() {
        LocalDateTime futureDateTime = LocalDateTime.now().plusDays(1);
        assertThrows(IllegalArgumentException.class, () -> post1.setDateTimeCreated(futureDateTime));
    }

    @Test
    void testEqualsWithSameInstance() {
        assertEquals(post1, post1);
    }


    @Test
    void testEqualsWithDifferentIds() {
        post1.setId(1L);
        post2.setId(2L);
        assertNotEquals(post1, post2);
    }

    @Test
    void testEqualsWithNullObject() {
        assertNotEquals(null, post1);
    }

    @Test
    void testToString() {
        GroupEntity group = new GroupEntity();
        group.setName("Sample Group");

        UserEntity user = new UserEntity();
        user.setUsername("Sample User");

        post1.setId(1L);
        post1.setText("This is a sample post.");
        post1.setDateTimeCreated(LocalDateTime.of(2022, 9, 12, 14, 30));
        post1.setClosed(true);
        post1.setDeleted(false);
        post1.setGroup(group);
        post1.setCreator(user);

        String expected = "PostEntity{id=1, text='This is a sample post.', dateTimeCreated=2022-09-12T14:30, closed=true, deleted=false, group=Sample Group, creator=Sample User}";
        assertEquals(expected, post1.toString());
    }
}
