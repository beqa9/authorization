package com.example.authorization.services;

import com.example.authorization.entities.Authority;
import com.example.authorization.entities.Role;
import com.example.authorization.entities.User;
import com.example.authorization.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository repo;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        User user = repo.findDetailedByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // gather authorities (roles + role authorities)
        Set<String> authorities = user.getRoles().stream()
                .flatMap(role -> {

                    String roleName = role.getName();
                    if (roleName == null) roleName = "";
                    if (!roleName.startsWith("ROLE_")) {
                        roleName = "ROLE_" + roleName;
                    }

                    // Authorities inside role
                    Set<String> authNames = role.getAuthorities().stream()
                            .map(Authority::getName)
                            .collect(Collectors.toSet());

                    return Stream.concat(Stream.of(roleName), authNames.stream());
                })
                .collect(Collectors.toSet());

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(authorities.toArray(String[]::new))
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(user.getEnabled() != null && !user.getEnabled())
                .build();
    }
}