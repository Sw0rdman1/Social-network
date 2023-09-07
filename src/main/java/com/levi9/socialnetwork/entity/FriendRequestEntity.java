/**
 * Predstavlja entitet zahteva za prijateljstvo između korisnika u aplikaciji.
 */
package com.levi9.socialnetwork.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "friend_request")
@NoArgsConstructor
@Getter
@Setter
public class FriendRequestEntity {

    /**
     * Jedinstveni identifikator zahteva za prijateljstvo.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Korisnik koji je poslao zahtev za prijateljstvo.
     */
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private UserEntity sender;

    /**
     * Korisnik koji je primio zahtev za prijateljstvo.
     */
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private UserEntity receiver;

    /**
     * Status zahteva za prijateljstvo (ODBIJEN ili NA ČEKANJU).
     */
    @Column(columnDefinition = "ENUM('REJECTED', 'PENDING')")
    @Enumerated(EnumType.STRING)
    private RequestStatusEntity status;

    /**
     * Brojač zahteva za prijateljstvo.
     * Nakon treceg puta korisnik ne moye slati vise.
     */
    @Column(name="request_counter")
    private Integer requestCounter;
}
