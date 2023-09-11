
package com.levi9.socialnetwork.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;


/**
 * Predstavlja entitet odgovora na komentar u aplikaciji društvene mreže.
 */
@Entity
@Table(name = "comment_reply")
@Getter
@Setter
@NoArgsConstructor
public class CommentReplyEntity {

    /**
     * Jedinstveni identifikator odgovora na komentar.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Tekst odgovora na komentar.
     */
    @Column(columnDefinition = "text")
    private String text;

    /**
     * Komentar na koji je odgovoreno.
     */
    @ManyToOne
    @JoinColumn(name = "comment_id")
    private CommentEntity comment;

    /**
     * Korisnik koji je kreirao odgovor na komentar.
     */
    @ManyToOne
    @JoinColumn(name = "creator_id")
    private UserEntity creator;

    /**
     * Datum i vreme kada je odgovor na komentar kreiran.
     */
    @Column(name = "date_created")
    private LocalDateTime dateTimeCreated;

    public void setText(String text) {
        if (text.length() < 3) {
            throw new IllegalArgumentException("Comment reply should be at least 3 characters long");
        }
        this.text = text;
    }

    @Override
    public String toString() {
        return "CommentReplyEntity{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", comment=" + comment.getText() +
                ", creator=" + creator.getUsername() +
                ", dateTimeCreated=" + dateTimeCreated +
                '}';
    }
}
