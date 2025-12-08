package com.example.authorization.models;

import lombok.Builder;

import java.time.OffsetDateTime;

@Builder
public record RefreshTokenDto(
        Long id,
        Long userId,
        Long sessionId,
        String token,
        OffsetDateTime expiry,
        OffsetDateTime createdAt
) {}