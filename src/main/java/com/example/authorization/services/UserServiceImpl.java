package com.example.authorization.services;

import com.example.authorization.entities.User;
import com.example.authorization.mappers.UserMapper;
import com.example.authorization.models.UserDto;
import com.example.authorization.repositories.RoleRepository;
import com.example.authorization.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder encoder;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepo, RoleRepository roleRepo, PasswordEncoder encoder, UserMapper userMapper) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.encoder = encoder;
        this.userMapper = userMapper;
    }

    @Override
    public UserDto createUser(UserDto dto) {
        if (userRepo.findByUsername(dto.username()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "username exists");
        }

        var defaultRole = roleRepo.findByName("ROLE_USER")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Default role ROLE_USER not found in database"));

        User u = new User();
        u.setUsername(dto.username());
        u.setPassword(encoder.encode(dto.password()));
        u.setEmployeeId(dto.employeeId());

        u.setRoles(new java.util.HashSet<>());
        u.getRoles().add(defaultRole);

        userRepo.save(u);
        return userMapper.toDto(u);
    }

    @Override
    public UserDto getUser(Long id) {
        var u = userRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return userMapper.toDto(u);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepo.findAll().stream().map(userMapper::toDto).toList();
    }

    @Override
    public UserDto addRoleToUser(Long userId, String roleName) {
        var u = userRepo.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        var r = roleRepo.findByName(roleName).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));
        u.getRoles().add(r);
        userRepo.save(u);
        return userMapper.toDto(u);
    }

    @Override
    public void removeRoleFromUser(Long userId, String roleName) {
        var u = userRepo.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        u.getRoles().removeIf(r -> r.getName().equals(roleName));
        userRepo.save(u);
    }
}