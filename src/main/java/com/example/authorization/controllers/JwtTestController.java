package com.example.authorization.controllers;

import com.example.authorization.entities.User;
import com.example.authorization.services.JwtService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JwtTestController {

    private final JwtService jwtService;

    public JwtTestController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @GetMapping("/test/generate")
    public String generateToken(@RequestParam String username) {
        User user = new User();
        user.setUsername(username);
        String token = jwtService.generateAccessToken(user);
        return "Access Token: " + token;
    }

    @GetMapping("/test/validate")
    public String validateToken(@RequestParam String token) {
        boolean valid = jwtService.validateAccessToken(token);
        if (!valid) return "Token is invalid!";
        String username = jwtService.extractUsername(token);
        return "Token is valid for user: " + username;
    }
}