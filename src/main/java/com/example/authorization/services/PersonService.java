package com.example.authorization.services;

import com.example.authorization.models.PersonDto;

import java.util.List;

public interface PersonService {
    PersonDto create(PersonDto dto);
    PersonDto get(Long id);
    List<PersonDto> getAll();
    PersonDto update(Long id, PersonDto dto);
    void delete(Long id);
}