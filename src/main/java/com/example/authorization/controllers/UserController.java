package com.example.authorization.controllers;

import com.example.authorization.models.UserDto;
import com.example.authorization.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    public UserController(UserService userService) { this.userService = userService; }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAll() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> get(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @PostMapping
    public ResponseEntity<UserDto> create(@RequestBody UserDto dto) {
        return ResponseEntity.status(201).body(userService.createUser(dto));
    }

    @PostMapping("/{id}/roles/{roleName}")
    public ResponseEntity<UserDto> addRole(@PathVariable Long id, @PathVariable String roleName) {
        return ResponseEntity.ok(userService.addRoleToUser(id, roleName));
    }

    @DeleteMapping("/{id}/roles/{roleName}")
    public ResponseEntity<Void> removeRole(@PathVariable Long id, @PathVariable String roleName) {
        userService.removeRoleFromUser(id, roleName);
        return ResponseEntity.noContent().build();
    }
}