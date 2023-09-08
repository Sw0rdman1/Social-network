package com.levi9.socialnetwork.controllers;

import static org.springframework.http.HttpStatus.OK;
import com.levi9.socialnetwork.entity.GroupMemberEntity;
import com.levi9.socialnetwork.entity.GroupRequestEntity;
import com.levi9.socialnetwork.mapper.GroupMemberMapper;
import com.levi9.socialnetwork.mapper.GroupRequestMapper;
import com.levi9.socialnetwork.request.ApproveOrDeclineGroupRequest;
import com.levi9.socialnetwork.response.GroupRequestResponse;
import com.levi9.socialnetwork.service.groupmember.GroupMemberService;
import com.levi9.socialnetwork.service.grouprequest.GroupRequestService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("admin")
@AllArgsConstructor
public class AdminController {

    private final GroupRequestService groupRequestService;
    private final GroupMemberService groupMemberService;
    private final GroupMemberMapper groupMemberMapper;
    private final GroupRequestMapper groupRequestMapper;

    @GetMapping("requests/{groupId}")
    public ResponseEntity<List<GroupRequestResponse>> listAllPendingGroupRequests(@PathVariable int groupId) {
        List<GroupRequestResponse> groupRequests = groupRequestService.finAllGroupRequestsByGroupId((long) groupId);
        return new ResponseEntity<>(groupRequests, HttpStatus.OK);
    }
    @PostMapping("requests")
    public ResponseEntity<Object> approveOrDeclineGroupRequests(@RequestBody ApproveOrDeclineGroupRequest request) {
        Long requestId = request.getRequestId();
        if(request.isApprove()){
            GroupMemberEntity userJoined =  groupMemberService.adminApproveByRequestId(requestId);
            return new ResponseEntity<>(groupMemberMapper.toGroupMemberResponse(userJoined),OK);
        }
        else{
            GroupRequestEntity groupRequest = groupRequestService.adminChangeRequestToRejected(requestId);
            return new ResponseEntity<>(groupRequestMapper
                    .mapGroupRequestEntityToGroupRequestResponseSentByUser(groupRequest),OK);
        }
    }
}