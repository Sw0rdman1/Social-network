package com.levi9.socialnetwork.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PostHiddenFromEntityTest {

    private PostHiddenFromEntity postHidden1;
    private PostHiddenFromEntity postHidden2;

    @BeforeEach
    void setUp() {
        postHidden1 = new PostHiddenFromEntity();
        postHidden2 = new PostHiddenFromEntity();
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(postHidden1);
        assertNull(postHidden1.getId());
        assertNull(postHidden1.getUser());
        assertNull(postHidden1.getPost());
    }

    @Test
    void testEqualsWithSameInstance() {
        assertEquals(postHidden1, postHidden1);
    }

    @Test
    void testEqualsWithDifferentIds() {
        postHidden1.setId(1L);
        postHidden2.setId(2L);
        assertNotEquals(postHidden1, postHidden2);
    }

    @Test
    void testEqualsWithNullObject() {
        assertNotEquals(null, postHidden1);
    }

    @Test
    void testToString() {
        UserEntity user = new UserEntity();
        user.setUsername("Sample User");

        PostEntity post = new PostEntity();
        post.setId(1L);

        postHidden1.setId(1L);
        postHidden1.setUser(user);
        postHidden1.setPost(post);

        String expected = "PostHiddenFromEntity{id=1, user=Sample User, post=1}";
        assertEquals(expected, postHidden1.toString());
    }
}
