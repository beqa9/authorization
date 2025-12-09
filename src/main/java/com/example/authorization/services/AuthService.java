package com.example.authorization.services;

import com.example.authorization.models.AuthDtos.LoginRequest;
import com.example.authorization.models.AuthDtos.LoginResponse;
import com.example.authorization.models.AuthDtos.RefreshRequest;
import com.example.authorization.models.AuthDtos.RegisterRequest;

public interface AuthService {
    LoginResponse login(LoginRequest request, String ip, String userAgent);
    LoginResponse refreshToken(RefreshRequest request);
    void register(RegisterRequest request);
    void logout(String refreshToken, String ip, String userAgent);
}