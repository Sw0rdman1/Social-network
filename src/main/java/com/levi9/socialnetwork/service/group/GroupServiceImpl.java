package com.levi9.socialnetwork.service.group;

import com.levi9.socialnetwork.entity.GroupEntity;
import com.levi9.socialnetwork.entity.GroupMemberEntity;
import com.levi9.socialnetwork.entity.PostEntity;
import com.levi9.socialnetwork.entity.UserEntity;
import com.levi9.socialnetwork.exception.customexception.EntityAlreadyExistsException;
import com.levi9.socialnetwork.exception.customexception.EntityNotFoundException;
import com.levi9.socialnetwork.exception.customexception.GroupRequestException;
import com.levi9.socialnetwork.exception.customexception.GroupException;
import com.levi9.socialnetwork.mapper.GroupMapper;
import com.levi9.socialnetwork.mapper.PostMapper;
import com.levi9.socialnetwork.repository.GroupMemberRepository;
import com.levi9.socialnetwork.repository.GroupRepository;
import com.levi9.socialnetwork.repository.UserRepository;
import com.levi9.socialnetwork.repository.PostRepository;
import com.levi9.socialnetwork.response.GroupResponse;
import com.levi9.socialnetwork.response.PostResponse;
import com.levi9.socialnetwork.service.user.UserService;
import com.levi9.socialnetwork.util.AuthUtil;
import com.levi9.socialnetwork.util.GenericMessages;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.List;

