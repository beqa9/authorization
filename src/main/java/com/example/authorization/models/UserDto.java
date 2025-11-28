package com.example.authorization.models;

import lombok.Builder;

import java.util.List;

@Builder
public record UserDto(
        Long id,
        Long employeeId,
        String username,
        String password,
        List<String> roles
) {}