package com.levi9.socialnetwork.repository;

import com.levi9.socialnetwork.entity.GroupEntity;
import com.levi9.socialnetwork.entity.GroupRequestEntity;
import com.levi9.socialnetwork.entity.RequestStatusEntity;
import com.levi9.socialnetwork.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRequestRepository extends JpaRepository<GroupRequestEntity, Long> {
    Optional<GroupRequestEntity> findByUserAndGroup(UserEntity user, GroupEntity group);
    List<GroupRequestEntity> findAllByGroupAndStatusAndAdminSentRequest(GroupEntity group,
                                                                        RequestStatusEntity status,
                                                                        boolean adminSentRequest);
    List<GroupRequestEntity> findAllByUserAndStatusAndAdminSentRequest(UserEntity user,
                                                                       RequestStatusEntity status,
                                                                       boolean adminSentRequest);
}