package com.levi9.socialnetwork.entity;

import jakarta.persistence.*;
import lombok.*;


/**
 * Predstavlja entitet poziva na događaj u aplikaciji društvene mreže.
 */
@Entity
@Table(name = "event_invitation")
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Builder
public class EventInvitationEntity {
    /**
     * Jedinstveni identifikator poziva na događaj.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Član grupe koji je pozvan na događaj.
     */
    @ManyToOne
    @JoinColumn(name = "invitee_id")
    private GroupMemberEntity invitee;

    /**
     * Događaj za koji je poslat poziv.
     */
    @ManyToOne
    @JoinColumn(name = "event_id")
    private EventEntity event;

    /**
     * Status poziva na događaj (NE DOLAZIM ili DOLAZIM).
     */
    @Column(columnDefinition = "ENUM('NOT_COMING', 'COMING')")
    @Enumerated(EnumType.STRING)
    private EventInvitationStatusEntity status;
}
