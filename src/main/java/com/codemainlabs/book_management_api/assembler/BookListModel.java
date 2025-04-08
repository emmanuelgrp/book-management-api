package com.codemainlabs.book_management_api.assembler;

import com.codemainlabs.book_management_api.model.dto.BookResponseDTO;
import com.fasterxml.jackson.annotation.JsonProperty; // Importante para controlar el nombre de la clave JSON
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;
import java.util.Objects;

// Hereda de RepresentationModel para poder añadirle enlaces (_links)
public class BookListModel extends RepresentationModel<BookListModel> {

    // Esta anotación define el nombre de la clave JSON para tu lista.
    // Cámbiala a lo que necesites (e.g., "libros", "items", "data").
    @JsonProperty("books")
    private final List<EntityModel<BookResponseDTO>> content;

    public BookListModel(List<EntityModel<BookResponseDTO>> content) {
        this.content = Objects.requireNonNull(content, "Content cannot be null");
        // Puedes añadir enlaces comunes aquí si lo deseas, aunque es mejor hacerlo en el Assembler/Controller
    }

    // Getter necesario para la serialización JSON
    public List<EntityModel<BookResponseDTO>> getContent() {
        return content;
    }

    // Sobrescribir equals y hashCode es una buena práctica
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false; // Compara los enlaces de RepresentationModel
        BookListModel that = (BookListModel) o;
        return Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), content); // Incluye el hash de los enlaces
    }
}