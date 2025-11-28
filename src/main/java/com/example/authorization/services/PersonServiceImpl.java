package com.example.authorization.services;

import com.example.authorization.entities.Person;
import com.example.authorization.mappers.PersonMapper;
import com.example.authorization.models.PersonDto;
import com.example.authorization.repositories.PersonRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepo;
    private final PersonMapper personMapper;

    public PersonServiceImpl(PersonRepository personRepo, PersonMapper personMapper) {
        this.personRepo = personRepo;
        this.personMapper = personMapper;
    }

    @Override
    public PersonDto create(PersonDto dto) {
        Person p = personMapper.toEntity(dto);
        p = personRepo.save(p);
        return personMapper.toDto(p);
    }

    @Override
    public PersonDto get(Long id) {
        var p = personRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Person not found"));
        return personMapper.toDto(p);
    }

    @Override
    public List<PersonDto> getAll() {
        return personRepo.findAll().stream().map(personMapper::toDto).toList();
    }

    @Override
    public PersonDto update(Long id, PersonDto dto) {
        var existing = personRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Person not found"));
        var updated = personMapper.toEntity(dto);
        updated.setId(existing.getId());
        updated = personRepo.save(updated);
        return personMapper.toDto(updated);
    }

    @Override
    public void delete(Long id) {
        if (!personRepo.existsById(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Person not found");
        personRepo.deleteById(id);
    }
}