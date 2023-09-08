package com.levi9.socialnetwork.repository;

import com.levi9.socialnetwork.entity.GroupEntity;
import com.levi9.socialnetwork.entity.GroupMemberEntity;
import com.levi9.socialnetwork.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMemberEntity, Long> {

    Optional<GroupMemberEntity> findByMemberAndGroup(UserEntity member, GroupEntity group);

    @Query("SELECT m.member.email FROM GroupMemberEntity m WHERE m.group.id = :groupId")
    List<String> findMemberEmailsByGroupId(@Param("groupId") Long groupId);

    List<GroupMemberEntity> findByGroup(GroupEntity group);
}