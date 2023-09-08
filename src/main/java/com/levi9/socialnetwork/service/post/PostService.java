package com.levi9.socialnetwork.service.post;

import com.levi9.socialnetwork.entity.PostEntity;
import com.levi9.socialnetwork.request.HiddenPostRequest;
import com.levi9.socialnetwork.request.PostModificationRequest;
import com.levi9.socialnetwork.request.PostRequest;
import com.levi9.socialnetwork.response.PostResponse;

import java.util.List;

/**
 * Interfejs koji definiše operacije za rad sa objavama (postovima) u društvenoj mreži.
 */
public interface PostService {

    /**
     * Kreira novu objavu na osnovu zahteva za objavom.
     *
     * @param postRequest Zahtev za kreiranje objave.
     * @return Entitet kreirane objave kao {@link PostEntity}.
     */
    PostEntity createPost(PostRequest postRequest);

    /**
     * Kreira novu objavu u okviru određene grupe na osnovu zahteva za objavom.
     *
     * @param postRequest Zahtev za kreiranje objave.
     * @param groupID     ID grupe u kojoj se kreira objava.
     * @return Odgovor na kreiranje objave kao {@link PostResponse}.
     */
    PostResponse createPostInGroup(PostRequest postRequest, Long groupID);

    /**
     * Čuva postojeću objavu.
     *
     * @param post Objava koja se čuva.
     * @return Sačuvani entitet objave kao {@link PostEntity}.
     */
    PostEntity savePost(PostEntity post);

    /**
     * Ažurira postojeću objavu na osnovu zahteva za izmenom.
     *
     * @param postModificationRequest Zahtev za izmenu objave.
     * @return Ažurirani entitet objave kao {@link PostEntity}.
     */
    PostEntity updatePost(PostModificationRequest postModificationRequest);

    /**
     * Briše objavu na osnovu njenog ID-a.
     *
     * @param postID ID objave koja se briše.
     */
    void deletePost(Long postID);

    /**
     * Dohvata listu objava za prikaz na korisnikovoj početnoj stranici (feed-u).
     *
     * @return Lista objava za prikaz kao {@link List<PostResponse>}.
     */
    List<PostResponse> getFeedPosts();

    /**
     * Dohvata listu objava sa profila određenog korisnika.
     *
     * @param profileUsername Korisničko ime profila čije se objave dohvataju.
     * @return Lista objava sa profila kao {@link List<PostResponse>}.
     */
    List<PostResponse> getUsersProfilePosts(String profileUsername);

    /**
     * Skriva objavu od određenih korisnika na osnovu zahteva za skrivanje objave.
     *
     * @param hiddenPostRequest Zahtev za skrivanje objave.
     * @return Poruka o uspešnom skrivanju objave.
     */
    String hidePostFromUsers(HiddenPostRequest hiddenPostRequest);

    /**
     * Ponovno prikazuje objavu korisnicima od kojih je bila sakrivena na osnovu zahteva za ponovno prikazivanje.
     *
     * @param hiddenPostRequest Zahtev za ponovno prikazivanje objave.
     * @return Poruka o uspešnom ponovnom prikazivanju objave.
     */
    String unhidePostFromUsers(HiddenPostRequest hiddenPostRequest);
}
