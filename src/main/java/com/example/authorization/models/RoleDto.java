package com.example.authorization.models;


import lombok.Builder;
import java.util.List;

@Builder
public record RoleDto(
        Long id,
        String name,
        String description,
        List<String> authorities
) {}