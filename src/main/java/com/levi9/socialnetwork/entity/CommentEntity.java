/**
 * Predstavlja entitet komentara u aplikaciji društvene mreže.
 */
package com.levi9.socialnetwork.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class CommentEntity {

    /**
     * Jedinstveni identifikator komentara.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Tekst komentara.
     */
    @Column(columnDefinition = "text")
    private String text;

    /**
     * Post na koji je ostavljen komentar.
     */
    @ManyToOne
    @JoinColumn(name = "post_id")
    private PostEntity post;

    /**
     * Korisnik koji je kreirao komentar.
     */
    @ManyToOne
    @JoinColumn(name = "creator_id")
    private UserEntity creator;

    /**
     * Datum i vreme kada je komentar kreiran.
     */
    @Column(name = "date_created")
    private LocalDateTime dateTimeCreated;
}
