package com.levi9.socialnetwork.service.grouprequest;

import com.levi9.socialnetwork.entity.*;
import com.levi9.socialnetwork.exception.customexception.EntityNotFoundException;
import com.levi9.socialnetwork.exception.customexception.FriendRequestException;
import com.levi9.socialnetwork.exception.customexception.GroupRequestException;
import com.levi9.socialnetwork.mapper.GroupRequestMapper;
import com.levi9.socialnetwork.repository.GroupRepository;
import com.levi9.socialnetwork.repository.GroupRequestRepository;
import com.levi9.socialnetwork.response.GroupRequestResponse;
import com.levi9.socialnetwork.service.group.GroupService;
import com.levi9.socialnetwork.service.groupmember.GroupMemberService;
import com.levi9.socialnetwork.service.user.UserService;
import com.levi9.socialnetwork.util.AuthUtil;
import com.levi9.socialnetwork.util.GenericMessages;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.List;


@Service
@AllArgsConstructor
public class GroupRequestServiceImpl implements GroupRequestService {

    private final GroupService groupService;
    private final GroupRepository groupRepository;
    private final UserService userService;
    private final GroupMemberService groupMemberService;
    private final GroupRequestRepository groupRequestRepository;
    private final GroupRequestMapper groupRequestMapper;


    /**
     * @throws GroupRequestException ako korisnik koji salje zahtev nije admin grupe ili je korisnik kome se salje zahtev vec clan grupe
     * @throws FriendRequestException ako je admin vec poslao zahtev tom korisniku ili je to uradio tri puta a on ga odbio
     *
     * @param username Korisničko ime korisnika koji šalje zahtev.
     * @param groupId  ID grupe kojoj se šalje zahtev.
     * @return  zahtev koji je poslat korisniku
     */
    @Override
    public GroupRequestResponse sendGroupRequestToUser(String username, Long groupId) {
        UserEntity user = userService.findByUsername(username);
        GroupEntity group = groupService.findById(groupId);
        UserEntity allegedAdmin = userService.findById(AuthUtil.getPrincipalId());

        if (!allegedAdmin.equals(group.getAdmin())) {
            throw new GroupRequestException(String
                    .format(GenericMessages.ERROR_MESSAGE_NOT_A_GROUP_ADMIN, group.getName()));
        }

        if (groupMemberService.groupMemberExists(user, group)) {
            throw new GroupRequestException(String
                    .format(GenericMessages.ERROR_MESSAGE_ALREADY_GROUP_MEMBER, user.getUsername(), group.getName()));
        }

        try {
            GroupRequestEntity groupRequest = findByUserAndGroup(user, group);
            if (groupRequest.isAdminSentRequest()) {
                if (groupRequest.getStatus().equals(RequestStatusEntity.PENDING)) {
                    throw new FriendRequestException(String
                            .format(GenericMessages.ERROR_MESSAGE_ALREADY_PENDING_REQUEST_BY_ADMIN, group.getName(),
                                    user.getUsername()));
                } else if (groupRequest.getRequestCounter() >= 3) {
                    throw new FriendRequestException(GenericMessages.ERROR_MESSAGE_REQUEST_TIMES_LIMIT);
                } else {
                    groupRequest.setStatus(RequestStatusEntity.PENDING);
                    groupRequest.setRequestCounter(groupRequest.getRequestCounter() + 1);
                    groupRequestRepository.save(groupRequest);

                    return groupRequestMapper.mapGroupRequestEntityToGroupRequestResponseSentByAdmin(groupRequest);
                }
            } else {
                if (groupRequest.getStatus().equals(RequestStatusEntity.PENDING)) {
                    groupMemberService.approveRequest(groupRequest);
                    GroupRequestResponse response = groupRequestMapper
                            .mapGroupRequestEntityToGroupRequestResponseSentByAdmin(groupRequest);
                    response.setStatus(RequestStatusEntity.ACCEPTED);

                    return response;
                } else {
                    groupRequest.setStatus(RequestStatusEntity.PENDING);
                    groupRequest.setAdminSentRequest(true);
                    groupRequest.setRequestCounter(1);
                    groupRequestRepository.save(groupRequest);

                    return groupRequestMapper.mapGroupRequestEntityToGroupRequestResponseSentByAdmin(groupRequest);
                }
            }
        } catch (EntityNotFoundException ex) {
            return saveGroupRequest(user, group, true);
        }
    }


