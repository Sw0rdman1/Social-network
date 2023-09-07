/**
 * Predstavlja entitet zahteva za pridruživanje grupi u aplikaciji društvene mreže.
 */
package com.levi9.socialnetwork.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "group_request")
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Builder
public class GroupRequestEntity {

    /**
     * Jedinstveni identifikator zahteva za pridruživanje grupi.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Korisnik koji je poslao zahtev za pridruživanje.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    /**
     * Grupa za koju je poslat zahtev za pridruživanje.
     */
    @ManyToOne
    @JoinColumn(name = "group_id")
    private GroupEntity group;

    /**
     * Status zahteva za pridruživanje grupi (ODBIJEN ili NA ČEKANJU).
     */
    @Column(columnDefinition = "ENUM('REJECTED', 'PENDING')")
    @Enumerated(EnumType.STRING)
    private RequestStatusEntity status;

    /**
     * Logicka promenljiva koja označava da li je zahtev poslao administrator grupe.
     */
    @Column(name = "admin_sent_request")
    private boolean adminSentRequest;

    /**
     * Brojač zahteva za pridruživanje.
     * Nakon 3 odbijena zahteva korisnik ne moye vise slati yahteve.
     */
    @Column(name = "request_counter")
    private Integer requestCounter;
}
