package com.levi9.socialnetwork.service.event;

import com.levi9.socialnetwork.entity.*;
import com.levi9.socialnetwork.exception.customexception.CommentNoPermissionException;
import com.levi9.socialnetwork.exception.customexception.EntityNotFoundException;
import com.levi9.socialnetwork.exception.customexception.EventCreationUnauthorizedException;
import com.levi9.socialnetwork.exception.customexception.IllegalPriviledgeException;
import com.levi9.socialnetwork.mapper.CommentMapper;
import com.levi9.socialnetwork.repository.*;
import com.levi9.socialnetwork.request.CommentRequest;
import com.levi9.socialnetwork.request.EventRequest;
import com.levi9.socialnetwork.service.comment.CommentServiceImpl;
import com.levi9.socialnetwork.service.eventinvitation.EventInvitationService;
import com.levi9.socialnetwork.service.group.GroupService;
import com.levi9.socialnetwork.service.groupmember.GroupMemberService;
import com.levi9.socialnetwork.service.user.UserService;
import com.levi9.socialnetwork.util.AuthUtil;
import com.levi9.socialnetwork.util.GenericValues;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.cglib.core.Local;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class EventServiceImplTest {

    @Mock
    private EventRepository eventRepostiory;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @Mock
    private GroupService groupService;


    @Mock
    private CommentMapper commentMapper;

    @Mock
    private GroupMemberService groupMemberService;

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private EventInvitationService eventInvitationService;


    MockedStatic<AuthUtil> utilities;


    @InjectMocks
    private EventServiceImpl eventService;


    private final LocalDateTime dateTime = LocalDateTime.of(2024, 9, 12, 14, 30);

    @Before
    public void beforeEach() {
        utilities = Mockito.mockStatic(AuthUtil.class);
    }

    @After
    public void afterEach() {
        utilities.close();
    }

    @Test
    @DisplayName("Service should successfully create event")
    public void shouldSuccessfullyCreateEvent() {

        UserEntity userEntity = UserEntity.builder()
                .email(GenericValues.EMAIL)
                .username(GenericValues.USERNAME)
                .active(true)
                .id(GenericValues.USER_ID)
                .build();

        GroupEntity group = new GroupEntity();
        group.setId(1L);

        GroupMemberEntity creator = GroupMemberEntity
                .builder()
                .group(group)
                .member(userEntity)
                .build();

        EventEntity expectedEventEntity = EventEntity.builder()
                .creator(creator)
                .dateTime(dateTime)
                .group(group)
                .location("Beograd")
                .build();

        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));
        utilities.when(AuthUtil::getPrincipalId).thenReturn(GenericValues.USER_ID);
        when(userService.findById(GenericValues.USER_ID)).thenReturn(userEntity);
        when(groupMemberService.findByMemberAndGroup(userEntity, group)).thenReturn(creator);
        when(eventRepostiory.save(Mockito.any(EventEntity.class))).thenReturn(expectedEventEntity);
        var realEventEntity = eventService.createEvent(1l,"Beograd",dateTime);

        assertNotNull(realEventEntity.getDateTime());
        assertEquals(realEventEntity.getGroup(), expectedEventEntity.getGroup());
        assertEquals(realEventEntity.getCreator(), expectedEventEntity.getCreator());
    }


    @Test
    @DisplayName("Service should throw exception for non existing group")
    public void shouldThrowExceptionForCreatingEventInNonExistingGroup() {
        when(groupRepository.findById(1L)).thenAnswer(i -> Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> eventService.createEvent(1L,"Beograd", dateTime));
        assertEquals("Group with ID: 1 not found", exception.getMessage());

        verify(eventRepostiory, never()).save(any(EventEntity.class));
    }

    @Test
    @DisplayName("Service should throw exception for user not being part of group")
    public void shouldThrowExceptionForUserNotBeingInGroup() {

        UserEntity userEntity = UserEntity.builder()
                .email(GenericValues.EMAIL)
                .username(GenericValues.USERNAME)
                .active(true)
                .id(GenericValues.USER_ID)
                .build();

        GroupEntity group = new GroupEntity();
        group.setId(1L);
        group.setName("Grupa");

        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));
        utilities.when(AuthUtil::getPrincipalId).thenReturn(GenericValues.USER_ID);
        when(userService.findById(GenericValues.USER_ID)).thenReturn(userEntity);

        when(groupMemberService.findByMemberAndGroup(userEntity, group)).thenThrow(new EntityNotFoundException(""));

        EventCreationUnauthorizedException exception = assertThrows(EventCreationUnauthorizedException.class, () -> eventService.createEvent(1L,"Beograd", dateTime));
        assertEquals("You are not authorized to create an event in group " + group.getName() + " because you are not a member of the group", exception.getMessage());

        verify(eventRepostiory, never()).save(any(EventEntity.class));
    }

    @Test
    @DisplayName("Service should successfully delete event - you are creator of event")
    public void shouldSuccessfullyDeleteEventCreator() {

        UserEntity userEntity = UserEntity.builder()
                .email(GenericValues.EMAIL)
                .username(GenericValues.USERNAME)
                .active(true)
                .id(GenericValues.USER_ID)
                .build();

        GroupEntity group = new GroupEntity();
        group.setId(1L);

        GroupMemberEntity creator = GroupMemberEntity
                .builder()
                .group(group)
                .member(userEntity)
                .build();

        EventEntity eventEntityToDelete = EventEntity.builder()
                .id(1L)
                .creator(creator)
                .dateTime(dateTime)
                .group(group)
                .location("Beograd")
                .build();

        when(eventRepostiory.findById(any(Long.class))).thenReturn(Optional.of(eventEntityToDelete));
        utilities.when(AuthUtil::getPrincipalId).thenReturn(GenericValues.USER_ID);

        eventService.remove(1L);
        verify(eventRepostiory, times(1)).findById(any(Long.class));
        utilities.verify(AuthUtil::getPrincipalId, times(1));
    }


    @Test
    @DisplayName("Service should successfully delete event - you are admin of group where event is")
    public void shouldSuccessfullyDeleteEventAdmin() {

        UserEntity user = UserEntity.builder()
                .email(GenericValues.EMAIL)
                .username(GenericValues.USERNAME)
                .active(true)
                .id(GenericValues.DUMMY_USER1_ID)
                .build();

        UserEntity admin = UserEntity.builder()
                .email(GenericValues.EMAIL)
                .username(GenericValues.USERNAME)
                .active(true)
                .id(GenericValues.USER_ID)
                .build();

        GroupEntity group = new GroupEntity();
        group.setId(1L);
        group.setAdmin(admin);

        GroupMemberEntity creator = GroupMemberEntity
                .builder()
                .group(group)
                .member(user)
                .build();

        EventEntity eventEntityToDelete = EventEntity.builder()
                .id(1L)
                .creator(creator)
                .dateTime(dateTime)
                .group(group)
                .location("Beograd")
                .build();

        when(eventRepostiory.findById(any(Long.class))).thenReturn(Optional.of(eventEntityToDelete));
        utilities.when(AuthUtil::getPrincipalId).thenReturn(GenericValues.USER_ID);

        eventService.remove(1L);
        verify(eventRepostiory, times(1)).findById(any(Long.class));
        utilities.verify(AuthUtil::getPrincipalId, times(2));
    }


    @Test
    @DisplayName("Service should throw exception for trying to delete event if you dont have permission!")
    public void shouldThrowExceptionForNoPermissionDeletingEvent() {

        UserEntity user1 = new UserEntity();
        user1.setId(GenericValues.DUMMY_USER_ID);

        UserEntity user2 = new UserEntity();
        user2.setId(GenericValues.DUMMY_USER1_ID);

        UserEntity user3 = new UserEntity();
        user3.setId(GenericValues.DUMMY_USER2_ID);


        GroupEntity group = new GroupEntity();
        group.setId(1L);
        group.setAdmin(user1);

        GroupMemberEntity creator = GroupMemberEntity
                .builder()
                .group(group)
                .member(user2)
                .build();

        EventEntity eventEntityToDelete = EventEntity.builder()
                .id(1L)
                .creator(creator)
                .dateTime(dateTime)
                .group(group)
                .location("Beograd")
                .build();

        when(eventRepostiory.findById(any(Long.class))).thenReturn(Optional.of(eventEntityToDelete));
        utilities.when(AuthUtil::getPrincipalId).thenReturn(GenericValues.DUMMY_USER2_ID);

        IllegalPriviledgeException exception = Assert.assertThrows(IllegalPriviledgeException.class, () -> eventService.remove(1L));
        Assert.assertEquals("Only group admins and event creators can remove events", exception.getMessage());

        verify(eventRepostiory, times(1)).findById(any(Long.class));
        utilities.verify(AuthUtil::getPrincipalId, times(2));
        verify(eventRepostiory, never()).delete(any(EventEntity.class));
    }

    @Test
    @DisplayName("Service should throw exception for deleting non-existing event")
    public void shouldThrowExceptionForNotFoundDeleteEvent() {
        when(eventRepostiory.findById(any(Long.class))).thenReturn(Optional.empty());

        EntityNotFoundException exception = Assert.assertThrows(EntityNotFoundException.class, () -> eventService.remove(1L));
        Assert.assertEquals("Event with ID: 1 not found", exception.getMessage());

        verify(eventRepostiory, times(1)).findById(any(Long.class));
        verify(eventRepostiory, never()).delete(any(EventEntity.class));
    }

    @Test
    @DisplayName("Service should throw exception for event time in near future")
    public void shouldThrowExceptionForEventToCloseToTimeDelete() {
        EventEntity eventEntityToDelete = EventEntity.builder()
                .id(1L)
                .dateTime(LocalDateTime.now())
                .location("Beograd")
                .build();

        when(eventRepostiory.findById(any(Long.class))).thenReturn(Optional.of(eventEntityToDelete));

        EntityNotFoundException exception = Assert.assertThrows(EntityNotFoundException.class, () -> eventService.remove(1L));
        Assert.assertEquals("Event is too soon to delete", exception.getMessage());

        verify(eventRepostiory, times(1)).findById(any(Long.class));
        verify(eventRepostiory, never()).delete(any(EventEntity.class));
    }
}