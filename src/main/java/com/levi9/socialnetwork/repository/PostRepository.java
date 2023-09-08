package com.levi9.socialnetwork.repository;

import com.levi9.socialnetwork.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {

    PostEntity save(PostEntity post);

    Optional<PostEntity> findById(Long id);

    List<PostEntity> findByCreatorIdInAndIdNotInOrderByDateTimeCreatedDesc(List<String> creatorId, List<Long> postId);

    List<PostEntity> findByGroupIdAndDeletedFalse(Long GroupId);

    List<PostEntity> findByCreatorIdAndIdNotInAndClosedIsFalseOrderByDateTimeCreatedDesc(String creatorId, List<Long> postId);
    
    @Query(value = "select * from post where deleted = false and date_created <= (localtimestamp() - interval 1 day)", nativeQuery = true)
    List<PostEntity> findExpiredPosts();
}