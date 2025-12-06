package com.example.authorization.models;

import lombok.Builder;

@Builder
public record AuthorityDto(
        Long id,
        String name,
        String description
) {}