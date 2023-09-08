package com.levi9.socialnetwork.service.event;

import com.levi9.socialnetwork.entity.EventEntity;
import com.levi9.socialnetwork.response.EventResponse;
import java.time.LocalDateTime;
import java.util.List;

/**
 * EventService je interfejs koje definise metode za upravljanje događajima u društvenoj mreži.
 * Koristi se za pronalaženje događaja, kreiranje, ažuriranje i brisanje događaja.
 */
public interface EventService {

    /**
     * Pronalazi događaj po ID-u.
     *
     * @param id ID događaja koji se traži.
     * @return Objekat tipa EventEntity koji predstavlja pronađeni događaj.
     */
    EventEntity findById(Long id);

    /**
     * Ažurira datum i vreme postojećeg događaja.
     *
     * @param eventId     ID događaja koji se ažurira.
     * @param newDateTime Novi datum i vreme za događaj.
     * @return Objekat tipa EventEntity koji predstavlja ažurirani događaj.
     */
    EventEntity updateEvent(Long eventId, String newDateTime);

    /**
     * Kreira novi događaj za određenu grupu.
     *
     * @param groupId   ID grupe za koju se kreira događaj.
     * @param location  Lokacija događaja.
     * @param dateTime  Datum i vreme događaja.
     * @return Objekat tipa EventResponse koji predstavlja novi događaj.
     */
    EventResponse createEvent(Long groupId, String location, LocalDateTime dateTime);

    /**
     * Briše događaj na osnovu ID-a.
     *
     * @param eventId ID događaja koji se briše.
     */
    void remove(Long eventId);
}
