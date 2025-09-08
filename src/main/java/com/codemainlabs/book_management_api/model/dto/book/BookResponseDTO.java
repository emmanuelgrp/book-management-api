package com.codemainlabs.book_management_api.model.dto.book;

import com.codemainlabs.book_management_api.model.dto.author.AuthorIDDTO;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record BookResponseDTO(
        Long bookID,
        String title,
        String isbn,
        String synopsis,
        LocalDate publicationDate,
        String genre,
        Integer pageCount,
        List<AuthorIDDTO> authors
) {}

