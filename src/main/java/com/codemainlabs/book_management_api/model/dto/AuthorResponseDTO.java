package com.codemainlabs.book_management_api.model.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record AuthorResponseDTO(
        Long id,
        String name,
        String biography,
        int bookCount,
        LocalDate birthDate,
        String nationality,
        boolean isAlive,
        LocalDate deathDate
) {}
