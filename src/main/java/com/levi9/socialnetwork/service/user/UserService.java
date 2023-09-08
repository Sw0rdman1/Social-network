package com.levi9.socialnetwork.service.user;

import com.levi9.socialnetwork.entity.UserEntity;
import com.levi9.socialnetwork.response.UserResponse;

import java.util.List;

/**
 * Interfejs koji definiše operacije za rad sa korisnicima u društvenoj mreži.
 */
public interface UserService {

    /**
     * Pronalazi korisnika na osnovu email adrese.
     *
     * @param email Email adresa korisnika.
     * @return Entitet korisnika kao {@link UserEntity}.
     */
    UserEntity findByEmail(String email);

    /**
     * Čuva ili ažurira korisnika.
     *
     * @param user Entitet korisnika koji se čuva ili ažurira.
     * @return Sačuvani ili ažurirani entitet korisnika kao {@link UserEntity}.
     */
    UserEntity save(UserEntity user);

    /**
     * Pronalazi korisnika na osnovu njegovog ID-a.
     *
     * @param id ID korisnika.
     * @return Entitet korisnika kao {@link UserEntity}.
     */
    UserEntity findById(String id);

    /**
     * Pronalazi korisnika na osnovu korisničkog imena.
     *
     * @param username Korisničko ime korisnika.
     * @return Entitet korisnika kao {@link UserEntity}.
     */
    UserEntity findByUsername(String username);

    /**
     * Pronalazi korisnike čija korisnička imena sadrže određeni kriterijum pretrage.
     *
     * @param searchCriteria Kriterijum pretrage za korisnička imena.
     * @return Lista odgovora sa korisnicima koji ispunjavaju kriterijum kao {@link List<UserResponse>}.
     */
    List<UserResponse> findUsersByUsernameContaining(String searchCriteria);

    /**
     * Pronalazi zajedničke prijatelje između trenutnog korisnika i drugog korisnika.
     *
     * @param currentUserFriends Lista ID-eva prijatelja trenutnog korisnika.
     * @param otherUserID       ID drugog korisnika.
     * @return Lista ID-eva zajedničkih prijatelja kao {@link List<String>}.
     */
    List<String> findMutualFriends(List<String> currentUserFriends, String otherUserID);
}
