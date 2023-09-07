package com.levi9.socialnetwork.mapper;

import com.levi9.socialnetwork.entity.GroupEntity;
import com.levi9.socialnetwork.entity.PostEntity;
import com.levi9.socialnetwork.entity.UserEntity;
import com.levi9.socialnetwork.response.PostResponse;
import com.levi9.socialnetwork.util.GenericValues;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringRunner;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
public class PostMapperTest {

    @InjectMocks
    private PostMapper postMapper;

    @Test
    @DisplayName("Mapper should map PostEntity to PostResponse")
    public void shouldMapToPostResponse() {

        PostEntity post = createDummyPost();
        PostResponse response = postMapper.toPostResponse(post);

        assertEquals(post.getId(), response.getId());
        assertEquals(post.getText(), response.getText());
        assertEquals(post.getDateTimeCreated(), response.getDateTimeCreated());
        assertEquals(post.isClosed(), response.isClosed());
        assertEquals(post.getGroup(), response.getGroup());
        assertEquals(post.getCreator(), response.getCreator());
    }

    private PostEntity createDummyPost() {

        UserEntity user = new UserEntity();
        user.setId(GenericValues.DUMMY_USER_ID);

        GroupEntity group = new GroupEntity();
        group.setId(GenericValues.GROUP_ID);
        group.setName(GenericValues.DUMMY_GROUP_NAME);
        group.setClosed(GenericValues.DUMMY_GROUP_NOT_CLOSED);
        group.setAdmin(user);

        PostEntity post = new PostEntity();
        post.setId(GenericValues.POST1_ID);
        post.setText(GenericValues.DUMMY_POST_TEXT);
        post.setDateTimeCreated(LocalDateTime.now());
        post.setClosed(GenericValues.DUMMY_POST_CLOSED);
        post.setGroup(group);
        post.setCreator(user);

        return post;
    }
}