package com.example.authorization.services;

import com.example.authorization.entities.Role;
import com.example.authorization.mappers.AuthorityMapper;
import com.example.authorization.mappers.RoleMapper;
import com.example.authorization.models.AuthorityDto;
import com.example.authorization.models.RoleDto;
import com.example.authorization.repositories.AuthorityRepository;
import com.example.authorization.repositories.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    private final AuthorityRepository authorityRepo;
    private final RoleRepository roleRepo;
    private final AuthorityMapper authorityMapper;
    private final RoleMapper roleMapper;

    public AdminServiceImpl(AuthorityRepository authorityRepo, RoleRepository roleRepo,
                            AuthorityMapper authorityMapper, RoleMapper roleMapper) {
        this.authorityRepo = authorityRepo;
        this.roleRepo = roleRepo;
        this.authorityMapper = authorityMapper;
        this.roleMapper = roleMapper;
    }

    @Override
    public List<AuthorityDto> getAuthorities() {
        return authorityRepo.findAll().stream()
                .map(authorityMapper::toDto)
                .toList();
    }

    @Override
    public AuthorityDto createAuthority(AuthorityDto dto) {
        var saved = authorityRepo.save(authorityMapper.toEntity(dto));
        return authorityMapper.toDto(saved);
    }

    @Override
    public List<RoleDto> getRoles() {
        return roleRepo.findAll().stream()
                .map(roleMapper::toDto)
                .toList();
    }

    @Override
    public RoleDto createRole(RoleDto dto) {
        Role r = new Role();
        r.setName(dto.name());
        r.setDescription(dto.description());
        var saved = roleRepo.save(r);
        return roleMapper.toDto(saved);
    }

    @Override
    public void assignAuthority(Long roleId, Long authorityId) {
        var role = roleRepo.findById(roleId).orElseThrow();
        var auth = authorityRepo.findById(authorityId).orElseThrow();
        role.getAuthorities().add(auth);
        roleRepo.save(role);
    }

    @Override
    public void removeAuthority(Long roleId, Long authorityId) {
        var role = roleRepo.findById(roleId).orElseThrow();
        role.getAuthorities().removeIf(a -> a.getId().equals(authorityId));
        roleRepo.save(role);
    }
}