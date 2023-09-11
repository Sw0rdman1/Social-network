package com.levi9.socialnetwork.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Predstavlja entitet člana grupe u aplikaciji društvene mreže.
 */
@Entity
@Table(name = "group_member")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class GroupMemberEntity {

    /**
     * Jedinstveni identifikator člana grupe.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Korisnik koji je član grupe.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity member;

    /**
     * Grupa članstva korisnika.
     */
    @ManyToOne
    @JoinColumn(name = "group_id")
    private GroupEntity group;

    /**
     * Datum i vreme kada je korisnik postao član grupe.
     */
    @Column(name = "date_joined")
    private LocalDateTime dateTimeJoined;

    public void setDateTimeJoined(LocalDateTime dateTimeJoined) {
        if (dateTimeJoined.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Date must be in past");
        }
        this.dateTimeJoined = dateTimeJoined;
    }

    @Override
    public String toString() {
        return "GroupMemberEntity{" +
                "id=" + id +
                ", member=" + member.getUsername() +
                ", group=" + group.getName() +
                ", dateTimeJoined=" + dateTimeJoined +
                '}';
    }
}
