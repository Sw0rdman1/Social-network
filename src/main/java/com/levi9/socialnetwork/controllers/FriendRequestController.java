package com.levi9.socialnetwork.controllers;

import com.levi9.socialnetwork.response.FriendRequestResponse;
import com.levi9.socialnetwork.service.friend.FriendService;
import com.levi9.socialnetwork.service.friendRequest.FriendRequestService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@AllArgsConstructor
@RequestMapping("friendRequests")
@RestController
public class FriendRequestController {

    private final FriendRequestService friendRequestService;
    private FriendService friendService;

    @PostMapping
    public ResponseEntity<String> sendRequest(@RequestBody String receiverUsername) {

        String createFriendResponse = friendRequestService.createFriendRequest(receiverUsername);

        return new ResponseEntity<>(createFriendResponse, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<FriendRequestResponse>> getPendingFriendRequests() {
        List<FriendRequestResponse> friendRequests = friendRequestService.getPendingFriendRequests();
        return new ResponseEntity<>(friendRequests, HttpStatus.OK);
    }

    @PostMapping(value = "/accept")
    public ResponseEntity<String> acceptFriendRequest(@RequestBody String senderUsername) {
        String friendRequestResultResponse = friendRequestService.acceptFriendRequest(senderUsername);
        return new ResponseEntity<>(friendRequestResultResponse, HttpStatus.CREATED);
    }

    @PutMapping(value = "/decline")
    public ResponseEntity<String> declineFriendRequest(@RequestBody String senderUsername) {
        String friendRequestResultResponse = friendRequestService.declineFriendRequest(senderUsername);
        return new ResponseEntity<>(friendRequestResultResponse, HttpStatus.OK);
    }
}
