/**
 * Predstavlja entitet prijateljstva između korisnika u aplikaciji.
 */
package com.levi9.socialnetwork.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

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
}
