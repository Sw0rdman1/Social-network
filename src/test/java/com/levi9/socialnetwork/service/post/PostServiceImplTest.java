package com.levi9.socialnetwork.service.post;

import com.levi9.socialnetwork.entity.*;
import com.levi9.socialnetwork.exception.customexception.EmptyPostException;
import com.levi9.socialnetwork.exception.customexception.EntityNotFoundException;
import com.levi9.socialnetwork.exception.customexception.PostException;
import com.levi9.socialnetwork.exception.customexception.PostNoPermissionException;
import com.levi9.socialnetwork.mapper.PostMapper;
import com.levi9.socialnetwork.repository.FriendshipRepository;
import com.levi9.socialnetwork.repository.PostHiddenFromRepository;
import com.levi9.socialnetwork.repository.PostRepository;
import com.levi9.socialnetwork.repository.UserRepository;
import com.levi9.socialnetwork.request.PostModificationRequest;
import com.levi9.socialnetwork.request.PostRequest;
import com.levi9.socialnetwork.response.PostResponse;
import com.levi9.socialnetwork.service.user.UserService;
import com.levi9.socialnetwork.util.AuthUtil;
import com.levi9.socialnetwork.util.GenericValues;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class PostServiceImplTest {
    @Mock
    private PostHiddenFromRepository postHiddenFromRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private FriendshipRepository friendshipRepository;

    @Mock
    private UserService userService;

    @Mock
    private PostMapper postMapper;

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
    @DisplayName("Service should return feed for logged user with no friends")
    public void shouldGetFeedPostsForUserWithoutFriends() {
        utilities.when(AuthUtil::getPrincipalId).thenReturn(GenericValues.USER_ID);

        List<PostEntity> returnedPosts = Stream
                .of(PostEntity.builder().creator(UserEntity.builder().id(GenericValues.USER_ID).build()).build(),
                        PostEntity.builder().creator(UserEntity.builder().id(GenericValues.USER_ID).build()).build())
                .toList();

        when(friendshipRepository.findUserFriends(anyString())).thenReturn(new LinkedList<>());
        when(postRepository.findByCreatorIdInAndIdNotInOrderByDateTimeCreatedDesc(Mockito.<String>anyList(), Mockito.<Long>anyList())).thenReturn(returnedPosts);
        when(postMapper.toPostResponse(any(PostEntity.class))).thenAnswer(i -> {
            PostEntity postEntity = i.getArgument(0);
            return PostResponse
                    .builder()
                    .creator(postEntity.getCreator())
                    .build();
        });

        List<PostResponse> posts = postService.getFeedPosts();
        assertEquals(2, posts.size());
        for (PostResponse post : posts) {
            assertEquals(GenericValues.USER_ID, post.getCreator().getId());
        }

        verify(friendshipRepository, times(1)).findUserFriends(anyString());
        verify(postHiddenFromRepository, times(1)).findByUserId(anyString());
        verify(postRepository, times(1)).findByCreatorIdInAndIdNotInOrderByDateTimeCreatedDesc(anyList(), anyList());
    }

    @Test
    @DisplayName("Service should return feed for logged user with friends")
    public void shouldGetFeedPostsForUserWithFriends() {
        utilities.when(AuthUtil::getPrincipalId).thenReturn(GenericValues.USER_ID);
        List<String> friendIds = Stream
                .of(GenericValues.USER2_USERNAME, GenericValues.USER3_USERNAME)
                .collect(Collectors.toList());
        List<PostEntity> returnedPosts = createMockPostList();

        when(friendshipRepository.findUserFriends(anyString())).thenReturn(friendIds);
        when(postRepository.findByCreatorIdInAndIdNotInOrderByDateTimeCreatedDesc(Mockito.<String>anyList(), Mockito.<Long>anyList())).thenReturn(returnedPosts);
        when(postMapper.toPostResponse(any(PostEntity.class))).thenAnswer(i -> {
            PostEntity postEntity = i.getArgument(0);
            return PostResponse
                    .builder()
                    .id(postEntity.getId())
                    .creator(postEntity.getCreator())
                    .build();
        });

        List<PostResponse> posts = postService.getFeedPosts();
        assertEquals(3, posts.size());
        for (PostResponse post : posts) {
            assertTrue((post.getCreator().getId().equals(GenericValues.USER_ID)
                    || post.getCreator().getId().equals(GenericValues.USER2_USERNAME)
                    || post.getCreator().getId().equals(GenericValues.USER3_USERNAME)));
        }

        verify(friendshipRepository, times(1)).findUserFriends(anyString());
        verify(postHiddenFromRepository, times(1)).findByUserId(anyString());
        verify(postRepository, times(1)).findByCreatorIdInAndIdNotInOrderByDateTimeCreatedDesc(anyList(), anyList());
    }

    @Test
    @DisplayName("Service should throw exception for non-existing profile")
    public void shouldThrowExceptionForProfileNotFound() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> postService.getUsersProfilePosts(GenericValues.USERNAME));
        assertEquals("User with username " + GenericValues.USERNAME + " not found", exception.getMessage());

        verify(userRepository, times(1)).findByUsername(GenericValues.USERNAME);
        verify(postRepository, never()).findByCreatorIdInAndIdNotInOrderByDateTimeCreatedDesc(anyList(), anyList());
    }

    @Test
    @DisplayName("Service should return empty profile dashboard for requested profile with no posts")
    public void shouldGetProfilePostsForUserWithNoPosts() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(UserEntity.builder().id(GenericValues.USER_ID).build()));
        when(postRepository.findByCreatorIdInAndIdNotInOrderByDateTimeCreatedDesc(anyList(), anyList())).thenReturn(Collections.emptyList());
        when(postRepository.findByCreatorIdAndIdNotInAndClosedIsFalseOrderByDateTimeCreatedDesc(anyString(), anyList())).thenReturn(Collections.emptyList());

        List<PostResponse> posts = postService.getUsersProfilePosts(GenericValues.USERNAME);
        assertEquals(0, posts.size());

        verify(userRepository, times(1)).findByUsername(anyString());
        verify(postRepository, atMost(1)).findByCreatorIdInAndIdNotInOrderByDateTimeCreatedDesc(anyList(), anyList());
        verify(postRepository, atMost(1)).findByCreatorIdAndIdNotInAndClosedIsFalseOrderByDateTimeCreatedDesc(anyString(), anyList());
    }

    @Test
    @DisplayName("Service should return profile dashboard for requested profile with some posts")
    public void shouldGetProfilePostsForUserWithPosts() {
        utilities.when(AuthUtil::getPrincipalId).thenReturn(GenericValues.USER2_USERNAME);
        List<PostEntity> returnedPosts = createMockPostList();

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(UserEntity.builder().id(GenericValues.USER_ID).build()));
        when(postRepository.findByCreatorIdInAndIdNotInOrderByDateTimeCreatedDesc(anyList(), anyList())).thenReturn(returnedPosts);
        when(postRepository.findByCreatorIdAndIdNotInAndClosedIsFalseOrderByDateTimeCreatedDesc(anyString(), anyList())).thenReturn(returnedPosts);
        when(postMapper.toPostResponse(any(PostEntity.class))).thenAnswer(i -> {
            PostEntity postEntity = i.getArgument(0);
            return PostResponse.builder().id(postEntity.getId()).build();
        });

        List<PostResponse> posts = postService.getUsersProfilePosts(GenericValues.USERNAME);
        assertEquals(3, posts.size());
        for (PostResponse post : posts) {
            assertNotNull(post.getId());
        }

        verify(userRepository, times(1)).findByUsername(GenericValues.USERNAME);
        verify(postRepository, atMost(1)).findByCreatorIdInAndIdNotInOrderByDateTimeCreatedDesc(anyList(), anyList());
        verify(postRepository, atMost(1)).findByCreatorIdAndIdNotInAndClosedIsFalseOrderByDateTimeCreatedDesc(anyString(), anyList());
    }

    @Test
    @DisplayName("Service should return profile dashboard for logged user")
    public void shouldGetProfilePostsForLoggedUser(){
        utilities.when(AuthUtil::getPrincipalId).thenReturn(GenericValues.USER_ID);
        List<PostEntity> returnedPosts = createMockPostListCreatorUserID();

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(UserEntity.builder().id(GenericValues.USER_ID).build()));
        when(postRepository.findByCreatorIdInAndIdNotInOrderByDateTimeCreatedDesc(anyList(),anyList())).thenReturn(returnedPosts);
        when(postMapper.toPostResponse(any(PostEntity.class))).thenAnswer(i -> {
            PostEntity postEntity = i.getArgument(0);
            return PostResponse.builder().id(postEntity.getId()).creator(postEntity.getCreator()).build();
        });

        List<PostResponse> posts = postService.getUsersProfilePosts(GenericValues.USER_ID);
        assertEquals(3, posts.size());
        for(PostResponse post : posts){
            assertEquals(GenericValues.USER_ID, post.getCreator().getId());
        }

        verify(userRepository, times(1)).findByUsername(anyString());
        verify(postRepository, times(1)).findByCreatorIdInAndIdNotInOrderByDateTimeCreatedDesc(anyList(), anyList());
        verify(postRepository, times(0)).findByCreatorIdAndIdNotInAndClosedIsFalseOrderByDateTimeCreatedDesc(anyString(), anyList());
    }

    @Test
    @DisplayName("Service should return profile dashboard when logged user and searched user are not friends")
    public void shouldGetProfilePostsWhenLoggedUserIsNotFriendWithRequested(){
        List<PostEntity> returnedPosts = createMockPostListCreatorUserID();

        utilities.when(AuthUtil::getPrincipalId).thenReturn(GenericValues.USER_ID);
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(UserEntity.builder().id(GenericValues.USERNAME).build()));
        when(friendshipRepository.findByPairOfIds(anyString(),anyString())).thenReturn(Optional.empty());
        when(postRepository.findByCreatorIdAndIdNotInAndClosedIsFalseOrderByDateTimeCreatedDesc(anyString(), anyList())).thenReturn(returnedPosts);
        when(postMapper.toPostResponse(any(PostEntity.class))).thenAnswer(i -> {
            PostEntity postEntity = i.getArgument(0);
            return PostResponse.builder().id(postEntity.getId()).creator(postEntity.getCreator()).build();
        });

        List<PostResponse> posts = postService.getUsersProfilePosts(GenericValues.USER_ID);
        assertEquals(3, posts.size());
        for(PostResponse post : posts){
            assertEquals(GenericValues.USER_ID, post.getCreator().getId());
        }

        verify(userRepository, times(1)).findByUsername(anyString());
        verify(postRepository, times(0)).findByCreatorIdInAndIdNotInOrderByDateTimeCreatedDesc(anyList(), anyList());
        verify(postRepository, times(1)).findByCreatorIdAndIdNotInAndClosedIsFalseOrderByDateTimeCreatedDesc(anyString(), anyList());
    }

    @Test
    @DisplayName("Service should return profile dashboard when logged user and searched user are friends")
    public void shouldGetProfilePostsWhenLoggedUserIsFriendWithRequested(){
        List<PostEntity> returnedPosts = createMockPostListCreatorUserID();

        utilities.when(AuthUtil::getPrincipalId).thenReturn(GenericValues.USER_ID);
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(UserEntity.builder().id(GenericValues.USERNAME).build()));
        when(friendshipRepository.findByPairOfIds(anyString(),anyString())).thenReturn(Optional.of(new FriendshipEntity()));
        when(postRepository.findByCreatorIdInAndIdNotInOrderByDateTimeCreatedDesc(anyList(),anyList())).thenReturn(returnedPosts);
        when(postMapper.toPostResponse(any(PostEntity.class))).thenAnswer(i -> {
            PostEntity postEntity = i.getArgument(0);
            return PostResponse.builder().id(postEntity.getId()).creator(postEntity.getCreator()).build();
        });

        List<PostResponse> posts = postService.getUsersProfilePosts(GenericValues.USER_ID);
        assertEquals(3, posts.size());
        for(PostResponse post : posts){
            assertEquals(GenericValues.USER_ID, post.getCreator().getId());
        }

        verify(userRepository, times(1)).findByUsername(anyString());
        verify(postRepository, times(1)).findByCreatorIdInAndIdNotInOrderByDateTimeCreatedDesc(anyList(), anyList());
        verify(postRepository, times(0)).findByCreatorIdAndIdNotInAndClosedIsFalseOrderByDateTimeCreatedDesc(anyString(), anyList());
    }

    @Test
    @DisplayName("Service should successfully create post")
    public void shouldSuccessfullyCreatePost() {

        PostRequest createPostRequest = PostRequest.builder()
                .text(GenericValues.TEXT)
                .closed(false)
                .username(GenericValues.USERNAME)
                .build();

        UserEntity userEntity = UserEntity.builder()
                .email(GenericValues.EMAIL)
                .username(GenericValues.USERNAME)
                .active(true)
                .id(GenericValues.USER_ID)
                .build();

        PostEntity expectedPostEntity = PostEntity.builder()
                .text(GenericValues.TEXT)
                .dateTimeCreated(LocalDateTime.now())
                .closed(false)
                .deleted(false)
                .creator(userEntity)
                .build();

        when(userService.findByUsername(GenericValues.USERNAME)).thenReturn(userEntity);
        when(postRepository.save(Mockito.any(PostEntity.class))).thenReturn(expectedPostEntity);
        var realPostEntity = postService.createPost(createPostRequest);
        assertNotNull(realPostEntity.getDateTimeCreated());
        assertEquals(realPostEntity.getCreator(), expectedPostEntity.getCreator());
        assertEquals(realPostEntity.getText(), expectedPostEntity.getText());
    }

    @Test
    @DisplayName("Service should throw exception for empty post")
    public void shouldThrowEmptyPostExceptionForCreatingPost() {
        PostRequest createPostRequest = PostRequest.builder()
                .text("")
                .closed(false)
                .username(GenericValues.USERNAME)
                .build();

        assertThrows(EmptyPostException.class, () -> postService.createPost(createPostRequest));
    }

    @Test
    @DisplayName("Service should throw exception for non-existing post")
    public void shouldThrowExceptionForNotFoundUpdatingPost() {

        var updatePostRequest = new PostModificationRequest(1L, "Dummy test", true);

        when(postRepository.findById(updatePostRequest.getId())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> postService.updatePost(updatePostRequest));
        assertEquals("Post with id " + updatePostRequest.getId() + " cannot be updated because it does not exist", exception.getMessage());

        verify(postRepository, times(1)).findById(updatePostRequest.getId());
        verify(postRepository, never()).save(any(PostEntity.class));
    }

    @Test
    @DisplayName("Service should throw exception for expired post")
    public void shouldThrowExceptionForExpiredUpdatingPost() {

        var updatePostRequest = new PostModificationRequest(1L, "Dummy test", true);

        when(postRepository.findById(any(Long.class))).thenAnswer(i -> {
            PostEntity post = new PostEntity()
                    .builder()
                    .id(updatePostRequest.getId())
                    .deleted(true)
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

        var updatePostRequest = new PostModificationRequest(1L, "Dummy test", true);

        UserEntity user = new UserEntity();
        user.setId("dummy user1");

        PostEntity post = new PostEntity()
                .builder()
                .id(updatePostRequest.getId())
                .deleted(false)
                .build();
        post.setCreator(user);

        utilities.when(() -> AuthUtil.getPrincipalId()).thenReturn("dummy user2");
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

        var updatePostRequest = new PostModificationRequest(1L, "Dummy text 1", true);

        UserEntity user = new UserEntity();
        user.setId("dummy user1");

        PostEntity post = new PostEntity()
                .builder()
                .id(updatePostRequest.getId())
                .text("Dummy text 2")
                .closed(!updatePostRequest.isClosed())
                .deleted(false)
                .build();
        post.setCreator(user);

        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        when(postRepository.save(any(PostEntity.class))).thenAnswer(i -> i.getArgument(0));
        utilities.when(() -> AuthUtil.getPrincipalId()).thenReturn("dummy user1");

        PostEntity updatedPost = postService.updatePost(updatePostRequest);

        assertEquals(updatePostRequest.getId(), updatedPost.getId());
        assertEquals(updatePostRequest.getText(), updatedPost.getText());
        assertEquals(updatePostRequest.isClosed(), updatedPost.isClosed());

        verify(postRepository, times(1)).findById(post.getId());
        utilities.verify(() -> AuthUtil.getPrincipalId(), times(1));
        verify(postRepository).save(updatedPost);
    }

    @Test
    @DisplayName("Service should throw exception for non-existing post")
    public void shouldThrowExceptionForNotFoundDeletingPost() {
        when(postRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        EntityNotFoundException exception = Assert.assertThrows(EntityNotFoundException.class, () -> postService.deletePost(1L));
        Assert.assertEquals("Post with ID 1 does not exist", exception.getMessage());

        verify(postRepository, times(1)).findById(any(Long.class));
        verify(postRepository, never()).delete(any(PostEntity.class));
    }

    @Test
    @DisplayName("Service should throw exception for trying to delete post if you are not creator or admin of group!")
    public void shouldThrowExceptionForNoPermissionDeletingPost() {

        UserEntity user1 = new UserEntity();
        user1.setUsername(GenericValues.USERNAME);

        UserEntity user2 = new UserEntity();
        user2.setUsername(GenericValues.USER2_USERNAME);

        GroupEntity group = new GroupEntity();
        group.setAdmin(user2);

        PostEntity post = PostEntity
                .builder()
                .id(1L)
                .deleted(false)
                .creator(user1)
                .group(group)
                .build();

        utilities.when(AuthUtil::getPrincipalUsername).thenReturn(GenericValues.USER3_USERNAME);
        when(postRepository.findById(any(Long.class))).thenReturn(Optional.of(post));

        PostNoPermissionException exception = Assert.assertThrows(PostNoPermissionException.class, () -> postService.deletePost(1L));
        Assert.assertEquals("You don't have permission to delete this post", exception.getMessage());

        verify(postRepository, times(1)).findById(any(Long.class));
        utilities.verify(AuthUtil::getPrincipalUsername, times(1));
        verify(postRepository, never()).delete(any(PostEntity.class));
    }

    @Test
    @DisplayName("Service should delete post - You are creator of post")
    public void shouldUpdatePostCreator() {
        UserEntity user = new UserEntity();
        user.setUsername(GenericValues.USERNAME);

        PostEntity post = PostEntity
                .builder()
                .id(1L)
                .creator(user)
                .build();

        when(postRepository.findById(any(Long.class))).thenReturn(Optional.of(post));
        utilities.when(AuthUtil::getPrincipalUsername).thenReturn(GenericValues.USERNAME);

        postService.deletePost(1L);
        verify(postRepository, times(1)).findById(any(Long.class));
        utilities.verify(AuthUtil::getPrincipalUsername, times(1));
    }

    @Test
    @DisplayName("Service should delete post - You are admin of group")
    public void shouldUpdatePostGroupAdmin() {
        UserEntity user1 = new UserEntity();
        user1.setUsername(GenericValues.USERNAME);

        UserEntity user2 = new UserEntity();
        user2.setUsername(GenericValues.USER2_USERNAME);

        GroupEntity group = new GroupEntity();
        group.setAdmin(user2);

        PostEntity post = PostEntity
                .builder()
                .id(1L)
                .creator(user1)
                .group(group)
                .build();

        when(postRepository.findById(any(Long.class))).thenReturn(Optional.of(post));
        utilities.when(AuthUtil::getPrincipalUsername).thenReturn(GenericValues.USER2_USERNAME);

        postService.deletePost(1L);
        verify(postRepository, times(1)).findById(any(Long.class));
        utilities.verify(AuthUtil::getPrincipalUsername, times(1));
    }

    private List<PostEntity> createMockPostList(){
        return Stream.of(PostEntity.builder().id(GenericValues.POST1_ID).creator(UserEntity.builder().id(GenericValues.USER_ID).build()).build()
                        , PostEntity.builder().id(GenericValues.POST2_ID).creator(UserEntity.builder().id(GenericValues.USER2_USERNAME).build()).build()
                        , PostEntity.builder().id(GenericValues.POST3_ID).creator(UserEntity.builder().id(GenericValues.USER3_USERNAME).build()).build())
                .toList();
    }

    private List<PostEntity> createMockPostListCreatorUserID(){
        return Stream.of(PostEntity.builder().id(GenericValues.POST1_ID).creator(UserEntity.builder().id(GenericValues.USER_ID).build()).build()
                        , PostEntity.builder().id(GenericValues.POST2_ID).creator(UserEntity.builder().id(GenericValues.USER_ID).build()).build()
                        , PostEntity.builder().id(GenericValues.POST3_ID).creator(UserEntity.builder().id(GenericValues.USER_ID).build()).build())
                .toList();
    }
}