    /**
     * @throws EntityNotFoundException ako grupa ne postoji
     * @throws GroupRequestException ako korisnik koji salje zahtevnije ulogovani korisnik
     * @throws FriendRequestException ako je korisnik vec poslao zahtev za uclanjenje u grupu ili je to uradio tri puta a admin ga odbio
     *
     * @param groupId  ID grupe kojoj se šalje zahtev.
     * @return  zahtev koji je poslat korisniku
     */
    @Override
    public GroupRequestResponse sendRequestToJoinGroup(Long groupId) {
        UserEntity currentUser = userService.findById(AuthUtil.getPrincipalId());
        GroupEntity group = groupRepository.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Group with ID: " + groupId + " not found"));

        if (Objects.equals(group.getAdmin().getUsername(), currentUser.getUsername())) {
            throw new GroupRequestException(String.format
                    (GenericMessages.ERROR_MESSAGE_ALREADY_GROUP_MEMBER_AS_ADMIN, group.getName()));
        }

        if (groupMemberService.groupMemberExists(currentUser, group)) {
            throw new GroupRequestException(String
                    .format(GenericMessages.ERROR_MESSAGE_ALREADY_GROUP_MEMBER, currentUser.getUsername(), group.getName()));
        }
        try {
            GroupRequestEntity groupRequest = findByUserAndGroup(currentUser, group);
            if (!groupRequest.isAdminSentRequest()) {
                if (groupRequest.getStatus().equals(RequestStatusEntity.PENDING)) {
                    throw new FriendRequestException(String
                            .format(GenericMessages.ERROR_MESSAGE_ALREADY_PENDING_USER, group.getName()));
                } else if (groupRequest.getRequestCounter() >= 3) {
                    throw new FriendRequestException(GenericMessages.ERROR_MESSAGE_REQUEST_TIMES_LIMIT_TO_ADMIN);
                } else {
                    groupRequest.setStatus(RequestStatusEntity.PENDING);
                    groupRequest.setRequestCounter(groupRequest.getRequestCounter() + 1);
                    groupRequestRepository.save(groupRequest);

                    return groupRequestMapper.mapGroupRequestEntityToGroupRequestResponseSentByUser(groupRequest);
                }
            } else {
                if (groupRequest.getStatus().equals(RequestStatusEntity.PENDING)) {
                    groupMemberService.approveRequest(groupRequest);
                    GroupRequestResponse response = groupRequestMapper
                            .mapGroupRequestEntityToGroupRequestResponseSentByAdmin(groupRequest);
                    response.setStatus(RequestStatusEntity.ACCEPTED);

                    return response;
                } else {
                    // This edge case is when admin sent user request to join and user
                    // reject it, and now user is sending admin request to join that group
                    groupRequest.setStatus(RequestStatusEntity.PENDING);
                    groupRequest.setAdminSentRequest(false);
                    groupRequest.setRequestCounter(1);
                    groupRequestRepository.save(groupRequest);

                    return groupRequestMapper.mapGroupRequestEntityToGroupRequestResponseSentByUser(groupRequest);
                }
            }
        } catch (EntityNotFoundException ex) {
            if (!group.isClosed()) {
                GroupMemberEntity memberEntity = GroupMemberEntity.builder()
                        .member(currentUser)
                        .group(group)
                        .dateTimeJoined(LocalDateTime.now())
                        .build();
                groupMemberService.save(memberEntity);
                return GroupRequestResponse.builder()
                        .id(0L)
                        .adminSentRequest(false)
                        .group(group.getName())
                        .sender(currentUser.getUsername())
                        .status(RequestStatusEntity.ACCEPTED)
                        .build();
            }
            return saveGroupRequest(currentUser, group, false);
        }
    }

    /**
     *
     * @param user korisnik kome se salje zahtev
     * @param group grupa za koju se salje zahtev
     * @param isAdmin boolean vrednost da li admin salje zahtev ili korisnik
     * @return objekat koji ppredstavlja zahtev za uclanjenje
     */
    private GroupRequestResponse saveGroupRequest(UserEntity user, GroupEntity group, boolean isAdmin) {
        GroupRequestEntity groupRequest = GroupRequestEntity.builder()
                .user(user)
                .group(group)
                .status(RequestStatusEntity.PENDING)
                .adminSentRequest(isAdmin)
                .requestCounter(1)
                .build();
        groupRequestRepository.save(groupRequest);

        return isAdmin ? groupRequestMapper.mapGroupRequestEntityToGroupRequestResponseSentByAdmin(groupRequest) :
                groupRequestMapper.mapGroupRequestEntityToGroupRequestResponseSentByUser(groupRequest);
    }


