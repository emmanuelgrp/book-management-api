package com.codemainlabs.book_management_api.assembler;

import com.codemainlabs.book_management_api.controller.BookController;
import com.codemainlabs.book_management_api.model.dto.AuthorIDDTO;
import com.codemainlabs.book_management_api.model.dto.BookResponseDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BookAssembler implements RepresentationModelAssembler<BookResponseDTO, EntityModel<BookRepresentation>> {

    private final AuthorIDDTOAssembler authorIDDTOAssembler;

    public BookAssembler(AuthorIDDTOAssembler authorIDDTOAssembler) {
        this.authorIDDTOAssembler = authorIDDTOAssembler;
    }

    @Override
    @NonNull
    public EntityModel<BookRepresentation> toModel(@NonNull BookResponseDTO bookResponseDTO) {
        List<EntityModel<AuthorIDDTO>> authorModels = bookResponseDTO.authors()
                .stream()
                .map(authorIDDTOAssembler::toModel)
                .collect(Collectors.toList());

        BookRepresentation bookRepresentation = new BookRepresentation(
                bookResponseDTO.bookID(),
                bookResponseDTO.title(),
                bookResponseDTO.isbn(),
                bookResponseDTO.synopsis(),
                bookResponseDTO.publicationDate(),
                bookResponseDTO.genre(),
                bookResponseDTO.pageCount(),
                authorModels
        );

        return EntityModel.of(
                bookRepresentation,
                linkTo(methodOn(BookController.class).getBookById(bookResponseDTO.bookID())).withSelfRel().withType("GET"),
                linkTo(methodOn(BookController.class).updateBook(bookResponseDTO.bookID(), null)).withRel("update").withType("PUT"),
                linkTo(methodOn(BookController.class).deleteBook(bookResponseDTO.bookID())).withRel("delete").withType("DELETE")
        );
    }


    @NonNull
    public BookListResponse toBookListResponse(@NonNull Iterable<? extends BookResponseDTO> books) {
        List<EntityModel<BookRepresentation>> bookEntityModels = StreamSupport.stream(books.spliterator(), false)
                .map(this::toModel)
                .collect(Collectors.toList());

        Link selfLink = linkTo(methodOn(BookController.class).getAllBooks()).withSelfRel().withType("GET");
        Link createLink = linkTo(methodOn(BookController.class).createBook(null)).withRel("create").withType("POST");

        return new BookListResponse(bookEntityModels, selfLink, createLink);
    }

}