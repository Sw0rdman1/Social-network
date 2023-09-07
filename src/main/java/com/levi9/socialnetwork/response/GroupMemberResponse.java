package com.levi9.socialnetwork.response;

import com.levi9.socialnetwork.entity.GroupEntity;
import com.levi9.socialnetwork.entity.UserEntity;
import lombok.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class GroupMemberResponse {

    private Long id;
    private UserEntity groupMember;
    private GroupEntity group;
    private LocalDateTime dateTimeJoined;
}