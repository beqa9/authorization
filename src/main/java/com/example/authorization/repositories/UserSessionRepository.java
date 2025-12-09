package com.example.authorization.repositories;

import com.example.authorization.entities.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserSessionRepository extends JpaRepository<UserSession, Long> {

    List<UserSession> findByUserId(Long userId);
    Optional<UserSession> findTopByUserIdOrderByLastSeenDesc(Long userId);

    List<UserSession> findByUserIdAndRevokedFalse(Long userId);
}