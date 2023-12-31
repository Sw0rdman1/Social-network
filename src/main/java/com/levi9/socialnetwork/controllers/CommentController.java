package com.levi9.socialnetwork.controllers;

import static org.springframework.http.HttpStatus.CREATED;

import com.levi9.socialnetwork.entity.CommentEntity;
import com.levi9.socialnetwork.request.CommentRequest;
import com.levi9.socialnetwork.request.ReplyToCommentRequest;
import com.levi9.socialnetwork.response.CommentResponse;
import com.levi9.socialnetwork.service.comment.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("comments")
@AllArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{postID}")
    public ResponseEntity<CommentEntity> createComment(@PathVariable final Long postID , @RequestBody CommentRequest commentRequest) {
        CommentEntity response = commentService.createComment(commentRequest, postID);
        return new ResponseEntity<>(response, CREATED);
    }

    @DeleteMapping("/{commentID}")
    public ResponseEntity<Void> commentID(@PathVariable final Long commentID) {
        commentService.deleteComment(commentID);
        return ResponseEntity.ok().build();
    }

}
