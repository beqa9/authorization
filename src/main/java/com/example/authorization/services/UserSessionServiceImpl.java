package com.example.authorization.services;

import com.example.authorization.entities.User;
import com.example.authorization.entities.UserSession;
import com.example.authorization.mappers.UserSessionMapper;
import com.example.authorization.models.UserSessionDto;
import com.example.authorization.repositories.UserRepository;
import com.example.authorization.repositories.UserSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserSessionServiceImpl implements UserSessionService {

    private final UserRepository userRepo;
    private final UserSessionRepository sessionRepo;
    private final UserSessionMapper mapper;

    @Override
    @Transactional
    public UserSessionDto createSession(Long userId, String ip, String userAgent) {

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserSession session = new UserSession();
        session.setUser(user);
        session.setIp(ip);
        session.setUserAgent(userAgent);
        session.setCreatedAt(OffsetDateTime.now());
        session.setLastSeen(OffsetDateTime.now());
        session.setExpiresAt(OffsetDateTime.now().plusDays(7));
        session.setRevoked(false);

        sessionRepo.save(session);
        return mapper.toDto(session);
    }

    @Override
    public List<UserSessionDto> getActiveSessions(Long userId) {
        return sessionRepo.findByUserIdAndRevokedFalse(userId)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public void revokeSession(Long sessionId) {
        sessionRepo.findById(sessionId).ifPresent(s -> {
            s.setRevoked(true);
            sessionRepo.save(s);
        });
    }
}