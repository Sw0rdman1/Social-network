package com.levi9.socialnetwork.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;


/**
 * Predstavlja entitet prijateljstva između korisnika u aplikaciji.
 */
@Entity
@Table(name = "friendship")
@NoArgsConstructor
@Getter
@Setter
public class FriendshipEntity {

    /**
     * Jedinstveni identifikator prijateljstva.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Prvi korisnik u prijateljstvu.
     */
    @ManyToOne
    @JoinColumn(name = "user1_id")
    private UserEntity user1;

    /**
     * Drugi korisnik u prijateljstvu.
     */
    @ManyToOne
    @JoinColumn(name = "user2_id")
    private UserEntity user2;

    /**
     * Datum i vreme kada su korisnici postali prijatelji.
     */
    @Column(name = "date_connected")
    private LocalDateTime dateConnected;

    public void setDateConnected(LocalDateTime dateConnected) {
        if (dateConnected.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Date must be in past");
        }
        this.dateConnected = dateConnected;
    }

    @Override
    public String toString() {
        return "FriendshipEntity{" +
                "id=" + id +
                ", user1=" + user1.getUsername() +
                ", user2=" + user2.getUsername() +
                ", dateConnected=" + dateConnected +
                '}';
    }
}
