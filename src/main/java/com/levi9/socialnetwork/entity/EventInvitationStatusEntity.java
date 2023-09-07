/**
 * Enumeracija koja predstavlja status poziva na događaj u aplikaciji društvene mreže.
 * Statusi uključuju DA DOLAZIM i NE DOLAZIM.
 */
package com.levi9.socialnetwork.entity;

public enum EventInvitationStatusEntity {
    /**
     * Status poziva na događaj koji označava da korisnik ne dolazi na događaj.
     */
    NOT_COMING,

    /**
     * Status poziva na događaj koji označava da korisnik dolazi na događaj.
     */
    COMING
}
