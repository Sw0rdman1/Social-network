package com.levi9.socialnetwork.controllers;

import static org.springframework.http.HttpStatus.OK;
import com.levi9.socialnetwork.service.friend.FriendService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RequestMapping("friendship")
@RestController
public class FriendshipController {
    FriendService friendService;
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteFriendship(@RequestBody String userToBeDeleted){
        friendService.remove(userToBeDeleted);
        return new ResponseEntity<>(OK);
    }
}
