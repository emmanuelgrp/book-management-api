package com.codemainlabs.book_management_api.assembler;

import com.codemainlabs.book_management_api.model.dto.AuthorIDDTO;
import org.springframework.hateoas.EntityModel;

import java.time.LocalDate;
import java.util.List;

// Esta clase representa cómo se verá UN libro en la respuesta JSON final
// Nota que 'authors' es List<EntityModel<AuthorIDDTO>>
public record BookRepresentation(
        Long bookID,
        String title,
        String isbn,
        String synopsis,
        LocalDate publicationDate,
        String genre,
        Integer pageCount,
        List<EntityModel<AuthorIDDTO>> authors // <-- Cambio clave aquí
) {
    // No necesitas @Builder aquí necesariamente,
    // lo construiremos en el Assembler
}