/**
 * Enumeracija koja predstavlja status zahteve u aplikaciji društvene mreže.
 * Statusi uključuju ODBIJEN, NA ČEKANJU i PRIHVACEN.
 */
package com.levi9.socialnetwork.entity;

public enum RequestStatusEntity {
    /**
     * Status zahteva koji označava da je zahtev odbijen.
     */
    REJECTED,

    /**
     * Status zahteva koji označava da je zahtev na čekanju.
     */
    PENDING,

    /**
     * Status zahteva koji označava da je zahtev prihvaćen.
     */
    ACCEPTED;
}
