package com.levi9.socialnetwork.service;

import com.levi9.socialnetwork.entity.PostEntity;
import com.levi9.socialnetwork.entity.UserEntity;
import com.levi9.socialnetwork.exception.customexception.EntityNotFoundException;
import com.levi9.socialnetwork.exception.customexception.PostException;
import com.levi9.socialnetwork.repository.PostRepository;
import com.levi9.socialnetwork.request.PostModificationRequest;
import com.levi9.socialnetwork.service.post.PostServiceImpl;
import com.levi9.socialnetwork.util.AuthUtil;
import com.levi9.socialnetwork.util.GenericValues;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    MockedStatic<AuthUtil> utilities;

    @InjectMocks
    private PostServiceImpl postService;

    @Before
    public void beforeEach() {
        utilities = Mockito.mockStatic(AuthUtil.class);
    }

    @After
    public void afterEach() {
        utilities.close();
    }

    @Test
    @DisplayName("Service should throw exception for non-existing post")
    public void shouldThrowExceptionForNotFoundUpdatingPost() {

        var updatePostRequest = new PostModificationRequest(
                GenericValues.POST1_ID, GenericValues.DUMMY_POST_TEXT, GenericValues.DUMMY_POST_CLOSED
        );

        when(postRepository.findById(updatePostRequest.getId())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> postService.updatePost(updatePostRequest));
        assertEquals("Post with id " + updatePostRequest.getId() + " cannot be updated because it does not exist", exception.getMessage());

        verify(postRepository, times(1)).findById(updatePostRequest.getId());
        verify(postRepository, never()).save(any(PostEntity.class));
    }

    @Test
    @DisplayName("Service should throw exception for expired post")
    public void shouldThrowExceptionForExpiredUpdatingPost() {

        var updatePostRequest = new PostModificationRequest(
                GenericValues.POST1_ID, GenericValues.DUMMY_POST_TEXT, GenericValues.DUMMY_POST_CLOSED
        );

        when(postRepository.findById(any(Long.class))).thenAnswer(i -> {
            PostEntity post = new PostEntity()
                    .builder()
                    .id(updatePostRequest.getId())
                    .deleted(GenericValues.DUMMY_POST_CLOSED)
                    .build();

            return Optional.of(post);
        });

        PostException exception = assertThrows(PostException.class, () -> postService.updatePost(updatePostRequest));
        assertEquals("Post with id " + updatePostRequest.getId() + " cannot be updated because it has expired", exception.getMessage());

        verify(postRepository, times(1)).findById(updatePostRequest.getId());
        verify(postRepository, never()).save(any(PostEntity.class));
    }

    @Test
    @DisplayName("Service should throw exception for trying to update someone else's post")
    public void shouldThrowExceptionForIllegalUpdatingPost() {

        var updatePostRequest = new PostModificationRequest(
                GenericValues.POST1_ID, GenericValues.DUMMY_POST_TEXT, GenericValues.DUMMY_POST_CLOSED
        );

        UserEntity user = new UserEntity();
        user.setId(GenericValues.DUMMY_USER1_ID);

        PostEntity post = new PostEntity()
                .builder()
                .id(updatePostRequest.getId())
                .deleted(GenericValues.DUMMY_POST_NOT_DELETED)
                .build();
        post.setCreator(user);

        utilities.when(() -> AuthUtil.getPrincipalId()).thenReturn(GenericValues.DUMMY_USER2_ID);
        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));

        PostException exception = assertThrows(PostException.class, () -> postService.updatePost(updatePostRequest));
        assertEquals("Post with id " + updatePostRequest.getId() + " can be updated only by the owner of the post", exception.getMessage());

        verify(postRepository, times(1)).findById(post.getId());
        utilities.verify(() -> AuthUtil.getPrincipalId(), times(1));
        verify(postRepository, never()).save(any(PostEntity.class));
    }

    @Test
    @DisplayName("Service should update post")
    public void shouldUpdatePost() {

        var updatePostRequest = new PostModificationRequest(
                GenericValues.POST1_ID, GenericValues.DUMMY_POST_TEXT1, GenericValues.DUMMY_POST_CLOSED
        );

        UserEntity user = new UserEntity();
        user.setId(GenericValues.DUMMY_USER1_ID);

        PostEntity post = new PostEntity()
                .builder()
                .id(updatePostRequest.getId())
                .text(GenericValues.DUMMY_POST_TEXT2)
                .closed(!updatePostRequest.isClosed())
                .deleted(GenericValues.DUMMY_POST_NOT_DELETED)
                .build();
        post.setCreator(user);

        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        when(postRepository.save(any(PostEntity.class))).thenAnswer(i -> i.getArgument(0));
        utilities.when(() -> AuthUtil.getPrincipalId()).thenReturn(GenericValues.DUMMY_USER1_ID);

        PostEntity updatedPost = postService.updatePost(updatePostRequest);

        assertEquals(updatePostRequest.getId(), updatedPost.getId());
        assertEquals(updatePostRequest.getText(), updatedPost.getText());
        assertEquals(updatePostRequest.isClosed(), updatedPost.isClosed());

        verify(postRepository, times(1)).findById(post.getId());
        utilities.verify(() -> AuthUtil.getPrincipalId(), times(1));
        verify(postRepository).save(updatedPost);
    }
}