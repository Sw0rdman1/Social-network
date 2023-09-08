package com.levi9.socialnetwork.repository;

import com.levi9.socialnetwork.entity.GroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<GroupEntity, Long> {

    Optional<GroupEntity> findByName(String groupName);

    List<GroupEntity> findAllByNameContaining(String searchCriteria);
}
