package com.levi9.socialnetwork.repository;

import com.levi9.socialnetwork.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByUsername(String username);
    List<UserEntity> findAllByActiveIsTrueAndUsernameIsNot(String currentUserUsername);
    List<UserEntity> findAllByUsernameContainingIgnoreCaseAndActiveIsTrueAndUsernameIsNot(String searchCriteria, String currentUserUsername);
}