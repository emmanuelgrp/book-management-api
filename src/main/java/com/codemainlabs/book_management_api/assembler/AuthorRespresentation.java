package com.codemainlabs.book_management_api.assembler;

import org.springframework.hateoas.EntityModel;

import java.time.LocalDate;
import java.util.List;

public record AuthorRespresentation (
        Long authorID,
        String name,
        String nationality,
        int bookCount,
        List<EntityModel<BookRepresentation>> books,
        LocalDate birthDate,
        LocalDate deathDate,
        boolean isAlive,
        String city,
        String biography
){}
