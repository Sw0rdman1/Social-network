package com.levi9.socialnetwork.service.groupmember;

import com.levi9.socialnetwork.entity.GroupEntity;
import com.levi9.socialnetwork.entity.GroupMemberEntity;
import com.levi9.socialnetwork.entity.GroupRequestEntity;
import com.levi9.socialnetwork.entity.UserEntity;
import java.util.List;

/**
 * GroupMemberService je intefejs koje definiše metode za upravljanje članstvom korisnika u grupama u društvenoj mreži.
 * Koristi se za pronalaženje, proveru članstva, pronalaženje e-mail adresa korisnika u grupi,
 * odobravanje zahteva za pridruživanje, kreiranje članstva i dohvatanje članova grupe.
 */
public interface GroupMemberService {

    /**
     * Pronalazi članstvo u grupi na osnovu korisnika i grupe.
     *
     * @param member Korisnik za koji se proverava članstvo.
     * @param group  Grupa u kojoj se proverava članstvo.
     * @return Objekat tipa GroupMemberEntity koji predstavlja pronađeno članstvo.
     */
    GroupMemberEntity findByMemberAndGroup(UserEntity member, GroupEntity group);

    /**
     * Proverava postojanje članstva korisnika u grupi.
     *
     * @param member Korisnik za koji se proverava članstvo.
     * @param group  Grupa u kojoj se proverava članstvo.
     * @return True ako korisnik pripada grupi, inače false.
     */
    boolean groupMemberExists(UserEntity member, GroupEntity group);

    /**
     * Pronalazi e-mail adrese korisnika koji su članovi određene grupe na osnovu ID-a grupe.
     *
     * @param groupID ID grupe za koju se traže e-mail adrese članova.
     * @return Lista e-mail adresa članova grupe.
     */
    List<String> findUserEmailsByGroupId(Long groupID);

    /**
     * Admin odobrava zahtev za pridruživanje grupei na osnovu ID-a zahteva.
     *
     * @param requestId ID zahteva za pridruživanje koji se odobrava.
     * @return Objekat tipa GroupMemberEntity koji predstavlja odobreno članstvo.
     */
    GroupMemberEntity adminApproveByRequestId(Long requestId);

    /**
     * Korisnik odobrava zahtev za pridruživanje grupi na osnovu ID-a zahtjeva.
     *
     * @param requestId ID zahteva za pridruživanje koji se odobrava.
     * @return Objekat tipa GroupMemberEntity koji predstavlja odobreno članstvo.
     */
    GroupMemberEntity userApproveByRequestId(Long requestId);

    /**
     * Odobrava zahtjev za pridruživanje grupei.
     *
     * @param groupRequest Zahtjev za pridruživanje grupi.
     * @return Objekat tipa GroupMemberEntity koji predstavlja odobreno članstvo.
     */
    GroupMemberEntity approveRequest(GroupRequestEntity groupRequest);

    /**
     * Cuva članstvo u grupi.
     *
     * @param groupMember Članstvo koje se cuva.
     * @return Objekat tipa GroupMemberEntity koji predstavlja sacuvano članstvo.
     */
    GroupMemberEntity save(GroupMemberEntity groupMember);

    /**
     * Dohvata listu članova grupe na osnovu grupe.
     *
     * @param group Grupa za koju se dohvataju članovi.
     * @return Lista objekata tipa GroupMemberEntity koji predstavljaju članove grupe.
     */
    List<GroupMemberEntity> getGroupMembers(GroupEntity group);
}
