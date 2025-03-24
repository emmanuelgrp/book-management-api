package com.codemainlabs.book_management_api.model.dto;

import com.codemainlabs.book_management_api.model.entity.Author;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record BookResponseDTO(
        Long id,
        String title,
        String isbn,
        String synopsis,
        String authorName, // Solo incluimos el nombre del autor
        LocalDate publicationDate,
        BigDecimal price,
        String genre,
        Integer pageCount
) {}
