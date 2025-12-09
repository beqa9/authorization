package com.example.authorization.services;

import com.example.authorization.entities.RefreshToken;
import com.example.authorization.entities.User;
import com.example.authorization.models.AuthDtos.LoginRequest;
import com.example.authorization.models.AuthDtos.LoginResponse;
import com.example.authorization.models.AuthDtos.RefreshRequest;
import com.example.authorization.models.AuthDtos.RegisterRequest;
import com.example.authorization.repositories.RefreshTokenRepository;
import com.example.authorization.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final UserRepository userRepo;
    private final RefreshTokenRepository refreshRepo;
    private final PasswordEncoder passwordEncoder;
    private final UserSessionService userSessionService;

    public AuthServiceImpl(AuthenticationManager authManager,
                           JwtService jwtService,
                           UserRepository userRepo,
                           RefreshTokenRepository refreshRepo,
                           PasswordEncoder passwordEncoder,UserSessionService userSessionService) {
        this.authManager = authManager;
        this.jwtService = jwtService;
        this.userRepo = userRepo;
        this.refreshRepo = refreshRepo;
        this.passwordEncoder = passwordEncoder;
        this.userSessionService = userSessionService;
    }

    @Override
    public LoginResponse login(LoginRequest request, String ip, String userAgent) {
        var authToken = new UsernamePasswordAuthenticationToken(request.username(), request.password());
        var auth = authManager.authenticate(authToken);

        var user = userRepo.findDetailedByUsername(request.username())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Generate tokens
        String access = jwtService.generateAccessToken(user);
        String refresh = createAndStoreRefreshToken(user);

        // Save session
        userSessionService.createSession(user.getId(), ip, userAgent);

        return new LoginResponse(access, refresh, "Bearer");
    }

    private String createAndStoreRefreshToken(User user) {
        String token = UUID.randomUUID().toString();
        RefreshToken rt = new RefreshToken();
        rt.setToken(token);
        rt.setUser(user);
        rt.setExpiry(OffsetDateTime.now().plusDays(30));
        refreshRepo.save(rt);
        return token;
    }

    @Override
    public LoginResponse refreshToken(RefreshRequest request) {
        var found = refreshRepo.findByToken(request.refreshToken())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token"));
        if (found.getExpiry().isBefore(OffsetDateTime.now())) {
            refreshRepo.delete(found);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token expired");
        }
        var user = userRepo.findById(found.getUser().getId()).orElseThrow();
        String access = jwtService.generateAccessToken(user);
        return new LoginResponse(access, found.getToken(), "Bearer");
    }

    @Override
    public void register(RegisterRequest request) {
        if (userRepo.findByUsername(request.username()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "username exists");
        }
        User u = new User();
        u.setUsername(request.username());
        u.setPassword(passwordEncoder.encode(request.password()));
        u.setEmployeeId(request.employeeId());
        userRepo.save(u);
    }

    @Override
    public void logout(String refreshToken, String ip, String userAgent) {
        refreshRepo.findByToken(refreshToken).ifPresent(rt -> {
            // Revoke the latest session for this user from this IP/UA
            userSessionService.getActiveSessions(rt.getUser().getId())
                    .stream()
                    .filter(s -> !s.revoked() &&
                            (ip == null || s.ip().equals(ip)) &&
                            (userAgent == null || s.userAgent().equals(userAgent)))
                    .forEach(s -> userSessionService.revokeSession(s.id()));

            // Delete refresh token
            refreshRepo.delete(rt);
        });
    }
}