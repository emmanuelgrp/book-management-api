package com.codemainlabs.book_management_api.assembler;


import com.codemainlabs.book_management_api.controller.BookController;
import com.codemainlabs.book_management_api.model.dto.BookResponseDTO;
import org.springframework.hateoas.CollectionModel; // Aún lo necesitarás si la interfaz lo exige
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class BookAssembler implements RepresentationModelAssembler<BookResponseDTO, EntityModel<BookResponseDTO>> {

    @Override
    @NonNull
    public EntityModel<BookResponseDTO> toModel(@NonNull BookResponseDTO bookResponseDTO) {
        // Este método se mantiene igual
        return EntityModel.of(
                bookResponseDTO,
                linkTo(methodOn(BookController.class).getBookById(bookResponseDTO.bookID())).withSelfRel(),
                linkTo(methodOn(BookController.class).updateBook(bookResponseDTO.bookID(), null)).withRel("update").withType("PUT"),
                linkTo(methodOn(BookController.class).deleteBook(bookResponseDTO.bookID())).withRel("delete").withType("DELETE")
        );
    }

    /**
     * Este método cumple con la interfaz RepresentationModelAssembler,
     * pero NO será el que uses en tu controlador para obtener la estructura deseada.
     * Generará la respuesta estándar con _embedded.
     */
    @Override
    @NonNull
    public CollectionModel<EntityModel<BookResponseDTO>> toCollectionModel(@NonNull Iterable<? extends BookResponseDTO> books) {
        List<EntityModel<BookResponseDTO>> bookModels = StreamSupport.stream(books.spliterator(), false)
                .map(this::toModel)
                .collect(Collectors.toList());

        // Retornamos CollectionModel estándar
        return CollectionModel.of(
                bookModels,
                linkTo(methodOn(BookController.class).getAllBooks()).withSelfRel()
        );
    }

    /**
     * NUEVO MÉTODO: Crea y devuelve el modelo de lista personalizado sin "_embedded".
     * Usa este método en tu controlador.
     * @param books Iterable de DTOs de libros.
     * @return Un BookListModel con la lista bajo la clave 'books' y enlaces de colección.
     */
    @NonNull
    public BookListModel toBookListModel(@NonNull Iterable<? extends BookResponseDTO> books) {
        // 1. Convierte cada DTO a EntityModel (reutiliza toModel)
        List<EntityModel<BookResponseDTO>> bookEntityModels = StreamSupport.stream(books.spliterator(), false)
                .map(this::toModel) // Llama al método toModel existente para cada libro
                .collect(Collectors.toList());

        // 2. Crea una instancia de tu modelo personalizado
        BookListModel bookListModel = new BookListModel(bookEntityModels);

        // 3. Añade los enlaces a nivel de colección a tu modelo personalizado
        bookListModel.add(linkTo(methodOn(BookController.class).getAllBooks()).withSelfRel());
        // Puedes añadir más enlaces aquí si son necesarios para la colección
        // bookListModel.add(linkTo(methodOn(BookController.class).createBook(null)).withRel("create").withType("POST"));

        // 4. Devuelve el modelo personalizado
        return bookListModel;
    }
}