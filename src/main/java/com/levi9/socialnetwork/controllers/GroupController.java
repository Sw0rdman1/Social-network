package com.levi9.socialnetwork.controllers;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import com.levi9.socialnetwork.request.CreateGroupRequest;
import com.levi9.socialnetwork.response.GroupResponse;
import com.levi9.socialnetwork.response.PostResponse;
import com.levi9.socialnetwork.service.group.GroupService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("groups")
@AllArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @PostMapping
    public ResponseEntity<GroupResponse> createGroup(@RequestBody @Valid CreateGroupRequest createGroupRequest) {
        GroupResponse group = groupService.createGroup(createGroupRequest.getGroupName(), createGroupRequest.getClosed());
        return new ResponseEntity<>(group, CREATED);
    }

    @PostMapping(value = "/{groupId}/leave")
    public ResponseEntity<Void> leaveGroup(@PathVariable int groupId) {
        groupService.leaveGroup(groupId);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/{groupId}/removeMember")
    public ResponseEntity<Void> removeMember(@PathVariable int groupId, @RequestBody String userToBeRemoved)
    {
        groupService.removeMember(groupId,userToBeRemoved);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/{groupId}/joinPublicGroup")
    public ResponseEntity<Void> joinPublicGroup(@PathVariable int groupId) {
        groupService.joinPublicGroup(groupId);
        return new ResponseEntity<>(OK);
    }

    @GetMapping(value = "/{groupId}/listPosts")
    public ResponseEntity<List<PostResponse>> listPosts(@PathVariable int groupId) {
        List<PostResponse> responses = groupService.showPostsInGroup(groupId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping(value = {"/{searchCriteria}", "/"})
    public ResponseEntity<List<GroupResponse>> searchGroupsByName(@PathVariable(required = false) String searchCriteria){
        List<GroupResponse> searchResult = groupService.searchGroupsByNameContaining(searchCriteria);
        return new ResponseEntity<>(searchResult, HttpStatus.OK);
    }

    @DeleteMapping("/{groupName}")
    public ResponseEntity<Void> deleteGroup(@PathVariable String groupName){
        groupService.deleteGroup(groupName);
        return ResponseEntity.ok().build();
    }
}
