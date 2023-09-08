package com.levi9.socialnetwork.repository;

import com.levi9.socialnetwork.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> { }
