package com.levi9.socialnetwork.service.comment;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.levi9.socialnetwork.entity.*;
import com.levi9.socialnetwork.exception.customexception.*;
import com.levi9.socialnetwork.mapper.CommentMapper;
import com.levi9.socialnetwork.repository.*;
import com.levi9.socialnetwork.request.CommentRequest;
import com.levi9.socialnetwork.service.user.UserService;
import com.levi9.socialnetwork.util.AuthUtil;
import com.levi9.socialnetwork.util.GenericValues;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Optional;


@RunWith(SpringRunner.class)
public class CommentServiceImplTest {

    @Mock
    private PostHiddenFromRepository postHiddenFromRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PostRepository postRepository;


    @Mock
    private CommentRepository commentRepository;


    @Mock
    private FriendshipRepository friendshipRepository;

    @Mock
    private GroupMemberRepository groupMemberRepository;

    @Mock
    private UserService userService;

    @Mock
    private CommentMapper commentMapper;

    MockedStatic<AuthUtil> utilities;

    @InjectMocks
    private CommentServiceImpl commentService;

    @Before
    public void beforeEach() {
        utilities = Mockito.mockStatic(AuthUtil.class);
    }

    @After
    public void afterEach() {
        utilities.close();
    }

    @Test
    @DisplayName("Service should successfully create comment")
    public void shouldSuccessfullyCreateComment() {

        CommentRequest createCommentRequest = new CommentRequest("This is comment");

        UserEntity userEntity = UserEntity.builder()
                .email(GenericValues.EMAIL)
                .username(GenericValues.USERNAME)
                .active(true)
                .id(GenericValues.USER_ID)
                .build();

        PostEntity postEntity = PostEntity.builder()
                .text(GenericValues.TEXT)
                .dateTimeCreated(LocalDateTime.now())
                .closed(false)
                .deleted(false)
                .creator(userEntity)
                .build();

        CommentEntity expectedCommentEntity = CommentEntity.builder()
                .text(GenericValues.TEXT)
                .dateTimeCreated(LocalDateTime.now())
                .post(postEntity)
                .creator(userEntity)
                .build();

        when(userService.findByUsername(GenericValues.USERNAME)).thenReturn(userEntity);
        when(postRepository.findById(1L)).thenReturn(Optional.of(postEntity));
        when(commentRepository.save(Mockito.any(CommentEntity.class))).thenReturn(expectedCommentEntity);
        var realCommentEntity = commentService.createComment(createCommentRequest, 1L);

        assertNotNull(realCommentEntity.getDateTimeCreated());
        assertEquals(realCommentEntity.getCreator(), expectedCommentEntity.getCreator());
        assertEquals(realCommentEntity.getText(), expectedCommentEntity.getText());
    }


    @Test
    @DisplayName("Service should throw exception for empty comment")
    public void shouldThrowEmptyCommentExceptionForCreatingPost() {
        CommentRequest createCommentRequest = new CommentRequest("");
        assertThrows(EmptyCommentException.class, () -> commentService.createComment(createCommentRequest, 1L));
    }

    @Test
    @DisplayName("Service should throw exception for expired post")
    public void shouldThrowExceptionForCommentingExpiredPost() {

        CommentRequest createCommentRequest = new CommentRequest("This is comment");

        when(postRepository.findById(any(Long.class))).thenAnswer(i -> {
            PostEntity post = new PostEntity().builder()
                    .id(1L)
                    .deleted(true)
                    .build();

            return Optional.of(post);
        });

        PostException exception = assertThrows(PostException.class, () -> commentService.createComment(createCommentRequest, 1L));
        assertEquals("You cannot comment because post with id " + 1L + " has expired", exception.getMessage());

        verify(commentRepository, never()).save(any(CommentEntity.class));
    }

    @Test
    @DisplayName("Service should throw exception for non existing post")
    public void shouldThrowExceptionForCommentingNonExistingPost() {

        CommentRequest createCommentRequest = new CommentRequest("This is comment");

        when(postRepository.findById(1L)).thenAnswer(i -> Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> commentService.createComment(createCommentRequest, 1L));
        assertEquals("Post with id 1 does not exist", exception.getMessage());

        verify(commentRepository, never()).save(any(CommentEntity.class));
    }

    @Test
    public void testCheckGroupCredentials_UserIsMember() {
        GroupEntity group = new GroupEntity();
        UserEntity user = new UserEntity();

        Mockito.when(groupMemberRepository.findByMemberAndGroup(user, group)).thenReturn(Optional.of(new GroupMemberEntity()));

        assertDoesNotThrow(() -> {
            commentService.checkGroupCredentials(group, user);
        });
    }

