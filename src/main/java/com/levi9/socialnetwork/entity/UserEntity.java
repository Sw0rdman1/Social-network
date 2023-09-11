package com.levi9.socialnetwork.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;


/**
 * Predstavlja entitet korisnika u aplikaciji.
 * Ova klasa je entitet koji je mapiran na tabelu "user" u bazi podataka.
 * Implementira UserDetails interfejs kako bi obezbedila detalje korisnika za Spring Security.
 */
@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString
public class UserEntity implements UserDetails {

    /**
     * Jedinstveni identifikator korisnika.
     */
    @Id
    private String id;

    /**
     * Korisničko ime korisnika.
     * Ono je jedinstveno i ne može biti null.
     */
    @Column(unique = true, nullable = false)
    private String username;

    /**
     * Email adresa korisnika.
     * Ona je jedinstvena i ne može biti null.
     */
    @Column(unique = true, nullable = false)
    private String email;

    /**
     * Lozinka korisnika.
     */
    @Column
    private String password;

    /**
     * Zastava koja označava da li je korisnik verifikovan.
     */
    @Column
    private Boolean active;

    /**
     * Vraća ovlašćenja dodeljena korisniku.
     * Ova metoda je deo UserDetails interfejsa.
     *
     * @return Kolekcija dodeljenih ovlašćenja (nije implementirano).
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    /**
     * Vraća nacin logovanja korisnika.
     * Ova metoda je deo UserDetails interfejsa.
     *
     * @return Korisničko ime.
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * Vraća da li je korisnički nalog istekao.
     * Ova metoda je deo UserDetails interfejsa.
     *
     * @return Uvek vraća true.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Vraća da li je korisnički nalog zaključan.
     * Ova metoda je deo UserDetails interfejsa.
     *
     * @return Uvek vraća true.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Vraća da li korisnički kredencijali (lozinka) nisu istekli.
     * Ova metoda je deo UserDetails interfejsa.
     *
     * @return Uvek vraća true.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Vraća da li je korisnik omogućen.
     * Ova metoda je deo UserDetails interfejsa.
     *
     * @return Uvek vraća true.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }


    public void setUsername(String username) {
        if (username.length() < 3) {
            throw new IllegalArgumentException("Username should be at least 3 characters long");
        }
        this.username = username;
    }

    public void setEmail(String email) {
        if (email.length() < 10 || !email.contains("@")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        this.email = email;
    }

    public void setPassword(String password) {
        if (password.length() < 8) {
            throw new IllegalArgumentException("Password should be at least 8 characters long");
        }
        this.password = password;
    }
}
