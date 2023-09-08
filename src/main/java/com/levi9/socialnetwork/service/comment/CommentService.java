package com.levi9.socialnetwork.service.comment;

import com.levi9.socialnetwork.entity.CommentEntity;
import com.levi9.socialnetwork.request.CommentRequest;
import com.levi9.socialnetwork.response.CommentResponse;

/**
 * CommentService je interfejs koje definira metode za upravljanje komentarima u društvenoj mreži.
 * Koristi se za kreiranje, čuvanje, brisanje i odgovaranje na komentare.
 */
public interface CommentService {

    /**
     * Kreira novi komentar na osnovu zahtjeva i ID-a posta.
     *
     * @param commentRequest Zahtjev koji sadrži informacije o komentaru.
     * @param postID         ID posta na koji se komentar odnosi.
     * @return Objekt tipa CommentResponse koji predstavlja novi komentar.
     */
    CommentResponse createComment(CommentRequest commentRequest, Long postID);

    /**
     * Cuva komentar u bazu podataka.
     *
     * @param comment Komentar koji se treba sacuvati.
     * @return Sacuvani komentar kao CommentEntity.
     */
    CommentEntity saveComment(CommentEntity comment);

    /**
     * Briše komentar na osnovu njegovog ID-a.
     *
     * @param commentID ID komentara koji se treba obrisati.
     */
    void deleteComment(Long commentID);

    /**
     * Dodaje odgovor na postojeći komentar.
     *
     * @param commentId ID komentara na koji se odgovara.
     * @param text      Tekst odgovora.
     */
    void reply(Long commentId, String text);
}
