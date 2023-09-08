package com.levi9.socialnetwork.service.friend;

/**
 * FriendService je interfejs koji definise metodu za brisanje prijatelja iz liste kontakata.
 * Koristi se za upravljanje prijateljstvima među korisnicima društvene mreže.
 */
public interface FriendService {

    /**
     * Uklanja korisnika iz liste prijatelja.
     *
     * @param userToBeDeleted Korisničko ime korisnika koji se uklanja iz liste prijatelja.
     */
    void remove(String userToBeDeleted);
}
