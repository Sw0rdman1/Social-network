package com.levi9.socialnetwork.service.event;

import com.levi9.socialnetwork.entity.EventEntity;
import com.levi9.socialnetwork.exception.customexception.*;
import com.levi9.socialnetwork.repository.EventRepository;
import com.levi9.socialnetwork.entity.GroupEntity;
import com.levi9.socialnetwork.entity.GroupMemberEntity;
import com.levi9.socialnetwork.entity.UserEntity;
import com.levi9.socialnetwork.mapper.EventMapper;
import com.levi9.socialnetwork.repository.GroupRepository;
import com.levi9.socialnetwork.response.EventResponse;
import com.levi9.socialnetwork.service.eventinvitation.EventInvitationService;
import com.levi9.socialnetwork.service.group.GroupService;
import com.levi9.socialnetwork.service.groupmember.GroupMemberService;
import com.levi9.socialnetwork.service.user.UserService;
import com.levi9.socialnetwork.util.AuthUtil;
import com.levi9.socialnetwork.util.GenericMessages;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class EventServiceImpl implements EventService {
    private static final String DATE_TIME_FORMAT = "yyyy/MM/dd HH:mm";
    private final EventRepository eventRepository;
    private final GroupRepository groupRepository;
    private final UserService userService;
    private final GroupMemberService groupMemberService;
    private final EventMapper eventMapper;
    private final EventInvitationService eventInvitationService;


    @Transactional
    @Override
    public EventEntity createEvent(Long groupId, String location, LocalDateTime dateTime) {
        GroupEntity group = groupRepository
                .findById(groupId).orElseThrow(() -> new EntityNotFoundException("Group with ID: " + groupId + " not found"));
        UserEntity loggedInUser = userService.findById(AuthUtil.getPrincipalId());
        GroupMemberEntity creator = getGroupMember(loggedInUser, group);

        EventEntity event = EventEntity.builder()
                .dateTime(dateTime)
                .location(location)
                .group(group)
                .creator(creator)
                .build();
        eventRepository.save(event);

        eventInvitationService.sendEventInvitations(event);

        return event;
    }

    private GroupMemberEntity getGroupMember(UserEntity loggedInUser, GroupEntity group) {
        try {
            return groupMemberService.findByMemberAndGroup(loggedInUser, group);
        } catch (EntityNotFoundException ex) {
            throw new EventCreationUnauthorizedException(String
                    .format(GenericMessages.ERROR_MESSAGE_UNAUTHORIZED_EVENT_CREATION, group.getName()));
        }
    }

    @Override
    public void remove(Long eventId) {
        EventEntity eventToRemove = eventRepository.findById(eventId).orElseThrow(
                () -> new EntityNotFoundException(String.format(GenericMessages.ERROR_MESSAGE_EVENT_NOT_FOUND, eventId)));
        if (eventToRemove.getDateTime().isBefore(LocalDateTime.now().plusHours(3))) {
            throw new EntityNotFoundException(GenericMessages.ERROR_MESSAGE_OUT_OF_TIME);
        }
        if (Objects.equals(AuthUtil.getPrincipalId(), eventToRemove.getCreator().getMember().getId()) || (AuthUtil.getPrincipalId()).equals(eventToRemove.getCreator().getGroup().getAdmin().getId())) {
            eventRepository.delete(eventToRemove);
            return;
        }
        throw new IllegalPriviledgeException(GenericMessages.ERROR_MESSAGE_NOT_ALLOWED);
    }



    @Override
    public EventEntity findById(Long id) {
        return eventRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String
                        .format(GenericMessages.ERROR_MESSAGE_EVENT_NOT_FOUND, id)));
    }
}