    @Test
    public void testCheckGroupCredentials_UserIsNotMember() {
        GroupEntity group = new GroupEntity();
        UserEntity user = new UserEntity();

        Mockito.when(groupMemberRepository.findByMemberAndGroup(user, group)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            commentService.checkGroupCredentials(group, user);
        });
    }

    @Test
    public void testCheckPostPrivacy_UserIsFriend() {
        PostEntity post = new PostEntity();
        UserEntity user = new UserEntity();
        user.setId("1");
        post.setCreator(new UserEntity());
        post.getCreator().setId("2");

        Mockito.when(friendshipRepository.findByPairOfIds("1", "2")).thenReturn(Optional.of(new FriendshipEntity()));

        assertDoesNotThrow(() -> {
            commentService.checkPostPrivacy(post, user);
        });
    }

    @Test
    public void testCheckPostPrivacy_UserIsNotFriend() {
        PostEntity post = new PostEntity();
        post.setClosed(true);
        UserEntity user = new UserEntity();
        user.setId("1");
        post.setCreator(new UserEntity());
        post.getCreator().setId("2");

        when(friendshipRepository.findByPairOfIds("1", "2")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            commentService.checkPostPrivacy(post, user);
        });
    }

    @Test
    @DisplayName("Service should throw exception for deleting non-existing comment")
    public void shouldThrowExceptionForNotFoundDeletingPost() {
        when(commentRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        EntityNotFoundException exception = Assert.assertThrows(EntityNotFoundException.class, () -> commentService.deleteComment(1L));
        Assert.assertEquals("Comment with ID: 1 does not exist", exception.getMessage());

        verify(commentRepository, times(1)).findById(any(Long.class));
        verify(commentRepository, never()).delete(any(CommentEntity.class));
    }

    @Test
    @DisplayName("Service should throw exception for trying to delete comment if you dont have permission!")
    public void shouldThrowExceptionForNoPermissionDeletingComment() {

        UserEntity user1 = new UserEntity();
        user1.setUsername(GenericValues.USERNAME);

        UserEntity user2 = new UserEntity();
        user2.setUsername(GenericValues.USER2_USERNAME);

        UserEntity user3 = new UserEntity();
        user3.setUsername("Bozidar");

        GroupEntity group = new GroupEntity();
        group.setAdmin(user2);

        PostEntity post = PostEntity
                .builder()
                .id(1L)
                .deleted(false)
                .creator(user1)
                .group(group)
                .build();

        CommentEntity comment = CommentEntity
                .builder()
                .post(post)
                .id(1L)
                .creator(user3)
                .build();

        utilities.when(AuthUtil::getPrincipalUsername).thenReturn(GenericValues.USER3_USERNAME);
        when(commentRepository.findById(any(Long.class))).thenReturn(Optional.of(comment));

        CommentNoPermissionException exception = Assert.assertThrows(CommentNoPermissionException.class, () -> commentService.deleteComment(1L));
        Assert.assertEquals("You don't have permission to delete this comment", exception.getMessage());

        verify(commentRepository, times(1)).findById(any(Long.class));
        utilities.verify(AuthUtil::getPrincipalUsername, times(1));
        verify(commentRepository, never()).delete(any(CommentEntity.class));
    }

    @Test
    @DisplayName("Service should delete comment - You are creator of post")
    public void shouldDeleteCommentCreatorPost() {
        UserEntity user = new UserEntity();
        user.setUsername(GenericValues.USERNAME);

        UserEntity user2 = new UserEntity();
        user2.setUsername(GenericValues.USER2_USERNAME);

        PostEntity post = PostEntity
                .builder()
                .id(1L)
                .creator(user)
                .build();

        CommentEntity comment = CommentEntity
                .builder()
                .post(post)
                .id(1L)
                .creator(user2)
                .build();

        when(commentRepository.findById(any(Long.class))).thenReturn(Optional.of(comment));
        utilities.when(AuthUtil::getPrincipalUsername).thenReturn(GenericValues.USERNAME);

        commentService.deleteComment(1L);
        verify(commentRepository, times(1)).findById(any(Long.class));
        utilities.verify(AuthUtil::getPrincipalUsername, times(1));
    }


    @Test
    @DisplayName("Service should delete comment - You are admin of group")
    public void shouldDeleteCommentGroupAdmin() {
        UserEntity user1 = new UserEntity();
        user1.setUsername(GenericValues.USERNAME);

        UserEntity user2 = new UserEntity();
        user2.setUsername(GenericValues.USER2_USERNAME);

        UserEntity user3 = new UserEntity();
        user3.setUsername("Bozidar");

        GroupEntity group = new GroupEntity();
        group.setAdmin(user2);

        PostEntity post = PostEntity
                .builder()
                .id(1L)
                .creator(user1)
                .group(group)
                .build();

        CommentEntity comment = CommentEntity
                .builder()
                .post(post)
                .id(1L)
                .creator(user3)
                .build();


        when(commentRepository.findById(any(Long.class))).thenReturn(Optional.of(comment));
        utilities.when(AuthUtil::getPrincipalUsername).thenReturn(GenericValues.USER2_USERNAME);

        commentService.deleteComment(1L);
        verify(commentRepository, times(1)).findById(any(Long.class));
        utilities.verify(AuthUtil::getPrincipalUsername, times(1));
    }

    @Test
    @DisplayName("Service should delete comment - You are creator of comment")
    public void shouldDeleteCommentCreatorComment() {
        UserEntity user = new UserEntity();
        user.setUsername(GenericValues.USERNAME);

        UserEntity user2 = new UserEntity();
        user2.setUsername(GenericValues.USER2_USERNAME);

        PostEntity post = PostEntity
                .builder()
                .id(1L)
                .creator(user)
                .build();

        CommentEntity comment = CommentEntity
                .builder()
                .post(post)
                .id(1L)
                .creator(user2)
                .build();

        when(commentRepository.findById(any(Long.class))).thenReturn(Optional.of(comment));
        utilities.when(AuthUtil::getPrincipalUsername).thenReturn(GenericValues.USER2_USERNAME);

        commentService.deleteComment(1L);
        verify(commentRepository, times(1)).findById(any(Long.class));
        utilities.verify(AuthUtil::getPrincipalUsername, times(1));
    }


}
