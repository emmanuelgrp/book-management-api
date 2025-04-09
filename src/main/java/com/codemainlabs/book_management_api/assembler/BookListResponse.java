package com.codemainlabs.book_management_api.assembler;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
// Quita la importación de Links si ya no la usas directamente aquí
import org.springframework.hateoas.RepresentationModel; // Importa RepresentationModel

import java.util.List;

// Haz que extienda RepresentationModel
@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY) // Opcional: No serializa si 'books' está vacío
@JsonPropertyOrder({ "books", "_links" })
public class BookListResponse extends RepresentationModel<BookListResponse> {

    // Getter para 'books' (necesario para Jackson)
    // Mantén la lista de libros. Usa @JsonProperty para asegurar el nombre.
    @JsonProperty("books")
    private final List<EntityModel<BookRepresentation>> books;

    // Constructor: Recibe los libros y los enlaces (como Iterable o varargs)
    // Llama a add() de RepresentationModel para registrar los enlaces.
    public BookListResponse(List<EntityModel<BookRepresentation>> books, Iterable<Link> links) {
        this.books = books;
        this.add(links); // Añade los enlaces usando el método heredado
    }

    // Constructor alternativo con varargs para los enlaces
    public BookListResponse(List<EntityModel<BookRepresentation>> books, Link... links) {
        this.books = books;
        this.add(links); // Añade los enlaces usando el método heredado
    }


    // NO necesitas un campo 'Links links' ni su getter.
    // RepresentationModel se encarga de serializar los enlaces añadidos con add()
    // en el formato correcto (_links: { ... }) cuando se usa HAL.
}