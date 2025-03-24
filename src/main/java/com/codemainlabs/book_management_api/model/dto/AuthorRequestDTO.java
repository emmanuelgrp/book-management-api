package com.codemainlabs.book_management_api.model.dto;


import lombok.*;
import java.time.LocalDate;

@Builder
public record AuthorRequestDTO(
        Long id,
        String firstName,
        String lastName,
        LocalDate birthDate,
        LocalDate deathDate,
        String biography,
        String nationality
) {}