@Service
@AllArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final UserService userService;
    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;
    private final GroupMemberRepository groupMemberRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostMapper postMapper;


    /**
     * @throws EntityNotFoundException ako grupa sa unetim imenom ne postoji
     *
     * @param groupName Ime grupe koja se traži.
     * @return grupa koja je nadjena na osnovu imena
     */
    @Override
    public GroupEntity findByName(String groupName) {
        return groupRepository.findByName(groupName).orElseThrow(() -> new EntityNotFoundException(String.format(GenericMessages.ERROR_MESSAGE_GROUP_NOT_FOUND_BY_NAME, groupName)));
    }

    /**
     * @throws EntityNotFoundException ako grupa u koju zelimo uci ne postoji ili  nije otvorena
     * @throws GroupException ako smo vec clan te grupe
     *
     * @param groupId Identifikator javne grupe kojoj se pridružuje korisnik.
     */
    @Override
    public void joinPublicGroup(int groupId) {
        GroupEntity targetGroup = groupRepository.findById(Long.valueOf(groupId)).orElseThrow(() -> new EntityNotFoundException(GenericMessages.ERROR_MESSAGE_GROUP_NOT_FOUND));
        UserEntity loggedUser = userService.findById(AuthUtil.getPrincipalId());

        if (targetGroup.isClosed()) {
            throw new EntityNotFoundException(GenericMessages.ERROR_GROUP_IS_CLOSED);
        }

        if (groupMemberRepository.findByMemberAndGroup(loggedUser, targetGroup).isPresent()) {
            throw new GroupException(String.format(GenericMessages.ERROR_MESSAGE_ALREADY_GROUP_MEMBER, loggedUser.getUsername(), targetGroup.getName()));
        }

        GroupMemberEntity groupMember = GroupMemberEntity.builder()
                .member(loggedUser)
                .group(targetGroup)
                .dateTimeJoined(LocalDateTime.now())
                .build();

        groupMemberRepository.save(groupMember);
    }

    /**
     * @throws EntityNotFoundException ako grupa ne postoji ili nismo njen clan ili nema objava u toj grupi
     *
     * @param groupId Identifikator grupe za koju se prikazuju postovi.
     * @return sve objave iz te grupe
     */
    @Override
    public List<PostResponse> showPostsInGroup(int groupId) {
        GroupEntity target = groupRepository.findById(Long.valueOf(groupId)).orElseThrow(() -> new EntityNotFoundException(GenericMessages.ERROR_MESSAGE_GROUP_NOT_FOUND));
        UserEntity loggedUser = userService.findById(AuthUtil.getPrincipalId());
        groupMemberRepository.findByMemberAndGroup(loggedUser, target).orElseThrow(() -> new EntityNotFoundException(GenericMessages.ERROR_MESSAGE_NOT_A_MEMBER));
        List<PostEntity> posts = postRepository.findByGroupIdAndDeletedFalse(Long.valueOf(groupId));

        List<PostResponse> responses = new ArrayList<PostResponse>();
        for (PostEntity post : posts) {
            if (post == null) {
                throw new EntityNotFoundException(GenericMessages.ERROR_MESSAGE_POST_NOT_FOUND);
            }
            responses.add(postMapper.toPostResponse(post));
        }
        return responses;
    }

    /**
     * @throws EntityNotFoundException ako taj korisnik ne postoji ili sama grupa ne postoji ili korisnik nije uopste clan te grupe
     * @throws GroupRequestException ako korisnik koji zeli zibaciti drugog nije admin grupe ili ako je admin a zeli samog sebe izbaciti
     *
     * @param groupId        Identifikator grupe iz koje se uklanja član.
     * @param userToBeRemoved Korisničko ime korisnika koji se uklanja iz grupe.
     */
    @Override
    public void removeMember(int groupId, String userToBeRemoved) {
        UserEntity userRemove = userRepository.findByUsername(userToBeRemoved).orElseThrow(() -> new EntityNotFoundException(String.format(GenericMessages.ERROR_MESSAGE_USER_NOT_FOUND,userToBeRemoved)));
        GroupEntity targetGroup = groupRepository.findById(Long.valueOf(groupId)).orElseThrow(() -> new EntityNotFoundException(GenericMessages.ERROR_MESSAGE_GROUP_NOT_FOUND));
        GroupMemberEntity groupMemberEntity = groupMemberRepository.findByMemberAndGroup(userRemove,targetGroup).orElseThrow(() -> new EntityNotFoundException(String.format(GenericMessages.ERROR_MESSAGE_NOT_IN_GROUP, targetGroup.getName())));

        UserEntity loggedInUser = userRepository.findById(AuthUtil.getPrincipalId()).get();
        if(!(loggedInUser.equals(targetGroup.getAdmin())))
        {
            throw new GroupRequestException(GenericMessages.ERROR_MESSAGE_USER_IS_NOT_ADMIN);
        }
        if(userRemove.equals(loggedInUser))
        {
            throw new GroupRequestException(GenericMessages.ERROR_MESSAGE_USER_IS_ADMIN);
        }
        groupMemberRepository.delete(groupMemberEntity);
    }

    /**
     *
     * @param groupName ime grupe
     * @return boolean vrednost da li grupa sa unetim imenom postoji
     */
    private boolean groupExists(String groupName) {
        return groupRepository.findByName(groupName).isPresent();
    }


    /**
     * @throws GroupRequestException ako grupa sa unetim imenom postoji
     *
     * @param groupName Ime nove grupe.
     * @param closed    Status zatvorenosti grupe (true ako je zatvorena, false inače).
     * @return grupu koja je kreirana
     */
    @Override
    @Transactional
    public GroupResponse createGroup(String groupName, boolean closed) {

        if (groupExists(groupName)) {
            throw new EntityAlreadyExistsException(String.format(GenericMessages.GROUP_NAME_ALREADY_EXIST_MESSAGE, groupName));
        }

        UserEntity groupAdmin = userService.findById(AuthUtil.getPrincipalId());

        GroupEntity group = GroupEntity.builder()
                .admin(groupAdmin)
                .name(groupName)
                .closed(closed)
                .build();
        GroupMemberEntity groupMemberAdmin = GroupMemberEntity.builder()
                .member(groupAdmin)
                .group(group)
                .dateTimeJoined(LocalDateTime.now())
                .build();

        groupRepository.save(group);
        groupMemberRepository.save(groupMemberAdmin);
        return groupMapper.mapGroupEntityToGroupResponse(group);
    }

    /**
     * @throws EntityNotFoundException ako grupa sa unetimID-ijem ne postoji
     *
     * @param id Identifikator grupe koja se traži.
     * @return objekat grupe
     */
    @Override
    public GroupEntity findById(Long id) {
        return groupRepository
                .findById(id).orElseThrow(() -> new EntityNotFoundException("Group with ID: " + id + " not found"));
    }

    /**
     *
     * @param searchCriteria Kriterijum pretrage za imena grupa.
     * @return listu grupa koje odgovaraju kriterijumu pretrage
     */
    @Override
    public List<GroupResponse> searchGroupsByNameContaining(String searchCriteria) {
        List<GroupEntity> resultGroups = searchCriteria == null ?
                groupRepository.findAll() :
                groupRepository.findAllByNameContaining(searchCriteria);
        return resultGroups.stream().map(groupMapper::mapGroupEntityToGroupResponse).toList();
    }


    /**
     * @throws EntityNotFoundException ako grupa ne postoji
     * @throws GroupException ako korisnik koji zeli izbrisati grupu nije admin same grupe
     *
     * @param groupName Ime grupe koja se briše.
     */
    @Override
    public void deleteGroup(String groupName) {
        String loggedUserId = AuthUtil.getPrincipalId();
        GroupEntity groupEntity = groupRepository.findByName(groupName)
                .orElseThrow(()->new EntityNotFoundException(String
                        .format(GenericMessages.ERROR_MESSAGE_GROUP_NOT_FOUND_BY_NAME, groupName)));
        if(!groupEntity.getAdmin().getId().equals(loggedUserId))
            throw new GroupException(GenericMessages.ERROR_MESSAGE_GROUP_CAN_BE_DELETED_ONLY_BY_ADMIN);
        groupRepository.delete(groupEntity);
    }


    /**
     * @throws EntityNotFoundException ako grupa sa unetim ID-ijem ne postoji ili korisnik nije clan te grupe
     * @throws IllegalStateException ako je korisnik admin same grupe
     *
     * @param groupId Identifikator grupe koju korisnik napušta.
     */
    @Override
    public void leaveGroup(int groupId) {
        GroupEntity targetGroup = groupRepository
                .findById(Long.valueOf(groupId))
                .orElseThrow(() -> new EntityNotFoundException(GenericMessages.ERROR_MESSAGE_GROUP_NOT_FOUND));
        UserEntity loggedUser = userService.findById(AuthUtil.getPrincipalId());
        GroupMemberEntity currentMember = groupMemberRepository
                .findByMemberAndGroup(loggedUser,targetGroup)
                .orElseThrow(() -> new EntityNotFoundException(GenericMessages.ERROR_MESSAGE_NOT_A_MEMBER));
        if(targetGroup.getAdmin() == loggedUser)
        {
            throw new IllegalStateException(GenericMessages.ERROR_MESSAGE_ADMIN_CANNOT_LEAVE);
        }
        groupMemberRepository.delete(currentMember);
    }
}
