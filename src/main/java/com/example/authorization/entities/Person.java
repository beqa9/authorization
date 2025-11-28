package com.example.authorization.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "persons")
public class Person {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String middleName;
    private String lastName;
    @Column(unique = true)
    private String email;
    private String phone;
    private String address;
    private LocalDate birthDate;
    private String nationality;
    private String documentNumber;
    @Column(columnDefinition = "text")
    private String notes;
}