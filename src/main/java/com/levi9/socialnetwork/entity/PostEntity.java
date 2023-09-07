/**
 * Predstavlja entitet posta u aplikaciji društvene mreže.
 */
package com.levi9.socialnetwork.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "post")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString
public class PostEntity {

    /**
     * Jedinstveni identifikator posta.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Tekst posta.
     */
    @Column(columnDefinition = "text")
    private String text;

    /**
     * Datum i vreme kada je post kreiran.
     */
    @Column(name = "date_created")
    private LocalDateTime dateTimeCreated;

    /**
     * Zastava koja označava da li je post zatvoren tj privatan.
     */
    @Column
    private boolean closed;

    /**
     * Logicka promenljiva koja označava da li je post automatski logicki obrisan nakon 24h.
     */
    @Column
    private boolean deleted;

    /**
     * Grupa kojoj post pripada.
     * Ako je ova vrednost null znaci da ne post ne pripada nijednoj grupi.
     */
    @ManyToOne
    @JoinColumn(name = "group_id")
    private GroupEntity group;

    /**
     * Kreator posta.
     */
    @ManyToOne
    @JoinColumn(name = "creator_id")
    private UserEntity creator;
}
