package com.example.authorization.controllers;

import com.example.authorization.entities.Authority;
import com.example.authorization.entities.Role;
import com.example.authorization.models.AuthorityDto;
import com.example.authorization.models.RoleDto;
import com.example.authorization.repositories.AuthorityRepository;
import com.example.authorization.repositories.RoleRepository;
import com.example.authorization.services.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/authorities")
    public ResponseEntity<List<AuthorityDto>> listAuthorities() {
        return ResponseEntity.ok(adminService.getAuthorities());
    }

    @PostMapping("/authorities")
    public ResponseEntity<AuthorityDto> createAuthority(@RequestBody AuthorityDto dto) {
        return ResponseEntity.status(201).body(adminService.createAuthority(dto));
    }

    @GetMapping("/roles")
    public ResponseEntity<List<RoleDto>> listRoles() {
        return ResponseEntity.ok(adminService.getRoles());
    }

    @PostMapping("/roles")
    public ResponseEntity<RoleDto> createRole(@RequestBody RoleDto dto) {
        return ResponseEntity.status(201).body(adminService.createRole(dto));
    }

    @PostMapping("/roles/{roleId}/assign/{authorityId}")
    public ResponseEntity<Void> assignAuthority(@PathVariable Long roleId, @PathVariable Long authorityId) {
        adminService.assignAuthority(roleId, authorityId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/roles/{roleId}/remove/{authorityId}")
    public ResponseEntity<Void> removeAuthority(@PathVariable Long roleId, @PathVariable Long authorityId) {
        adminService.removeAuthority(roleId, authorityId);
        return ResponseEntity.ok().build();
    }
}
