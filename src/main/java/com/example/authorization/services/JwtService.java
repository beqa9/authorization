package com.example.authorization.services;

import com.example.authorization.entities.User;

import java.util.Map;

public interface JwtService {

    String generateAccessToken(User user);

    String generateRefreshToken(User user);

    boolean validateAccessToken(String token);

    boolean validateRefreshToken(String token);

    String extractUsername(String token);
}