package com.codemainlabs.book_management_api.model.dto;

import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record AuthorResponseDTO(
        Long authorID,
        String name,
        String nationality,
        int bookCount,
        List<BookResponseDTO> books,
        LocalDate birthDate,
        LocalDate deathDate,
        boolean isAlive,
        String city,
        String biography
) {}

