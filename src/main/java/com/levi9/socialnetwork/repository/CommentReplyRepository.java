package com.levi9.socialnetwork.repository;

import com.levi9.socialnetwork.entity.CommentReplyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentReplyRepository extends JpaRepository<CommentReplyEntity, Long> {
}
