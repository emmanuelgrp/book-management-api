package com.codemainlabs.book_management_api.assembler.author;

import com.codemainlabs.book_management_api.controller.AuthorController;
import com.codemainlabs.book_management_api.model.dto.author.AuthorIDDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.springframework.lang.NonNull;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class AuthorIDDTOAssembler implements RepresentationModelAssembler<AuthorIDDTO, EntityModel<AuthorIDDTO>> {

    @Override
    @NonNull
    public EntityModel<AuthorIDDTO> toModel(@NonNull AuthorIDDTO author) {
        return EntityModel.of(
                author,
                linkTo(methodOn(AuthorController.class).getAuthorById(author.authorID())).withSelfRel().withType("GET")
        );
    }
}