package com.levi9.socialnetwork.mapper;

import com.levi9.socialnetwork.entity.GroupMemberEntity;
import com.levi9.socialnetwork.response.GroupMemberResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class GroupMemberMapper {
    public GroupMemberResponse toGroupMemberResponse(GroupMemberEntity groupMember) {
        return createGroupMemberResponse(groupMember);
    }

    private GroupMemberResponse createGroupMemberResponse(GroupMemberEntity groupMember) {
        return GroupMemberResponse.builder()
                .id(groupMember.getId())
                .groupMember(groupMember.getMember())
                .group(groupMember.getGroup())
                .dateTimeJoined(groupMember.getDateTimeJoined())
                .build();
    }
}