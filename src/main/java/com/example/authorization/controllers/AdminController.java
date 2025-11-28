package com.example.authorization.controllers;

import com.example.authorization.entities.Authority;
import com.example.authorization.entities.Role;
import com.example.authorization.repositories.AuthorityRepository;
import com.example.authorization.repositories.RoleRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AuthorityRepository authorityRepo;
    private final RoleRepository roleRepo;

    public AdminController(AuthorityRepository authorityRepo, RoleRepository roleRepo) {
        this.authorityRepo = authorityRepo;
        this.roleRepo = roleRepo;
    }

    @GetMapping("/authorities")
    public ResponseEntity<List<Authority>> listAuthorities() { return ResponseEntity.ok(authorityRepo.findAll()); }

    @PostMapping("/authorities")
    public ResponseEntity<Authority> createAuthority(@RequestBody Authority a) { return ResponseEntity.status(201).body(authorityRepo.save(a)); }

    @GetMapping("/roles")
    public ResponseEntity<List<Role>> listRoles() { return ResponseEntity.ok(roleRepo.findAll()); }

    @PostMapping("/roles")
    public ResponseEntity<Role> createRole(@RequestBody Role r) { return ResponseEntity.status(201).body(roleRepo.save(r)); }

    @PostMapping("/roles/{roleId}/assign/{authorityId}")
    public ResponseEntity<Void> assign(@PathVariable Long roleId, @PathVariable Long authorityId) {
        var r = roleRepo.findById(roleId).orElseThrow();
        var a = authorityRepo.findById(authorityId).orElseThrow();
        r.getAuthorities().add(a);
        roleRepo.save(r);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/roles/{roleId}/remove/{authorityId}")
    public ResponseEntity<Void> remove(@PathVariable Long roleId, @PathVariable Long authorityId) {
        var r = roleRepo.findById(roleId).orElseThrow();
        r.getAuthorities().removeIf(x -> x.getId().equals(authorityId));
        roleRepo.save(r);
        return ResponseEntity.ok().build();
    }
}