package com.levi9.socialnetwork.repository;

import com.levi9.socialnetwork.entity.PostHiddenFromEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PostHiddenFromRepository extends JpaRepository<PostHiddenFromEntity, Long> {

    List<PostHiddenFromEntity> findByUserId(String userId);

    List<PostHiddenFromEntity> findByUserIdAndPostId(String userId, Long postId);
}
