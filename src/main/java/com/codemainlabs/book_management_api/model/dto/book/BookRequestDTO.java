package com.codemainlabs.book_management_api.model.dto.book;

import java.time.LocalDate;
import java.util.List;

public record BookRequestDTO(
        String title,
        String isbn,
        String synopsis,
        List<Long> authorIds,
        LocalDate publicationDate,
        String genre,
        Integer pageCount,
        String editorial
) {}

