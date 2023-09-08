package com.levi9.socialnetwork.service.post;

import com.levi9.socialnetwork.entity.*;
import com.levi9.socialnetwork.entity.FriendshipEntity;
import com.levi9.socialnetwork.entity.PostEntity;
import com.levi9.socialnetwork.entity.PostHiddenFromEntity;
import com.levi9.socialnetwork.entity.UserEntity;
import com.levi9.socialnetwork.exception.customexception.EmptyPostException;
import com.levi9.socialnetwork.exception.customexception.EntityNotFoundException;
import com.levi9.socialnetwork.exception.customexception.PostException;
import com.levi9.socialnetwork.exception.customexception.PostNoPermissionException;
import com.levi9.socialnetwork.mapper.PostMapper;
import com.levi9.socialnetwork.repository.*;
import com.levi9.socialnetwork.request.HiddenPostRequest;
import com.levi9.socialnetwork.repository.FriendshipRepository;
import com.levi9.socialnetwork.repository.PostHiddenFromRepository;
import com.levi9.socialnetwork.repository.PostRepository;
import com.levi9.socialnetwork.repository.UserRepository;
import com.levi9.socialnetwork.request.HiddenPostRequest;
import com.levi9.socialnetwork.request.PostModificationRequest;
import com.levi9.socialnetwork.request.PostRequest;
import com.levi9.socialnetwork.response.PostResponse;
import com.levi9.socialnetwork.service.group.GroupService;
import com.levi9.socialnetwork.service.groupmember.GroupMemberService;
import com.levi9.socialnetwork.service.user.UserService;
import com.levi9.socialnetwork.util.AuthUtil;
import com.levi9.socialnetwork.util.GenericMessages;
import com.levi9.socialnetwork.util.notifications.MailServiceImpl;
import com.levi9.socialnetwork.util.notifications.Notification;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final FriendshipRepository friendshipRepository;
    private final PostHiddenFromRepository postHiddenFromRepository;
    private final PostMapper postMapper;
    private final UserService userService;
    private final GroupService groupService;
    private final GroupMemberService groupMemberService;
    private final GroupMemberRepository groupMemberRepository;
    private final MailServiceImpl emailService;

    @Override
    public PostEntity createPost(PostRequest postRequest) {

        if (postRequest.getText().isBlank()) {
            throw new EmptyPostException(GenericMessages.ERROR_MESSAGE_EMPTY_POST);
        }


        String currentUser = AuthUtil.getPrincipalUsername();

        UserEntity user = userService.findByUsername(currentUser);

        PostEntity newPost = PostEntity.builder()
                .text(postRequest.getText())
                .dateTimeCreated(LocalDateTime.now())
                .closed(postRequest.isClosed())
                .deleted(false)
                .creator(user)
                .build();
        return savePost(newPost);
    }

    @Override
    public PostResponse createPostInGroup(PostRequest postRequest, Long groupID) {
        if (postRequest.getText().isBlank()) {
            throw new EmptyPostException(GenericMessages.ERROR_MESSAGE_EMPTY_POST);
        }
        GroupEntity group = groupService.findById(groupID);
        String currentUser = AuthUtil.getPrincipalUsername();
        UserEntity user = userService.findByUsername(currentUser);
        groupMemberRepository.findByMemberAndGroup(user, group)
                .orElseThrow(() -> new PostNoPermissionException(
                        String.format(GenericMessages.ERROR_MESSAGE_NOT_PART_OF_GROUP, group.getName())));
        PostEntity newPost = PostEntity.builder()
                .text(postRequest.getText())
                .dateTimeCreated(LocalDateTime.now())
                .closed(true)
                .deleted(false)
                .group(group)
                .creator(user)
                .build();
        PostEntity savedPost = savePost(newPost);
        sendNotification(groupMemberService.findUserEmailsByGroupId(groupID),
                String.format(GenericMessages.EMAIL_NEW_POST_SUBJECT, group.getName()),
                String.format(GenericMessages.EMAIL_NEW_POST_BODY, user.getUsername(), savedPost.getText()),
                user);

        return postMapper.toPostResponse(savedPost);
    }
    private void sendNotification(List<String> userEmails, String subject, String body, UserEntity currentUser) {
        userEmails.forEach((email) -> {
            if (!email.equals(currentUser.getEmail())) {
                emailService.sendMail(new Notification(email, subject, body));
            }
        });
    }

    @Override
    public PostEntity savePost(PostEntity post) {
        return postRepository.save(post);
    }

    @Override
    public PostEntity updatePost(PostModificationRequest postModificationRequest) {

        PostEntity post = postRepository
                .findById(postModificationRequest.getId())
                .orElseThrow(() -> new EntityNotFoundException(String.format(GenericMessages.ERROR_MESSAGE_POST_DOES_NOT_EXIST, postModificationRequest.getId())));

        if (post.isDeleted()) {
            throw new PostException(String.format(GenericMessages.ERROR_MESSAGE_POST_EXPIRED, post.getId()));
        }

        String postModifierId = AuthUtil.getPrincipalId();
        if (!postModifierId.equals(post.getCreator().getId())) {
            throw new PostException(String.format(GenericMessages.ERROR_MESSAGE_POST_CAN_BE_CHANGED_ONLY_BY_OWNER, post.getId()));
        }

        post.setText(postModificationRequest.getText());
        post.setClosed(postModificationRequest.isClosed());

        return postRepository.save(post);
    }

    @Override
    public void deletePost(Long postID) {
        String currentUser = AuthUtil.getPrincipalUsername();
        PostEntity post = postRepository.findById(postID)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(GenericMessages.ERROR_MESSAGE_POST_NOT_EXIST, postID)));

        if (!checkIfUserHavePermission(currentUser, post)) {
            throw new PostNoPermissionException(GenericMessages.ERROR_MESSAGE_POST_NO_PERMISSION);
        }

        postRepository.delete(post);
    }

    private boolean checkIfUserHavePermission(String username, PostEntity post) {
        return (post.getCreator().getUsername().equals(username) ||
                (post.getGroup() != null && post.getGroup().getAdmin().getUsername().equals(username)));
    }

    @Override
    public List<PostResponse> getFeedPosts() {
        String loggedUserId = AuthUtil.getPrincipalId();
        List<String> friendIds = friendshipRepository.findUserFriends(loggedUserId);
        friendIds.add(loggedUserId);
        List<Long> postsHiddenFromLoggedUserIds = postHiddenFromRepository.findByUserId(loggedUserId).stream().map(PostHiddenFromEntity::getPost)
                .map(PostEntity::getId).toList();
        List<PostEntity> feed = postRepository.findByCreatorIdInAndIdNotInOrderByDateTimeCreatedDesc(friendIds, postsHiddenFromLoggedUserIds);
        return feed.stream().map(postMapper::toPostResponse).toList();
    }

    @Override
    public List<PostResponse> getUsersProfilePosts(String profileUsername) {
        String loggedUserId = AuthUtil.getPrincipalId();
        String profileId = userRepository
                .findByUsername(profileUsername)
                .orElseThrow(() -> new EntityNotFoundException(String.format(GenericMessages.ERROR_MESSAGE_USER_NOT_FOUND, profileUsername)))
                .getId();
        Optional<List<PostEntity>> feed;

        if (profileId.equals(loggedUserId)) {
            feed = Optional.ofNullable(postRepository.findByCreatorIdInAndIdNotInOrderByDateTimeCreatedDesc(loggedUserId.lines().toList(), Stream.of(Long.MIN_VALUE).collect(Collectors.toList())));
            return feed.map(postEntities -> postEntities.stream().map(postMapper::toPostResponse).toList()).orElse(null);
        }

        List<Long> postsHiddenFromLoggedUserIds = postHiddenFromRepository.findByUserId(loggedUserId).stream().map(PostHiddenFromEntity::getPost)
                .map(PostEntity::getId).toList();

        if (friendshipRepository.findByPairOfIds(loggedUserId, profileId).isPresent()) {
            feed = Optional.ofNullable(postRepository.findByCreatorIdInAndIdNotInOrderByDateTimeCreatedDesc(profileId.lines().toList(), postsHiddenFromLoggedUserIds));
        } else {
            feed = Optional.ofNullable(postRepository.findByCreatorIdAndIdNotInAndClosedIsFalseOrderByDateTimeCreatedDesc(profileId, postsHiddenFromLoggedUserIds));
        }

        return feed.map(postEntities -> postEntities.stream().map(postMapper::toPostResponse).toList()).orElse(null);
    }

        @Transactional
        @Override
        public String hidePostFromUsers(HiddenPostRequest hiddenPostRequest) {

            Long postId = hiddenPostRequest.getPostId();
            PostEntity post = postRepository
                    .findById(postId)
                    .orElseThrow(() -> new EntityNotFoundException(
                            String.format(GenericMessages.ERROR_MESSAGE_POST_NOT_EXIST, postId)));

            if (post.isDeleted()) {
                throw new PostException(String.format(GenericMessages.ERROR_MESSAGE_POST_EXPIRED, postId));
            }

            String postModifierId = AuthUtil.getPrincipalId();
            if (!postModifierId.equals(post.getCreator().getId())) {
                throw new PostException(GenericMessages.ERROR_MESSAGE_HIDE_POST_ALLOWED_ONLY_TO_OWNER);
            }

            if (post.getGroup() != null) {
                throw new PostException(GenericMessages.ERROR_MESSAGE_HIDE_POST_NOT_ALLOWED);
            }

            List<String> usernames = hiddenPostRequest.getUsernames();
            for (String username : usernames) {
                UserEntity user = userRepository
                        .findByUsername(username)
                        .orElseThrow(() -> new EntityNotFoundException(String.format(GenericMessages.ERROR_MESSAGE_USER_NOT_FOUND, username)));

                if (user.getId().equals(postModifierId)) {
                    throw new PostException(GenericMessages.ERROR_MESSAGE_HIDE_POST_FROM_OWNER_NOT_ALLOWED);
                }

                if (post.isClosed() && friendshipRepository.findByPairOfIds(postModifierId, user.getId()).isEmpty()) {
                        throw new PostException(GenericMessages.ERROR_MESSAGE_POST_CAN_BE_HIDDEN_ONLY_FROM_FRIENDS);
                }

                if (!postHiddenFromRepository.findByUserIdAndPostId(user.getId(), postId).isEmpty()) {
                    throw new PostException(String.format(GenericMessages.ERROR_MESSAGE_POST_ALREADY_HIDDEN_FROM_USER, username));
                }

                PostHiddenFromEntity postHiddenFromEntity = new PostHiddenFromEntity();
                postHiddenFromEntity.setUser(user);
                postHiddenFromEntity.setPost(post);

                postHiddenFromRepository.save(postHiddenFromEntity);
            }

            return GenericMessages.SUCCESS_MESSAGE_POST_HIDDEN;
        }

    @Transactional
    @Override
    public String unhidePostFromUsers(HiddenPostRequest hiddenPostRequest) {
        PostEntity post = postRepository
                .findById(hiddenPostRequest.getPostId())
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(GenericMessages.ERROR_MESSAGE_POST_NOT_EXIST, hiddenPostRequest.getPostId())));
        if (post.isDeleted()) {
            throw new PostException(String.format(GenericMessages.ERROR_MESSAGE_POST_EXPIRED, post.getId()));
        }
        String postModifierId = AuthUtil.getPrincipalId();
        if (!postModifierId.equals(post.getCreator().getId())) {
            throw new PostException(GenericMessages.ERROR_MESSAGE_HIDE_POST_ALLOWED_ONLY_TO_OWNER);
        }
        if (post.getGroup() != null) {
            throw new PostException(GenericMessages.ERROR_MESSAGE_HIDE_POST_NOT_ALLOWED);
        }
        List<String> usernames = hiddenPostRequest.getUsernames();
        for (String username : usernames) {
            UserEntity user = userRepository
                    .findByUsername(username)
                    .orElseThrow(() -> new EntityNotFoundException(String.format(GenericMessages.ERROR_MESSAGE_USER_NOT_FOUND, username)));
            if (post.isClosed() || user.getId().equals(postModifierId)) {
                friendshipRepository
                        .findByPairOfIds(postModifierId, user.getId())
                        .orElseThrow(() -> new PostException(GenericMessages.ERROR_MESSAGE_POST_CAN_BE_HIDDEN_ONLY_FROM_FRIENDS));
            }
            List<PostHiddenFromEntity> postHiddenFrom = postHiddenFromRepository.findByUserIdAndPostId(user.getId(), post.getId());
            if (postHiddenFrom.isEmpty()) {
                throw new PostException(String.format(GenericMessages.ERROR_MESSAGE_POST_IS_NOT_HIDDEN_FROM_USER, username));
            }
            postHiddenFromRepository.delete(postHiddenFrom.get(0));
        }
        return GenericMessages.SUCCESS_MESSAGE_POST_IS_UNHIDDEN;
    }
}