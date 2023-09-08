package com.levi9.socialnetwork.repository;

import com.levi9.socialnetwork.entity.EventEntity;
import com.levi9.socialnetwork.entity.EventInvitationEntity;
import com.levi9.socialnetwork.entity.EventInvitationStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EventInvitationRepository extends JpaRepository<EventInvitationEntity, Long> {
    List<EventInvitationEntity> getAllByEventAndStatus(EventEntity event, EventInvitationStatusEntity status);
}