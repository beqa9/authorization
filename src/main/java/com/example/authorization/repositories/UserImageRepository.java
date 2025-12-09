package com.example.authorization.repositories;

import com.example.authorization.entities.UserImage;
import com.example.authorization.entities.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserImageRepository extends JpaRepository<UserImage, Long> {
    List<UserImage> findBySessionId(Long sessionId);
}