package com.levi9.socialnetwork.service.groupmember;

import com.levi9.socialnetwork.entity.GroupEntity;
import com.levi9.socialnetwork.entity.GroupMemberEntity;
import com.levi9.socialnetwork.entity.GroupRequestEntity;
import com.levi9.socialnetwork.entity.UserEntity;
import com.levi9.socialnetwork.exception.customexception.EntityNotFoundException;
import com.levi9.socialnetwork.exception.customexception.GroupRequestException;
import com.levi9.socialnetwork.repository.GroupMemberRepository;
import com.levi9.socialnetwork.repository.GroupRequestRepository;
import com.levi9.socialnetwork.service.user.UserService;
import com.levi9.socialnetwork.util.AuthUtil;
import com.levi9.socialnetwork.util.GenericMessages;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class GroupMemberServiceImpl implements GroupMemberService {

    private final GroupMemberRepository groupMemberRepository;
    private final GroupRequestRepository groupRequestRepository;
    private final UserService userService;


    /**
     * @throws EntityNotFoundException ako korisnik nije clan te grupe
     *
     * @param member Korisnik za koji se proverava članstvo.
     * @param group  Grupa u kojoj se proverava članstvo.
     * @return objekat da li korisnik pripada nekog grupi
     */
    @Override
    public GroupMemberEntity findByMemberAndGroup(UserEntity member, GroupEntity group) {
        return groupMemberRepository.findByMemberAndGroup(member, group).
                orElseThrow(() -> new EntityNotFoundException(String
                        .format(GenericMessages.ERROR_MESSAGE_USER_NOT_FOUND_IN_GROUP, member.getUsername(),
                                group.getName())));
    }

    /**
     *
     * @param member Korisnik za koji se proverava članstvo.
     * @param group  Grupa u kojoj se proverava članstvo.
     * @return boolean vrednost da li je korisnik clan te grupe
     */
    @Override
    public boolean groupMemberExists(UserEntity member, GroupEntity group) {
        return groupMemberRepository.findByMemberAndGroup(member, group).isPresent();
    }

    /**
     * @throws EntityNotFoundException ako taj zahtev za clanstvo ne postoji
     * @throws GroupRequestException ako korisnik koji zeli prihvatiti zahtev zapravo nije admin grupe
     *
     * @param requestId ID zahteva za pridruživanje koji se odobrava.
     * @return sacuvani korisnik u datoj grupi
     */
    @Override
    public GroupMemberEntity adminApproveByRequestId(Long requestId) {
        UserEntity allegedAdmin = userService.findById(AuthUtil.getPrincipalId());
        GroupRequestEntity groupRequest = groupRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException(String
                        .format(GenericMessages.ERROR_MESSAGE_GROUP_REQUEST_NOT_FOUND, requestId)));
        GroupEntity group = groupRequest.getGroup();
        if(!allegedAdmin.equals(group.getAdmin()))
            throw new GroupRequestException(String
                    .format(GenericMessages.ERROR_MESSAGE_NOT_A_GROUP_ADMIN, group.getName()));
        return approveRequest(groupRequest);
    }

    /**
     * @throws EntityNotFoundException ako taj zahtev za clanstvo ne postoji
     * @throws GroupRequestException ako korisnik koji zeli prihvatiti zahtev zapravo nije onaj kome je zahtev poslat
     *
     * @param requestId ID zahteva za pridruživanje koji se odobrava.
     * @return sacuvani korisnik u datoj grupi
     */
    @Override
    public GroupMemberEntity userApproveByRequestId(Long requestId) {
        UserEntity allegedUser = userService.findById(AuthUtil.getPrincipalId());
        GroupRequestEntity groupRequest = groupRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException(String
                        .format(GenericMessages.ERROR_MESSAGE_GROUP_REQUEST_NOT_FOUND, requestId)));
        if(!allegedUser.equals(groupRequest.getUser()))
            throw new GroupRequestException(GenericMessages.ERROR_MESSAGE_NOT_A_REQUEST_OWNER);
        return approveRequest(groupRequest);
    }

    /**
     *
     * @param groupRequest Zahtjev za pridruživanje grupi.
     * @return sacuvani korisnik u datoj grupi
     */
    @Override
    @Transactional
    public GroupMemberEntity approveRequest(GroupRequestEntity groupRequest) {
        UserEntity user = groupRequest.getUser();
        GroupEntity group = groupRequest.getGroup();

        GroupMemberEntity member = GroupMemberEntity.builder()
                .member(user)
                .group(group)
                .dateTimeJoined(LocalDateTime.now())
                .build();
        GroupMemberEntity savedMember = groupMemberRepository.save(member);
        groupRequestRepository.delete(groupRequest);
        return savedMember;
    }

    /**
     *
     * @param groupMember Članstvo koje se cuva.
     * @return objekat koji sadrzi korisnika i grupu
     */
    @Override
    public GroupMemberEntity save(GroupMemberEntity groupMember) {
        return groupMemberRepository.save(groupMember);
    }

    /**
     *
     * @param groupID ID grupe za koju se traže e-mail adrese članova.
     * @return listu emailova svih clanova neke grupe
     */
    @Override
    public List<String> findUserEmailsByGroupId(Long groupID) {
        return groupMemberRepository.findMemberEmailsByGroupId(groupID);
    }

    /**
     *
     * @param group Grupa za koju se dohvataju članovi.
     * @return listu svih korisnika neke grupe
     */
    @Override
    public List<GroupMemberEntity> getGroupMembers(GroupEntity group) {
        return groupMemberRepository.findByGroup(group).stream().toList();
    }
}
