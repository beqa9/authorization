package com.example.authorization.models.AuthDtos;

public record LoginResponse(String accessToken, String refreshToken, String tokenType) {}
