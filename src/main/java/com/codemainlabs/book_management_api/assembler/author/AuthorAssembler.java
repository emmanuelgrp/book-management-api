package com.codemainlabs.book_management_api.assembler.author;

import com.codemainlabs.book_management_api.assembler.book.BookAssembler;
import com.codemainlabs.book_management_api.assembler.book.BookRepresentation;
import com.codemainlabs.book_management_api.controller.AuthorController;
import com.codemainlabs.book_management_api.model.dto.author.AuthorResponseDTO;
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
public class AuthorAssembler implements RepresentationModelAssembler<AuthorResponseDTO, EntityModel<AuthorRespresentation>> {

    private final BookAssembler bookAssembler;

    @Override
    @NonNull
    public EntityModel<AuthorRespresentation> toModel(@NonNull AuthorResponseDTO authorResponseDTO) {
        List<EntityModel<BookRepresentation>> bookModels = authorResponseDTO.books()
                .stream()
                .map(bookAssembler::toModel)
                .collect(Collectors.toList());

        AuthorRespresentation authorRepresentation = new AuthorRespresentation(
                authorResponseDTO.authorID(),
                authorResponseDTO.name(),
                authorResponseDTO.nationality(),
                authorResponseDTO.bookCount(),
                bookModels,
                authorResponseDTO.birthDate(),
                authorResponseDTO.deathDate(),
                authorResponseDTO.isAlive(),
                authorResponseDTO.city(),
                authorResponseDTO.biography()
        );

        return EntityModel.of(
                authorRepresentation,
                linkTo(methodOn(AuthorController.class).getAuthorById(authorResponseDTO.authorID())).withSelfRel().withType("GET"),
                linkTo(methodOn(AuthorController.class).updateAuthor(authorResponseDTO.authorID(), null)).withRel("update").withType("PUT"),
                linkTo(methodOn(AuthorController.class).deleteAuthor(authorResponseDTO.authorID())).withRel("delete").withType("DELETE")
        );
    }

    @NonNull
    public AuthorListResponse toAuthorListResponse(@NonNull Iterable<? extends AuthorResponseDTO> authors) {
        var authorEntityModels = StreamSupport.stream(authors.spliterator(), false)
                .map(this::toModel)
                .toList();

        return new AuthorListResponse(
                authorEntityModels,
                linkTo(methodOn(AuthorController.class).getAllAuthors(null)).withSelfRel().withType("GET"),
                linkTo(methodOn(AuthorController.class).createAuthor(null)).withRel("create").withType("POST")
        );
    }
}