package com.example.authorization.mappers;

import com.example.authorization.entities.UserSession;
import com.example.authorization.models.UserSessionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserSessionMapper {

    default UserSessionDto toDto(UserSession session) {
        if (session == null) return null;

        return UserSessionDto.builder()
                .id(session.getId())
                .userId(session.getUser() != null ? session.getUser().getId() : null)
                .ip(session.getIp())
                .userAgent(session.getUserAgent())
                .createdAt(session.getCreatedAt() != null ? session.getCreatedAt().toInstant() : null)
                .lastSeen(session.getLastSeen() != null ? session.getLastSeen().toInstant() : null)
                .expiresAt(session.getExpiresAt() != null ? session.getExpiresAt().toInstant() : null)
                .revoked(session.getRevoked() != null && session.getRevoked())
                .build();
    }
}