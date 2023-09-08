package com.levi9.socialnetwork.controllers;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import com.levi9.socialnetwork.entity.PostEntity;
import com.levi9.socialnetwork.mapper.PostMapper;
import com.levi9.socialnetwork.request.HiddenPostRequest;
import com.levi9.socialnetwork.request.HiddenPostRequest;
import com.levi9.socialnetwork.request.PostRequest;
import com.levi9.socialnetwork.request.PostModificationRequest;
import com.levi9.socialnetwork.request.PostRequest;
import com.levi9.socialnetwork.response.PostResponse;
import com.levi9.socialnetwork.service.post.PostService;
import jakarta.validation.Valid;
import com.levi9.socialnetwork.service.user.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("posts")
@AllArgsConstructor
public class PostController {

    private final PostService postService;
    private UserService userService;
    private final PostMapper postMapper;

    @PostMapping
    public ResponseEntity<PostResponse> createPost(@RequestBody PostRequest postRequest) {
        PostEntity post = postService.createPost(postRequest);
        return new ResponseEntity<>(postMapper.toPostResponse(post), CREATED);
    }

    @PostMapping("/group/{groupID}")
    public ResponseEntity<PostResponse> createPostInGroup(@RequestBody PostRequest postRequest, @PathVariable Long groupID) {
        PostResponse response = postService.createPostInGroup(postRequest,groupID);
        return new ResponseEntity<>(response, CREATED);
    }

    @PutMapping
    public ResponseEntity<PostResponse> updatePost(@RequestBody PostModificationRequest updatePostRequest) {
        PostEntity updatedPost = postService.updatePost(updatePostRequest);
        return new ResponseEntity<>(postMapper.toPostResponse(updatedPost), OK);
    }

    @DeleteMapping("/{postID}")
    public ResponseEntity<Void> deletePost(@PathVariable final Long postID) {
        postService.deletePost(postID);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getNewsFeed(){
        List<PostResponse> feedPosts = postService.getFeedPosts();
        return new ResponseEntity<>(feedPosts, HttpStatus.OK);
    }

    @GetMapping("/profile/{profileUsername}")
    public ResponseEntity<List<PostResponse>> getUsersPosts(@PathVariable String profileUsername){
        List<PostResponse> usersProfilePosts = postService.getUsersProfilePosts(profileUsername);
        return new ResponseEntity<>(usersProfilePosts, HttpStatus.OK);
    }

    @PostMapping(value = "/hide")
    public ResponseEntity<String> hidePost(@RequestBody @Valid HiddenPostRequest hiddenPostRequest) {
        String hiddenPostResponse = postService.hidePostFromUsers(hiddenPostRequest);
        return new ResponseEntity<>(hiddenPostResponse, OK);
    }

    @PostMapping(value = "/unhide")
    public ResponseEntity<String> unhidePost(@RequestBody @Valid HiddenPostRequest hiddenPostRequest){
        String hiddenPostResponse = postService.unhidePostFromUsers(hiddenPostRequest);
        return new ResponseEntity<>(hiddenPostResponse, CREATED);
    }
}
