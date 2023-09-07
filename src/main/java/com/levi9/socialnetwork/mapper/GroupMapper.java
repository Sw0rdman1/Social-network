package com.levi9.socialnetwork.mapper;

import com.levi9.socialnetwork.entity.GroupEntity;
import com.levi9.socialnetwork.response.GroupResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper
public interface GroupMapper {

    GroupResponse mapGroupEntityToGroupResponse(GroupEntity group);

    @Named(value = "mapGroupEntityToGroupResponseForEventResponse")
    @Mapping(target = "closed", ignore = true)
    @Mapping(target = "admin", ignore = true)
    GroupResponse mapGroupEntityToGroupResponseForEventResponse(GroupEntity group);
}