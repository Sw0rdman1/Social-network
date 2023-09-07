/**
 * Predstavlja entitet događaja u aplikaciji društvene mreže.
 */
package com.levi9.socialnetwork.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "event")
@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class EventEntity {

    /**
     * Jedinstveni identifikator događaja.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Datum i vreme događaja.
     */
    @Column(name = "date", nullable = false)
    private LocalDateTime dateTime;

    /**
     * Lokacija događaja.
     */
    @Column(nullable = false)
    private String location;

    /**
     * Grupa kojoj događaj pripada.
     */
    @ManyToOne
    @JoinColumn(name = "group_id")
    private GroupEntity group;

    /**
     * Član grupe koji je kreirao događaj.
     */
    @ManyToOne
    @JoinColumn(name = "creator_id")
    private GroupMemberEntity creator;
}
