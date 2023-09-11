package com.levi9.socialnetwork.entity;

import jakarta.persistence.*;
import lombok.*;


/**
 * Predstavlja entitet koji označava postove koji su skriveni od određenih korisnika u aplikaciji.
 */
@Entity
@Table(name = "post_hidden_from")
@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class PostHiddenFromEntity {

    /**
     * Jedinstveni identifikator za entitet posta koji je skriven od određenih korisnika.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Korisnik od koga je skriven post.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    /**
     * Post koji je skriven od određenog korisnika.
     */
    @ManyToOne
    @JoinColumn(name = "post_id")
    private PostEntity post;

    @Override
    public String toString() {
        return "PostHiddenFromEntity{" +
                "id=" + id +
                ", user=" + user.getUsername() +
                ", post=" + post.getId() +
                '}';
    }
}
