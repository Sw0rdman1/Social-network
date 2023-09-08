package com.levi9.socialnetwork.service.friendRequest;

import com.levi9.socialnetwork.response.FriendRequestResponse;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * FriendRequestService je interfejs koje definiše metode za upravljanje zahtevima za prijateljstvo u društvenoj mreži.
 * Koristi se za stvaranje, primanje, prihvatanje i odbijanje zahtjeva za prijateljstvo.
 */
@Service
public interface FriendRequestService {

    /**
     * Kreira novi zahtev za prijateljstvo prema korisniku čije je korisničko ime primljeno kao parametar.
     *
     * @param receiverUsername Korisničko ime korisnika koji će primiti zahtev za prijateljstvo.
     * @return Jedinstveni identifikator zahteva za prijateljstvo.
     */
    String createFriendRequest(String receiverUsername);

    /**
     * Dohvata listu čekajućih zahtev za prijateljstvo.
     *
     * @return Lista zahtev za prijateljstvo koji su u statusu čekanja.
     */
    List<FriendRequestResponse> getPendingFriendRequests();

    /**
     * Prihvata zahteva za prijateljstvo od strane korisnika čije je korisničko ime primljeno kao parametar.
     *
     * @param senderUsername Korisničko ime korisnika koji je poslao zahtev za prijateljstvo.
     * @return Poruka koja potvrđuje prihvatanje zahteva za prijateljstvo.
     */
    String acceptFriendRequest(String senderUsername);

    /**
     * Odbijanje zahteva za prijateljstvo od strane korisnika čije je korisničko ime primljeno kao parametar.
     *
     * @param senderUsername Korisničko ime korisnika koji je poslao zahtev za prijateljstvo.
     * @return Poruka koja potvrđuje odbijanje zahteva za prijateljstvo.
     */
    String declineFriendRequest(String senderUsername);
}
