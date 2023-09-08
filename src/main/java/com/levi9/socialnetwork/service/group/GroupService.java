package com.levi9.socialnetwork.service.group;

import com.levi9.socialnetwork.entity.GroupEntity;
import com.levi9.socialnetwork.response.GroupResponse;
import com.levi9.socialnetwork.response.PostResponse;
import java.util.List;

/**
 * GroupService je intefejs koji definiše metode za upravljanje grupama u društvenoj mreži.
 * Koristi se za kreiranje, pridruživanje, pronalaženje, prikazivanje, uklanjanje članova, napuštanje i brisanje grupa.
 */
public interface GroupService {

    /**
     * Kreira novu grupu sa zadatim imenom i statusom zatvorenosti.
     *
     * @param groupName Ime nove grupe.
     * @param closed    Status zatvorenosti grupe (true ako je zatvorena, false inače).
     * @return Objekat tipa GroupResponse koji predstavlja novu grupu.
     */
    GroupResponse createGroup(String groupName, boolean closed);

    /**
     * Pridružuje se javnoj grupi na osnovu njenog identifikatora.
     *
     * @param groupId Identifikator javne grupe kojoj se pridružuje korisnik.
     */
    void joinPublicGroup(int groupId);

    /**
     * Pronalazi grupu na osnovu njenog imena.
     *
     * @param groupName Ime grupe koja se traži.
     * @return Objekat tipa GroupEntity koji predstavlja pronađenu grupu.
     */
    GroupEntity findByName(String groupName);

    /**
     * Prikazuje sve postove u određenoj grupi na osnovu njenog identifikatora.
     *
     * @param groupId Identifikator grupe za koju se prikazuju postovi.
     * @return Lista objekata tipa PostResponse koji predstavljaju postove u grupi.
     */
    List<PostResponse> showPostsInGroup(int groupId);

    /**
     * Uklanja korisnika iz određene grupe na osnovu identifikatora grupe i korisničkog imena korisnika.
     *
     * @param groupId        Identifikator grupe iz koje se uklanja član.
     * @param userToBeRemoved Korisničko ime korisnika koji se uklanja iz grupe.
     */
    void removeMember(int groupId, String userToBeRemoved);

    /**
     * Pronalazi grupu na osnovu njenog identifikatora.
     *
     * @param id Identifikator grupe koja se traži.
     * @return Objekat tipa GroupEntity koji predstavlja pronađenu grupu.
     */
    GroupEntity findById(Long id);

    /**
     * Napušta grupu na osnovu njenog identifikatora.
     *
     * @param groupId Identifikator grupe koju korisnik napušta.
     */
    void leaveGroup(int groupId);

    /**
     * Pretraga grupa na osnovu imena koje sadrži zadati kriterijum pretrage.
     *
     * @param searchCriteria Kriterijum pretrage za imena grupa.
     * @return Lista objekata tipa GroupResponse koji predstavljaju pronađene grupe.
     */
    List<GroupResponse> searchGroupsByNameContaining(String searchCriteria);

    /**
     * Briše grupu na osnovu njenog imena.
     *
     * @param groupName Ime grupe koja se briše.
     */
    void deleteGroup(String groupName);
}
