package com.levi9.socialnetwork.mapper;

import com.levi9.socialnetwork.entity.EventEntity;
import com.levi9.socialnetwork.response.EventResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {GroupMapper.class})
public interface EventMapper {

    @Mapping(source = "group", target = "group", qualifiedByName = "mapGroupEntityToGroupResponseForEventResponse")
    @Mapping(source = "creator.member.username", target = "creatorUsername")
    EventResponse mapEventEntityToEventResponse(EventEntity event);
}