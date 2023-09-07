package com.levi9.socialnetwork.controllers;

import com.levi9.socialnetwork.entity.PostEntity;
import com.levi9.socialnetwork.mapper.PostMapper;
import com.levi9.socialnetwork.request.PostModificationRequest;
import com.levi9.socialnetwork.request.PostRequest;
import com.levi9.socialnetwork.response.PostResponse;
import com.levi9.socialnetwork.service.post.PostService;
import com.levi9.socialnetwork.util.GenericValues;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.List;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
public class PostControllerTest {

    @Mock
    private PostService postService;

    @Mock
    private PostMapper postMapper;

    @InjectMocks
    private PostController postController;

    @Test
    @DisplayName("Controller should call service for getting feed posts")
    public void shouldCallPostServiceForGettingFeedPosts() {
        postController.getNewsFeed();
        verify(postService).getFeedPosts();
    }

    @Test
    @DisplayName("Controller should call service for getting profile posts")
    public void shouldCallPostServiceForGettingProfilePosts() {
        postController.getUsersPosts(anyString());
        List<PostResponse> posts = verify(postService).getUsersProfilePosts(anyString());
    }

    @Test
    @DisplayName("Controller should call service for creating post")
    public void shouldCallPostServiceForCreatingPost() {
        PostRequest createPostRequest = PostRequest.builder()
                .text(GenericValues.TEXT)
                .closed(false)
                .username(GenericValues.USERNAME)
                .build();
        postController.createPost(createPostRequest);
        PostEntity postEntity = verify(postService).createPost(createPostRequest);
        verify(postMapper).toPostResponse(postEntity);
    }

    @Test
    @DisplayName("Controller should call service for updating post")
    public void shouldCallPostServiceForUpdatingPost() {

        var updatePostRequest = new PostModificationRequest(1L, "Dummy test", true);
        postController.updatePost(updatePostRequest);
        PostEntity postEntity = verify(postService).updatePost(updatePostRequest);
        verify(postMapper).toPostResponse(postEntity);
    }
}