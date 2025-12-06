package com.example.authorization.services;

import com.example.authorization.models.AuthorityDto;
import com.example.authorization.models.RoleDto;

import java.util.List;

public interface AdminService {

    List<AuthorityDto> getAuthorities();
    AuthorityDto createAuthority(AuthorityDto dto);

    List<RoleDto> getRoles();
    RoleDto createRole(RoleDto dto);

    void assignAuthority(Long roleId, Long authorityId);
    void removeAuthority(Long roleId, Long authorityId);
}
