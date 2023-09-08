package com.levi9.socialnetwork.repository;

import com.levi9.socialnetwork.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long> {

    @Query(value = "SELECT * FROM event WHERE date BETWEEN :start AND :end", nativeQuery = true)
    List<EventEntity> findEventsStartingBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    Optional<EventEntity> findById(Long eventId);
}