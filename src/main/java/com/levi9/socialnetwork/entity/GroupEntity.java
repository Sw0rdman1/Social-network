package com.levi9.socialnetwork.entity;

import jakarta.persistence.*;
import lombok.*;


/**
 * Predstavlja entitet društvene grupe u aplikaciji.
 */
@Entity
@Table(name = "social_group")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class GroupEntity {

    /**
     * Jedinstveni identifikator društvene grupe.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Ime društvene grupe.
     * Ono je jedinstveno i ne može biti null.
     */
    @Column(unique = true, nullable = false)
    private String name;

    public void setName(String name) {
        if (name.length() < 2) {
            throw new IllegalArgumentException("Name must be at least 2 characters long");
        }
        this.name = name;
    }

    /**
     * Logicka promenljiva koja označava da li je grupa privatna ili javna.
     * Ako je grupa privatna mora se poslati yahtev ya uclanjenje, u suprotnom svako se moye uclaniti u nju
     */
    @Column
    private boolean closed;

    /**
     * Administrator društvene grupe.
     */
    @ManyToOne
    @JoinColumn(name = "admin_id")
    private UserEntity admin;
}
