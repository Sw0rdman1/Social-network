package com.levi9.socialnetwork.mapper;

import com.levi9.socialnetwork.entity.EventInvitationEntity;
import com.levi9.socialnetwork.response.EventInvitationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface EventInvitationMapper {
    @Mapping(source = "invitee.member.username", target = "invitee")
    EventInvitationResponse mapEventInvitationEntityToEventInvitationResponse(EventInvitationEntity eventInvitation);

}
