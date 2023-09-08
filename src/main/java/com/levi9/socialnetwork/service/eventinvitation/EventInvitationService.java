package com.levi9.socialnetwork.service.eventinvitation;

import com.levi9.socialnetwork.entity.EventEntity;
import com.levi9.socialnetwork.entity.EventInvitationEntity;
import com.levi9.socialnetwork.response.EventInvitationResponse;
import java.util.List;

/**
 * EventInvitationService je intefejs koje definiše metode za upravljanje pozivima za događaje u društvenoj mreži.
 * Koristi se za prihvatanje pozivnica, pronalaženje pozivnica po ID-u i slanje pozivnica za događaje.
 */
public interface EventInvitationService {

    /**
     * Prihvata pozivnicu za događaj na osnovu ID-a pozivnice.
     *
     * @param invitationId ID pozivnice koja se prihvata.
     * @return Objekat tipa EventInvitationResponse koji predstavlja prihvaćenu pozivnicu.
     */
    EventInvitationResponse acceptEventInvitation(Long invitationId);

    /**
     * Pronalazi pozivnicu za događaj na osnovu njenog ID-a.
     *
     * @param id ID pozivnice koja se traži.
     * @return Objekat tipa EventInvitationEntity koji predstavlja pronađenu pozivnicu.
     */
    EventInvitationEntity findById(Long id);

    /**
     * Šalje pozivnice za određeni događaj.
     *
     * @param event Objekat tipa EventEntity koji predstavlja događaj za koji se šalju pozivnice.
     * @return Lista pozivnica za događaj.
     */
    List<EventInvitationEntity> sendEventInvitations(EventEntity event);
}