    /**
     * @throws EntityNotFoundException ako zahtev ne postoji
     *
     * @param user  Korisnik koji je poslao zahtev.
     * @param group Grupa kojoj je zahtev upućen.
     * @return objekat koji vezuje korisnika i grupu
     */
    @Override
    public GroupRequestEntity findByUserAndGroup(UserEntity user, GroupEntity group) {
        return groupRequestRepository.findByUserAndGroup(user, group)
                .orElseThrow(() -> new EntityNotFoundException(String.format(GenericMessages.ERROR_MESSAGE_FRIEND_REQUEST_NOT_FOUND,
                        user.getUsername(), group.getName())));
    }

    /**
     * @throws GroupRequestException ako korisnik nije admin grupe za koju trazi zahteve
     *
     * @param groupId ID grupe za koju se traže zahtevi.
     * @return svi zahtevi koji su vezani za odredjenu grupu
     */
    @Override
    public List<GroupRequestResponse> finAllGroupRequestsByGroupId(Long groupId) {
        GroupEntity group = groupService.findById(groupId);
        UserEntity allegedAdmin = userService.findById(AuthUtil.getPrincipalId());
        if (!allegedAdmin.equals(group.getAdmin())) {
            throw new GroupRequestException(String
                    .format(GenericMessages.ERROR_MESSAGE_NOT_A_GROUP_ADMIN, group.getName()));
        }

        return groupRequestRepository
                .findAllByGroupAndStatusAndAdminSentRequest(group, RequestStatusEntity.PENDING, false)
                .stream()
                .map(groupRequestMapper::mapGroupRequestEntityToGroupRequestResponseSentByUser)
                .toList();
    }

    /**
     * @throws EntityNotFoundException ako zahtev ne postoji
     *
     * @param id ID zahteva za pridruživanje.
     * @return objekat zahteva na osnovu njegovog ID-a
     */
    @Override
    public GroupRequestEntity findById(Long id) {
        return groupRequestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String
                        .format(GenericMessages.ERROR_MESSAGE_GROUP_REQUEST_NOT_FOUND, id)));
    }


    @Override
    public void delete(GroupRequestEntity entity) {
        groupRequestRepository.delete(entity);
    }

    /**
     * @throws GroupRequestException ako korisnik nije admin grupe
     *
     * @param requestId ID zahteva za pridruživanje koji se odbacuje.
     * @return zahtev koji je odbijen i brojac povecan za 1
     */
    @Override
    public GroupRequestEntity adminChangeRequestToRejected(Long requestId) {
        GroupRequestEntity request = findById(requestId);
        UserEntity allegedAdmin = userService.findById(AuthUtil.getPrincipalId());
        if (!allegedAdmin.equals(request.getGroup().getAdmin()))
            throw new GroupRequestException(String
                    .format(GenericMessages.ERROR_MESSAGE_NOT_A_GROUP_ADMIN, request.getGroup().getName()));
        return changeRequestToRejected(request);
    }

    /**
     * @throws GroupRequestException ako ulogovani korisnik nije onaj kome je zahtev poslat
     *
     * @param requestId ID zahteva za pridruživanje koji se odbacuje.
     * @return zahtev koji je odbijen i brojac povecan za 1
     */
    @Override
    public GroupRequestEntity userChangeRequestToRejected(Long requestId) {
        GroupRequestEntity request = findById(requestId);
        UserEntity allegedUser = userService.findById(AuthUtil.getPrincipalId());
        if (!allegedUser.equals(request.getUser()))
            throw new GroupRequestException(GenericMessages.ERROR_MESSAGE_NOT_A_REQUEST_OWNER);
        return changeRequestToRejected(request);
    }

    /**
     *
     * @param request Entitet zahteva za pridruživanje koji se odbacuje.
     * @return izmenjeni objekat zahteva
     */
    @Override
    public GroupRequestEntity changeRequestToRejected(GroupRequestEntity request) {
        request.setStatus(RequestStatusEntity.REJECTED);
        groupRequestRepository.save(request);
        return request;
    }

    /**
     *
     * @return listu svih zahteva vezanih za ulogovanog korisnika
     */
    @Override
    public List<GroupRequestResponse> findByUserId() {
        UserEntity user = userService.findById(AuthUtil.getPrincipalId());
        return groupRequestRepository
                .findAllByUserAndStatusAndAdminSentRequest(user, RequestStatusEntity.PENDING, true)
                .stream()
                .map(groupRequestMapper::mapGroupRequestEntityToGroupRequestResponseSentByAdmin)
                .toList();
    }
}