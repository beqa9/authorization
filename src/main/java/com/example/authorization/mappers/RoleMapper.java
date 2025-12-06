package com.example.authorization.mappers;

import com.example.authorization.entities.Authority;
import com.example.authorization.entities.Role;
import com.example.authorization.models.RoleDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "authorities", expression = "java(mapAuthorities(entity.getAuthorities()))")
    RoleDto toDto(Role entity);

    default List<String> mapAuthorities(java.util.Set<Authority> authorities) {
        return authorities.stream()
                .map(Authority::getName)
                .toList();
    }
}
