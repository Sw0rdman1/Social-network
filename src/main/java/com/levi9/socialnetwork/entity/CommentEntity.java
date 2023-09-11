
package com.levi9.socialnetwork.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


/**
 * Predstavlja entitet komentara u aplikaciji društvene mreže.
 */
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

    public void setText(String text) {
        if (text.length() < 3) {
            throw new IllegalArgumentException("Comment should be at least 3 characters long");
        }
        this.text = text;
    }

    @Override
    public String toString() {
        return "CommentEntity{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", post=" + post.getId() +
                ", creator=" + creator.getUsername() +
                ", dateTimeCreated=" + dateTimeCreated +
                '}';
    }
}
