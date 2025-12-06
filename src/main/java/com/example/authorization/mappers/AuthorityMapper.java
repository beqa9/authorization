package com.example.authorization.mappers;

import com.example.authorization.entities.Authority;
import com.example.authorization.models.AuthorityDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthorityMapper {
    AuthorityDto toDto(Authority a);
    Authority toEntity(AuthorityDto dto);
}