package com.codemainlabs.book_management_api.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BookRequestDTO(
        String title,
        String isbn,
        String synopsis,
        Long authorId,
        LocalDate publicationDate,
        String genre,
        Integer pageCount,
        String editorial
) {}

