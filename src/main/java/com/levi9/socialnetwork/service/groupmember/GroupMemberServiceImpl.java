package com.levi9.socialnetwork.service.groupmember;

import com.levi9.socialnetwork.entity.GroupEntity;
import com.levi9.socialnetwork.entity.GroupMemberEntity;
import com.levi9.socialnetwork.entity.GroupRequestEntity;
import com.levi9.socialnetwork.entity.UserEntity;
import com.levi9.socialnetwork.exception.customexception.EntityNotFoundException;
import com.levi9.socialnetwork.exception.customexception.GroupRequestException;
import com.levi9.socialnetwork.repository.GroupMemberRepository;
import com.levi9.socialnetwork.repository.GroupRequestRepository;
import com.levi9.socialnetwork.service.user.UserService;
import com.levi9.socialnetwork.util.AuthUtil;
import com.levi9.socialnetwork.util.GenericMessages;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class GroupMemberServiceImpl implements GroupMemberService {

    private final GroupMemberRepository groupMemberRepository;
    private final GroupRequestRepository groupRequestRepository;
    private final UserService userService;

    @Override
    public GroupMemberEntity findByMemberAndGroup(UserEntity member, GroupEntity group) {
        return groupMemberRepository.findByMemberAndGroup(member, group).
                orElseThrow(() -> new EntityNotFoundException(String
                        .format(GenericMessages.ERROR_MESSAGE_USER_NOT_FOUND_IN_GROUP, member.getUsername(),
                                group.getName())));
    }

    @Override
    public boolean groupMemberExists(UserEntity member, GroupEntity group) {
        return groupMemberRepository.findByMemberAndGroup(member, group).isPresent();
    }

    @Override
    public GroupMemberEntity adminApproveByRequestId(Long requestId) {
        UserEntity allegedAdmin = userService.findById(AuthUtil.getPrincipalId());
        GroupRequestEntity groupRequest = groupRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException(String
                        .format(GenericMessages.ERROR_MESSAGE_GROUP_REQUEST_NOT_FOUND, requestId)));
        GroupEntity group = groupRequest.getGroup();
        if(!allegedAdmin.equals(group.getAdmin()))
            throw new GroupRequestException(String
                    .format(GenericMessages.ERROR_MESSAGE_NOT_A_GROUP_ADMIN, group.getName()));
        return approveRequest(groupRequest);
    }

    @Override
    public GroupMemberEntity userApproveByRequestId(Long requestId) {
        UserEntity allegedUser = userService.findById(AuthUtil.getPrincipalId());
        GroupRequestEntity groupRequest = groupRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException(String
                        .format(GenericMessages.ERROR_MESSAGE_GROUP_REQUEST_NOT_FOUND, requestId)));
        if(!allegedUser.equals(groupRequest.getUser()))
            throw new GroupRequestException(GenericMessages.ERROR_MESSAGE_NOT_A_REQUEST_OWNER);
        return approveRequest(groupRequest);
    }

    @Override
    @Transactional
    public GroupMemberEntity approveRequest(GroupRequestEntity groupRequest) {
        UserEntity user = groupRequest.getUser();
        GroupEntity group = groupRequest.getGroup();

        GroupMemberEntity member = GroupMemberEntity.builder()
                .member(user)
                .group(group)
                .dateTimeJoined(LocalDateTime.now())
                .build();
        GroupMemberEntity savedMember = groupMemberRepository.save(member);
        groupRequestRepository.delete(groupRequest);
        return savedMember;
    }

    @Override
    public GroupMemberEntity save(GroupMemberEntity groupMember) {
        return groupMemberRepository.save(groupMember);
    }
    @Override
    public List<String> findUserEmailsByGroupId(Long groupID) {
        return groupMemberRepository.findMemberEmailsByGroupId(groupID);
    }

    @Override
    public List<GroupMemberEntity> getGroupMembers(GroupEntity group) {
        return groupMemberRepository.findByGroup(group).stream().toList();
    }
}
