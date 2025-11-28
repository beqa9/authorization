package com.example.authorization.mappers;


import com.example.authorization.entities.Person;
import com.example.authorization.models.PersonDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PersonMapper {
    PersonDto toDto(Person p);
    Person toEntity(PersonDto dto);
}