package com.example.authorization.models;

import lombok.Builder;

import java.time.Instant;
import java.time.OffsetDateTime;


@Builder
public record UserSessionDto(
        Long id,
        Long userId,
        String ip,
        String userAgent,
        Instant createdAt,
        Instant lastSeen,
        Instant expiresAt,
        boolean revoked
) {}