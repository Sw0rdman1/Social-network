package com.levi9.socialnetwork.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;


/**
 * Predstavlja entitet posta u aplikaciji društvene mreže.
 */
@Entity
@Table(name = "post")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
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

    public void setText(String text) {
        if (text.length() < 10) {
            throw new IllegalArgumentException("Post should be at least 10 characters long");
        }
        this.text = text;
    }

    public void setDateTimeCreated(LocalDateTime dateTimeCreated) {
        if (dateTimeCreated.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Date must be in past");
        }
        this.dateTimeCreated = dateTimeCreated;
    }



    @Override
    public String toString() {
        return "PostEntity{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", dateTimeCreated=" + dateTimeCreated +
                ", closed=" + closed +
                ", deleted=" + deleted +
                ", group=" + group.getName() +
                ", creator=" + creator.getUsername() +
                '}';
    }
}
