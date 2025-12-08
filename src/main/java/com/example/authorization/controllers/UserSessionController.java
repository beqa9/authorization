package com.example.authorization.controllers;


import com.example.authorization.models.UserSessionDto;
import com.example.authorization.services.UserSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
public class UserSessionController {

    private final UserSessionService sessionService;

    @PostMapping("/{userId}")
    public UserSessionDto createSession(
            @PathVariable Long userId,
            @RequestParam(required = false) String ip,
            @RequestParam(required = false, name = "ua") String userAgent
    ) {
        return sessionService.createSession(userId, ip, userAgent);
    }

    @GetMapping("/active/{userId}")
    public List<UserSessionDto> getActiveSessions(@PathVariable Long userId) {
        return sessionService.getActiveSessions(userId);
    }

    @PostMapping("/revoke/{sessionId}")
    public void revoke(@PathVariable Long sessionId) {
        sessionService.revokeSession(sessionId);
    }
}