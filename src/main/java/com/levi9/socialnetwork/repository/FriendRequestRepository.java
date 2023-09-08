package com.levi9.socialnetwork.repository;

import com.levi9.socialnetwork.entity.FriendRequestEntity;
import com.levi9.socialnetwork.entity.RequestStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequestEntity, Long> {

    @Query(value = "select * from friend_request where (sender_id=:senderId AND receiver_id=:receiverId)", nativeQuery = true)
    Optional<FriendRequestEntity> findByPairOfIds(String senderId, String receiverId);

    @Query(value = "select * from friend_request where (sender_id=:senderId AND receiver_id=:receiverId AND status='PENDING')", nativeQuery = true)
    Optional<FriendRequestEntity> findPendingFriendRequest(String senderId, String receiverId);

    List<FriendRequestEntity> findByReceiverIdAndStatus(String receiverId, RequestStatusEntity requestStatus);
}
