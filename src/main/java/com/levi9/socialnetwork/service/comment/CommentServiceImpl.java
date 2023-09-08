package com.levi9.socialnetwork.service.comment;

import com.levi9.socialnetwork.entity.CommentEntity;
import com.levi9.socialnetwork.entity.GroupEntity;
import com.levi9.socialnetwork.entity.PostEntity;
import com.levi9.socialnetwork.entity.UserEntity;
import com.levi9.socialnetwork.exception.customexception.*;
import com.levi9.socialnetwork.mapper.CommentMapper;
import com.levi9.socialnetwork.repository.CommentRepository;
import com.levi9.socialnetwork.repository.FriendshipRepository;
import com.levi9.socialnetwork.repository.GroupMemberRepository;
import com.levi9.socialnetwork.repository.PostRepository;
import com.levi9.socialnetwork.request.CommentRequest;
import com.levi9.socialnetwork.response.CommentResponse;
import com.levi9.socialnetwork.service.user.UserService;
import com.levi9.socialnetwork.entity.CommentReplyEntity;
import com.levi9.socialnetwork.exception.customexception.EntityNotFoundException;
import com.levi9.socialnetwork.repository.*;
import com.levi9.socialnetwork.util.AuthUtil;
import com.levi9.socialnetwork.util.GenericMessages;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final UserService userService;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final CommentMapper commentMapper;
    private final GroupMemberRepository groupMemberRepository;
    private final FriendshipRepository friendshipRepository;
    private final UserRepository userRepository;
    private final PostHiddenFromRepository postHiddenFromRepository;
    private final CommentReplyRepository commentReplyRepository;


    /**
     * @throws EmptyCommentException ako je komentar prazan string
     * @throws EntityNotFoundException ako ne postoji post na koji zelimo odgovoriti
     * @throws PostException ako je post istekao nakon 24 casa od njegovog kreiranja
     * */
    @Override
    public CommentResponse createComment(CommentRequest commentRequest, Long postID) {
        if (commentRequest.text().isBlank()) {
            throw new EmptyCommentException(GenericMessages.ERROR_MESSAGE_EMPTY_COMMENT);
        }

        PostEntity postCommented = postRepository.findById(postID)
                .orElseThrow(() -> new EntityNotFoundException
                        (String.format(GenericMessages.ERROR_MESSAGE_POST_DONT_EXIST, postID)));

        if (postCommented.isDeleted()) {
            throw new PostException(String.format(GenericMessages.ERROR_MESSAGE_POST_EXPIRED_CREATE_COMMENT, postID));
        }

        String currentUser = AuthUtil.getPrincipalUsername();
        UserEntity user = userService.findByUsername(currentUser);

        checkGroupCredentials(postCommented.getGroup(), user);

        checkPostPrivacy(postCommented, user);

        CommentEntity newComment = CommentEntity.builder()
                .text(commentRequest.text())
                .dateTimeCreated(LocalDateTime.now())
                .post(postCommented)
                .creator(user)
                .build();

        return commentMapper.mapCommentEntityToCommentResponse(saveComment(newComment));
    }


    /**
     * Proverava da li user ima pristup grupi
     * @throws EntityNotFoundException ako user nije clan grrupe u kojoj zeli ostaviti komentar
     */
    private void checkGroupCredentials(GroupEntity group, UserEntity user) {
        if (group != null) {
            groupMemberRepository.findByMemberAndGroup(user, group).
                    orElseThrow(() -> new EntityNotFoundException
                            (GenericMessages.ERROR_MESSAGE_GROUP_NOT_PART_OF_COMMENT));
        }
    }

    /**
     * Proverava da li user moze videti post
     * @throws EntityNotFoundException ako user ne moze videti posti na koji zeli ostaviti komentar
     */
    private void checkPostPrivacy(PostEntity post, UserEntity user) {
        if (post.isClosed() && !post.getCreator().getId().equals(user.getId())) {
            friendshipRepository.findByPairOfIds(user.getId(), post.getCreator().getId()).
                    orElseThrow(() -> new EntityNotFoundException
                            (GenericMessages.ERROR_MESSAGE_COMMENT_NO_FRIENDS));

        }
    }

    @Override
    public CommentEntity saveComment(CommentEntity comment) {
        return commentRepository.save(comment);
    }


    /**
     *
     * @throws EntityNotFoundException ako komentar sa unetim ID-ijem ne postoji
     */
    @Override
    public void deleteComment(Long commentID) {
        String currentUser = AuthUtil.getPrincipalUsername();
        CommentEntity comment = commentRepository.findById(commentID)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(GenericMessages.ERROR_MESSAGE_COMMENT_DONT_EXIST, commentID)));

        if (comment.getPost().isDeleted()) {
            throw new PostException(String.format(GenericMessages.ERROR_MESSAGE_POST_EXPIRED_DELETE_COMMENT, comment.getPost().getId()));
        }

        if (!checkIfUserHavePermission(currentUser, comment)) {
            throw new CommentNoPermissionException(GenericMessages.ERROR_MESSAGE_COMMENT_NO_PERMISSION);
        }

        commentRepository.delete(comment);
    }


    /**
     * Proverava da li user ima permisije da izbrise kometnar
     * @param username user koji zeli brisati komentar
     * @param comment sam komentar
     * @return true ako je on kreator posta, admin grupa ako je post u grupi ili je kreator samog komentara, u svim ostalim slucajevima je false
     */
    private boolean checkIfUserHavePermission(String username, CommentEntity comment) {
        return (comment.getCreator().getUsername().equals(username) ||
                comment.getPost().getCreator().getUsername().equals(username) ||
                (comment.getPost().getGroup() != null && comment.getPost().getGroup().getAdmin().getUsername().equals(username)));
    }


    /**
     * Odgovara na postojeći komentar.
     *
     * @param commentId ID komentara na koji se odgovara.
     * @param text      Tekst odgovora koji se dodaje na komentar.
     * @throws EntityNotFoundException       Bacanje izuzetka ako komentar s datim ID-om nije pronađen.
     * @throws IllegalStateException        Bacanje izuzetka ako postoje određeni uslovi koji sprečavaju dodavanje odgovora.
     */
    @Override
    public void reply(Long commentId, String text) {
        CommentReplyEntity reply = new CommentReplyEntity();
        CommentEntity rootComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException(GenericMessages.ERROR_MESSAGE_COMMENT_NOT_FOUND));

        if (rootComment.getPost().getGroup() == null) {
            if (postHiddenFromRepository.findByUserIdAndPostId(AuthUtil.getPrincipalId(), rootComment.getId()) != null) {
                throw new IllegalStateException(GenericMessages.ERROR_MESSAGE_POST_IS_HIDDEN);
            }
            if (rootComment.getPost().isClosed()) {
                if (friendshipRepository.findByPairOfIds(rootComment.getPost().getCreator().getId(), AuthUtil.getPrincipalId()).isEmpty()) {
                    throw new IllegalStateException(GenericMessages.ERROR_MESSAGE_CANNOT_REPLY);
                }
            }
        } else {
            if (groupMemberRepository.findByMemberAndGroup(userRepository.findById(AuthUtil.getPrincipalId()).get(), rootComment.getPost().getGroup()).isEmpty()) {
                throw new IllegalStateException(GenericMessages.ERROR_MESSAGE_NOT_A_MEMBER);
            }
        }
        reply.setComment(rootComment);
        reply.setText(text);
        reply.setCreator(userRepository.findById(AuthUtil.getPrincipalId()).get());
        reply.setDateTimeCreated(LocalDateTime.now());

        commentReplyRepository.save(reply);
    }

}