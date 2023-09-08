package com.levi9.socialnetwork.service.eventinvitation;

import com.levi9.socialnetwork.entity.*;
import com.levi9.socialnetwork.exception.customexception.EntityNotFoundException;
import com.levi9.socialnetwork.exception.customexception.EventInvitationException;
import com.levi9.socialnetwork.mapper.EventInvitationMapper;
import com.levi9.socialnetwork.repository.EventInvitationRepository;
import com.levi9.socialnetwork.repository.EventRepository;
import com.levi9.socialnetwork.response.EventInvitationResponse;
import com.levi9.socialnetwork.service.email.EmailService;
import com.levi9.socialnetwork.service.user.UserService;
import com.levi9.socialnetwork.util.AuthUtil;
import com.levi9.socialnetwork.util.GenericMessages;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import com.levi9.socialnetwork.entity.EventEntity;
import com.levi9.socialnetwork.entity.EventInvitationEntity;
import com.levi9.socialnetwork.entity.EventInvitationStatusEntity;
import com.levi9.socialnetwork.entity.GroupMemberEntity;
import com.levi9.socialnetwork.service.groupmember.GroupMemberService;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
public class EventInvitationServiceImpl implements EventInvitationService {

    private final EventInvitationRepository eventInvitationRepository;
    private final UserService userService;
    private final EventInvitationMapper eventInvitationMapper;
    private final EventRepository eventRepository;
    private final EmailService emailService;
    private final GroupMemberService groupMemberService;

    @Override
    public EventInvitationResponse acceptEventInvitation(Long invitationId) {
        EventInvitationEntity eventInvitation = findById(invitationId);
        UserEntity loggedInUser = userService.findById(AuthUtil.getPrincipalId());

        if (!eventInvitation.getInvitee().getMember().equals(loggedInUser)) {
            throw new EventInvitationException(String
                    .format(GenericMessages.ERROR_MESSAGE_EVENT_INVITATION_WRONG_INVITEE, invitationId));
        }

        if (eventInvitation.getStatus().equals(EventInvitationStatusEntity.COMING)) {
            throw new EventInvitationException(GenericMessages.ERROR_MESSAGE_EVENT_INVITATION_ALREADY_CONFIRMED);
        }

        if (LocalDateTime.now().isAfter(eventInvitation.getEvent().getDateTime().minusHours(24))) {
            throw new EventInvitationException(GenericMessages.ERROR_MESSAGE_EVENT_INVITATION_CONFIRMATION_TIME_EXPIRED);
        }

        eventInvitation.setStatus(EventInvitationStatusEntity.COMING);
        EventInvitationEntity confirmedEventInvitation = eventInvitationRepository.save(eventInvitation);
        return eventInvitationMapper.mapEventInvitationEntityToEventInvitationResponse(confirmedEventInvitation);
    }

    @Override
    public EventInvitationEntity findById(Long id) {
        return eventInvitationRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String
                        .format(GenericMessages.ERROR_MESSAGE_EVENT_INVITATION_NOT_FOUND, id)));
    }

    @Scheduled(fixedRate = 60000)
    public void sendReminders() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime inThreeHours = now.plusMinutes(1);
        List<EventEntity> upcomingEvents = eventRepository.findEventsStartingBetween(now, inThreeHours);

        upcomingEvents
                .forEach(event -> eventInvitationRepository.getAllByEventAndStatus(event, EventInvitationStatusEntity.COMING)
                        .forEach(emailService::sendEventReminder));
    }

    @Transactional
    @Override
    public List<EventInvitationEntity> sendEventInvitations(EventEntity event) {
        EventInvitationEntity creatorEventInvitation = EventInvitationEntity.builder()
                .invitee(event.getCreator())
                .event(event)
                .status(EventInvitationStatusEntity.COMING)
                .build();
        eventInvitationRepository.save(creatorEventInvitation);

        List<GroupMemberEntity> groupMembers = groupMemberService.getGroupMembers(event.getGroup());
        return groupMembers.stream()
                .filter(member -> !member.equals(event.getCreator()))
                .map(member -> createEventInvitation(member, event))
                .toList();
    }

    private EventInvitationEntity createEventInvitation(GroupMemberEntity member, EventEntity event) {
        EventInvitationEntity eventInvitation = EventInvitationEntity.builder()
                .invitee(member)
                .event(event)
                .status(EventInvitationStatusEntity.NOT_COMING)
                .build();
        emailService.sendEventNotification(eventInvitation);
        return eventInvitationRepository.save(eventInvitation);
    }
}