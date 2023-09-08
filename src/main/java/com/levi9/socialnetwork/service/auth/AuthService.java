package com.levi9.socialnetwork.service.auth;

import com.levi9.socialnetwork.response.UserResponse;

/**
 * AuthService je interfejs koje definiše metode za autentifikaciju i registraciju korisnika.
 * Koristi se za upravljanje autentifikacijom i registracijom korisnika u društvenoj mreži.
 */
public interface AuthService {

    /**
     * Registruje novog korisnika u sistemu.
     *
     * @param username Korisničko ime novog korisnika.
     * @param password Lozinka novog korisnika.
     * @param email    E-mail adresa novog korisnika.
     * @return Objekat tipa UserResponse koji predstavlja registrovanog korisnika.
     */
    UserResponse registerUser(String username, String password, String email);

    /**
     * Dohvata pristupni token za autentifikaciju korisnika na osnovu korisničkog imena i lozinke.
     *
     * @param username Korisničko ime korisnika.
     * @param password Lozinka korisnika.
     * @return Pristupni token kao String.
     */
    String getAccessToken(final String username, final String password);

    /**
     * Dohvata informacije o prijavljenom korisniku na osnovu e-mail adrese i pristupnog tokena.
     *
     * @param email E-mail adresa prijavljenog korisnika.
     * @param token Pristupni token prijavljenog korisnika.
     * @return Objekat tipa UserResponse koji predstavlja prijavljenog korisnika.
     */
    UserResponse returnLoggedInUser(String email, String token);
}
