package com.example.authorization.mappers;

import com.example.authorization.entities.Role;
import com.example.authorization.entities.User;
import com.example.authorization.models.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "roles", expression = "java(mapRoles(entity.getRoles()))")
    UserDto toDto(User entity);

    default List<String> mapRoles(Set<Role> roles) {
        return roles.stream()
                .map(Role::getName)
                .toList();
    }
}