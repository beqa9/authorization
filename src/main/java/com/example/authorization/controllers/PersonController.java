package com.example.authorization.controllers;


import com.example.authorization.models.PersonDto;
import com.example.authorization.services.PersonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/persons")
public class PersonController {

    private final PersonService personService;
    public PersonController(PersonService personService) { this.personService = personService; }

    @GetMapping
    public ResponseEntity<List<PersonDto>> getAll() { return ResponseEntity.ok(personService.getAll()); }

    @GetMapping("/{id}")
    public ResponseEntity<PersonDto> get(@PathVariable Long id) { return ResponseEntity.ok(personService.get(id)); }

    @PostMapping
    public ResponseEntity<PersonDto> create(@RequestBody PersonDto dto) {
        return ResponseEntity.status(201).body(personService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonDto> update(@PathVariable Long id, @RequestBody PersonDto dto) {
        return ResponseEntity.ok(personService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        personService.delete(id);
        return ResponseEntity.noContent().build();
    }
}