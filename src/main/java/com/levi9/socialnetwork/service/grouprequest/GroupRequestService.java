package com.levi9.socialnetwork.service.grouprequest;

import com.levi9.socialnetwork.entity.GroupEntity;
import com.levi9.socialnetwork.entity.GroupRequestEntity;
import com.levi9.socialnetwork.entity.UserEntity;
import com.levi9.socialnetwork.response.GroupRequestResponse;

import java.util.List;

/**
 * Interfejs koji definiše operacije koje se odnose na zahteve za pridruživanje grupi.
 */
public interface GroupRequestService {

    /**
     * Šalje zahtev za pridruživanje korisnika određenoj grupi.
     *
     * @param username Korisničko ime korisnika koji šalje zahtev.
     * @param groupId  ID grupe kojoj se šalje zahtev.
     * @return Odgovor na zahtev za pridruživanje kao {@link GroupRequestResponse}.
     */
    GroupRequestResponse sendGroupRequestToUser(String username, Long groupId);

    /**
     * Pronalazi zahtev za pridruživanje određenog korisnika određenoj grupi.
     *
     * @param user  Korisnik koji je poslao zahtev.
     * @param group Grupa kojoj je zahtev upućen.
     * @return Entitet zahteva za pridruživanje kao {@link GroupRequestEntity}.
     */
    GroupRequestEntity findByUserAndGroup(UserEntity user, GroupEntity group);

    /**
     * Šalje zahtev za pridruživanje grupi na osnovu njenog ID-a.
     *
     * @param groupId ID grupe kojoj se šalje zahtev.
     * @return Odgovor na zahtev za pridruživanje kao {@link GroupRequestResponse}.
     */
    GroupRequestResponse sendRequestToJoinGroup(Long groupId);

    /**
     * Pronalazi sve zahteve za pridruživanje određenoj grupi na osnovu njenog ID-a.
     *
     * @param groupId ID grupe za koju se traže zahtevi.
     * @return Lista odgovora na zahteve za pridruživanje kao {@link List<GroupRequestResponse>}.
     */
    List<GroupRequestResponse> finAllGroupRequestsByGroupId(Long groupId);

    /**
     * Pronalazi zahtev za pridruživanje na osnovu njegovog ID-a.
     *
     * @param id ID zahteva za pridruživanje.
     * @return Entitet zahteva za pridruživanje kao {@link GroupRequestEntity}.
     */
    GroupRequestEntity findById(Long id);

    /**
     * Briše određeni zahtev za pridruživanje.
     *
     * @param entity Entitet zahteva za pridruživanje koji se briše.
     */
    void delete(GroupRequestEntity entity);

    /**
     * Administraor odbacuje zahtev za pridruživanje na osnovu ID-a zahteva.
     *
     * @param requestId ID zahteva za pridruživanje koji se odbacuje.
     * @return Entitet odbijenog zahteva za pridruživanje kao {@link GroupRequestEntity}.
     */
    GroupRequestEntity adminChangeRequestToRejected(Long requestId);

    /**
     * Korisnik odbacuje zahtev za pridruživanje na osnovu ID-a zahteva.
     *
     * @param requestId ID zahteva za pridruživanje koji se odbacuje.
     * @return Entitet odbijenog zahteva za pridruživanje kao {@link GroupRequestEntity}.
     */
    GroupRequestEntity userChangeRequestToRejected(Long requestId);

    /**
     * Odbacuje određeni zahtev za pridruživanje.
     *
     * @param request Entitet zahteva za pridruživanje koji se odbacuje.
     * @return Entitet odbijenog zahteva za pridruživanje kao {@link GroupRequestEntity}.
     */
    GroupRequestEntity changeRequestToRejected(GroupRequestEntity request);

    /**
     * Pronalazi sve zahteve za pridruživanje određenog korisnika.
     *
     * @return Lista odgovora na zahteve za pridruživanje kao {@link List<GroupRequestResponse>}.
     */
    List<GroupRequestResponse> findByUserId();
}
