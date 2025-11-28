package com.example.authorization.models;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record PersonDto(
        Long id,
        String firstName,
        String middleName,
        String lastName,
        String email,
        String phone,
        String address,
        LocalDate birthDate,
        String nationality,
        String documentNumber,
        String notes
) {}