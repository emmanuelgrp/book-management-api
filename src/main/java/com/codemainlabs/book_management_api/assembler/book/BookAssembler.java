package com.codemainlabs.book_management_api.assembler.book;

import com.codemainlabs.book_management_api.assembler.author.AuthorIDDTOAssembler;
import com.codemainlabs.book_management_api.controller.BookController;
import com.codemainlabs.book_management_api.model.dto.author.AuthorIDDTO;
import com.codemainlabs.book_management_api.model.dto.book.BookResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
@AllArgsConstructor
public class BookAssembler implements RepresentationModelAssembler<BookResponseDTO, EntityModel<BookRepresentation>> {

    private final AuthorIDDTOAssembler authorIDDTOAssembler;

    @Override
    @NonNull
    public EntityModel<BookRepresentation> toModel(@NonNull BookResponseDTO bookResponseDTO) {
        List<EntityModel<AuthorIDDTO>> authorModels = bookResponseDTO.authors()
                .stream()
                .map(authorIDDTOAssembler::toModel)
                .collect(Collectors.toList());

        // Todo Mapper for BookResponseDTO and BookRepresentation
        BookRepresentation bookRepresentation = BookRepresentation.builder()
                .bookID(bookResponseDTO.bookID())
                .title(bookResponseDTO.title())
                .isbn(bookResponseDTO.isbn())
                .synopsis(bookResponseDTO.synopsis())
                .publicationDate(bookResponseDTO.publicationDate())
                .genre(bookResponseDTO.genre())
                .pageCount(bookResponseDTO.pageCount())
                .authors(authorModels)
                .build();


        return EntityModel.of(
                bookRepresentation,
                linkTo(methodOn(BookController.class).getBookById(bookResponseDTO.bookID())).withSelfRel().withType("GET"),
                linkTo(methodOn(BookController.class).updateBook(bookResponseDTO.bookID(), null)).withRel("update").withType("PUT"),
                linkTo(methodOn(BookController.class).deleteBook(bookResponseDTO.bookID())).withRel("delete").withType("DELETE")
        );
    }


    @NonNull
    public BookListResponse toBookListResponse(@NonNull Iterable<? extends BookResponseDTO> books) {
        var bookEntityModels = StreamSupport.stream(books.spliterator(), false)
                .map(this::toModel)
                .toList();

        return new BookListResponse(
                bookEntityModels,
                linkTo(methodOn(BookController.class).getAllBooks(null)).withSelfRel().withType("GET"),
                linkTo(methodOn(BookController.class).createBook(null)).withRel("create").withType("POST")
        );
    }


}