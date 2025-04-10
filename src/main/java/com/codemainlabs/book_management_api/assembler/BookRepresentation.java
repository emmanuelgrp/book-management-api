package com.codemainlabs.book_management_api.assembler;

import com.codemainlabs.book_management_api.model.dto.AuthorIDDTO;
import lombok.Builder;
import org.springframework.hateoas.EntityModel;

import java.time.LocalDate;
import java.util.List;

@Builder
public record BookRepresentation(
        Long bookID,
        String title,
        String isbn,
        String synopsis,
        LocalDate publicationDate,
        String genre,
        Integer pageCount,
        List<EntityModel<AuthorIDDTO>> authors
) {}