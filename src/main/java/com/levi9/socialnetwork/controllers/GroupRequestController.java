package com.levi9.socialnetwork.controllers;

import com.levi9.socialnetwork.entity.GroupMemberEntity;
import com.levi9.socialnetwork.entity.GroupRequestEntity;
import com.levi9.socialnetwork.mapper.GroupMemberMapper;
import com.levi9.socialnetwork.mapper.GroupRequestMapper;
import com.levi9.socialnetwork.request.ApproveOrDeclineGroupRequest;
import com.levi9.socialnetwork.request.GroupRequestToUserRequest;
import com.levi9.socialnetwork.response.GroupRequestResponse;
import com.levi9.socialnetwork.service.groupmember.GroupMemberService;
import com.levi9.socialnetwork.service.grouprequest.GroupRequestService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@AllArgsConstructor
@RequestMapping("groups")
@RestController
public class GroupRequestController {

    private final GroupRequestService groupRequestService;
    private final GroupMemberService groupMemberService;
    private final GroupMemberMapper groupMemberMapper;
    private final GroupRequestMapper groupRequestMapper;

    @PostMapping(value = "/{groupId}/sendRequest")
    public ResponseEntity<GroupRequestResponse> sendRequestToUser(@PathVariable Long groupId,
                                                                  @RequestBody GroupRequestToUserRequest groupRequestToUserRequest) {
        GroupRequestResponse sendGroupRequestToUserResponse = groupRequestService
                .sendGroupRequestToUser(groupRequestToUserRequest.getUsername(), groupId);
        return new ResponseEntity<>(sendGroupRequestToUserResponse, HttpStatus.CREATED);
    }

    @GetMapping("requests")
    public ResponseEntity<List<GroupRequestResponse>> listAllPendingGroupRequests() {
        List<GroupRequestResponse> groupRequests = groupRequestService.findByUserId();
        return new ResponseEntity<>(groupRequests, HttpStatus.OK);
    }

    @PostMapping(value = "/{groupId}/joinPrivateGroup")
    public ResponseEntity<GroupRequestResponse> joinPrivateGroup(@PathVariable Long groupId) {
        GroupRequestResponse requestToJoinGroupResponse = groupRequestService
                .sendRequestToJoinGroup(groupId);
        return new ResponseEntity<>(requestToJoinGroupResponse, HttpStatus.CREATED);
    }

    @PostMapping("requests")
    public ResponseEntity<Object> approveOrDeclineGroupRequests(@RequestBody ApproveOrDeclineGroupRequest request) {
        Long requestId = request.getRequestId();
        if (request.isApprove()) {
            GroupMemberEntity userJoined = groupMemberService.userApproveByRequestId(requestId);
            return new ResponseEntity<>(groupMemberMapper.toGroupMemberResponse(userJoined), HttpStatus.OK);
        } else {
            GroupRequestEntity groupRequest = groupRequestService.userChangeRequestToRejected(requestId);
            return new ResponseEntity<>(groupRequestMapper
                    .mapGroupRequestEntityToGroupRequestResponseSentByAdmin(groupRequest), HttpStatus.OK);
        }
    }
}