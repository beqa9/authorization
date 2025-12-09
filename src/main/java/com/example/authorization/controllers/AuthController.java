package com.example.authorization.controllers;

import com.example.authorization.models.AuthDtos.LoginRequest;
import com.example.authorization.models.AuthDtos.LoginResponse;
import com.example.authorization.models.AuthDtos.RefreshRequest;
import com.example.authorization.models.AuthDtos.RegisterRequest;
import com.example.authorization.models.UserDto;
import com.example.authorization.services.AuthService;
import com.example.authorization.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest req,
            HttpServletRequest httpReq) {

        String ip = httpReq.getRemoteAddr();
        String userAgent = httpReq.getHeader("User-Agent");

        var resp = authService.login(req, ip, userAgent);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(@RequestBody RefreshRequest req) {
        var resp = authService.refreshToken(req);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        // Use UserService to create user to ensure password encoding
        var userDto = UserDto.builder()

                .employeeId(req.employeeId())
                .username(req.username())
                .password(req.password())
                .build();
        userService.createUser(userDto);
        return ResponseEntity.status(201).build();
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            @RequestBody RefreshRequest req,
            HttpServletRequest httpReq) {

        String ip = httpReq.getRemoteAddr();
        String userAgent = httpReq.getHeader("User-Agent");

        authService.logout(req.refreshToken(), ip, userAgent);
        return ResponseEntity.ok().build();
    }
}