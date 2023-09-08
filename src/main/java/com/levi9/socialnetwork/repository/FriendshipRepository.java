package com.levi9.socialnetwork.repository;

import com.levi9.socialnetwork.entity.FriendshipEntity;
import com.levi9.socialnetwork.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FriendshipRepository extends JpaRepository<FriendshipEntity, Long> {

    @Query(value = "select * from friendship where (user1_id=:user1Id AND user2_id=:user2Id) OR (user1_id=:user2Id AND user2_id=:user1Id)", nativeQuery = true)
    Optional<FriendshipEntity> findByPairOfIds(String user1Id, String user2Id);

    @Query("SELECT DISTINCT u.email " +
            "FROM UserEntity u " +
            "JOIN FriendshipEntity f ON u.id = f.user1.id OR u.id = f.user2.id " +
            "WHERE (f.user1.id = :userID OR f.user2.id = :userID) AND u.id != :userID")
    List<String> findUserFriendsEmails(@Param("userID") String userID);

    @Query("SELECT u.id " +
            "FROM UserEntity u JOIN FriendshipEntity f ON u.id=f.user1.id OR u.id=f.user2.id " +
            "WHERE (f.user1.id=:userId OR f.user2.id=:userId) AND u.id!=:userId")
    List<String> findUserFriends(@Param("userId") String userId);

}
