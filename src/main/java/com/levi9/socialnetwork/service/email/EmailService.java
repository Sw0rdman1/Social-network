package com.levi9.socialnetwork.service.email;

import com.levi9.socialnetwork.entity.EventInvitationEntity;

/**
 * EmailService je interfejs koje definise metode za slanje e-mail obaveštenja vezanih za događaje.
 * Koristi se za slanje obaveštenja i podsetnika o događajima putem e-pošte.
 */
public interface EmailService {

    /**
     * Šalje e-mail obaveštenje za događaj.
     *
     * @param eventInvitation Objekat tipa EventInvitationEntity koji predstavlja pozivnicu za događaj.
     */
    void sendEventNotification(EventInvitationEntity eventInvitation);

    /**
     * Šalje e-mail podsetnik za događaj.
     *
     * @param eventInvitation Objekat tipa EventInvitationEntity koji predstavlja pozivnicu za događaj.
     */
    void sendEventReminder(EventInvitationEntity eventInvitation);
}
