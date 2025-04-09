package com.codemainlabs.book_management_api.assembler;

import com.codemainlabs.book_management_api.controller.AuthorController; // Asegúrate de tener este controlador
import com.codemainlabs.book_management_api.model.dto.AuthorIDDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.springframework.lang.NonNull;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class AuthorIdAssembler implements RepresentationModelAssembler<AuthorIDDTO, EntityModel<AuthorIDDTO>> {

    @Override
    @NonNull
    public EntityModel<AuthorIDDTO> toModel(@NonNull AuthorIDDTO author) {
        // 1. Crea el enlace 'self'
        Link selfLink = linkTo(methodOn(AuthorController.class).getAuthorById(author.authorID())).withSelfRel().withType("GET");

        // 2. Crea el EntityModel CON el AuthorIDDTO y ESE ÚNICO enlace 'self'
        return EntityModel.of(
                author, // El contenido (authorID, name)
                selfLink  // El único enlace
        );
    }
}