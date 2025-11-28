package com.example.authorization.services;

import com.example.authorization.models.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto dto);
    UserDto getUser(Long id);
    List<UserDto> getAllUsers();
    UserDto addRoleToUser(Long userId, String roleName);
    void removeRoleFromUser(Long userId, String roleName);
}