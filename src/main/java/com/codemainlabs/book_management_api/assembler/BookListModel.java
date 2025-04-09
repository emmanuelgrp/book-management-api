package com.codemainlabs.book_management_api.assembler; // O donde tengas tu BookListModel

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import java.util.Arrays; // Necesario si usas el constructor Iterable<Link>

public class BookListModel extends CollectionModel<EntityModel<BookRepresentation>> {

    // *** USA ESTE CONSTRUCTOR *** (Acepta varargs para los links)
    // Este es el constructor estándar y el que usa el BookAssembler al crear la instancia.
    public BookListModel(Iterable<EntityModel<BookRepresentation>> content, Link... links) {
        super(); // Llama al constructor super con varargs
    }
}