package com.levi9.socialnetwork.mapper;

import com.levi9.socialnetwork.entity.GroupRequestEntity;
import com.levi9.socialnetwork.response.GroupRequestResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface GroupRequestMapper {
    @Mapping(source = "group.admin.username", target = "sender")
    @Mapping(source = "user.username", target = "receiver")
    @Mapping(source = "group.name", target = "group")
    GroupRequestResponse mapGroupRequestEntityToGroupRequestResponseSentByAdmin(GroupRequestEntity groupRequest);

    @Mapping(source = "group.admin.username", target = "receiver")
    @Mapping(source = "user.username", target = "sender")
    @Mapping(source = "group.name", target = "group")
    GroupRequestResponse mapGroupRequestEntityToGroupRequestResponseSentByUser(GroupRequestEntity groupRequest);

}
