package com.example.authorization.services;

import com.example.authorization.models.UserSessionDto;

import java.util.List;


public interface UserSessionService {

    UserSessionDto createSession(Long userId, String ip, String userAgent);

    List<UserSessionDto> getActiveSessions(Long userId);

    void revokeSession(Long sessionId);
}