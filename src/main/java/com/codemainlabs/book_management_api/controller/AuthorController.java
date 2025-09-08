package com.codemainlabs.book_management_api.controller;

import com.codemainlabs.book_management_api.assembler.author.AuthorAssembler;
import com.codemainlabs.book_management_api.assembler.author.AuthorRespresentation;
import com.codemainlabs.book_management_api.model.dto.author.AuthorRequestDTO;
import com.codemainlabs.book_management_api.model.dto.author.AuthorResponseDTO;
import com.codemainlabs.book_management_api.model.dto.pagination.PageMetadata;
import com.codemainlabs.book_management_api.model.dto.pagination.PaginatedAuthorsResponse;
import com.codemainlabs.book_management_api.service.AuthorService;
import lombok.AllArgsConstructor;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Qualifier;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.PagedModel;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/authors")
@AllArgsConstructor
public class AuthorController {

    private final AuthorService authorService;
    private final AuthorAssembler authorAssembler;

    @Qualifier("authorPagedResourcesAssembler")
    private final PagedResourcesAssembler<AuthorResponseDTO> pagedResourcesAssembler;

    @GetMapping
    public ResponseEntity<PaginatedAuthorsResponse> getAllAuthors(Pageable pageable) {
        var authorsPage = authorService.getAllAuthors(pageable);

        // Genera temporalmente el PagedModel para obtener los links y el contenido
        PagedModel<EntityModel<AuthorRespresentation>> pagedModel = pagedResourcesAssembler.toModel(authorsPage, authorAssembler);

        List<EntityModel<AuthorRespresentation>> authorEntities = pagedModel.getContent().stream().collect(Collectors.toList());
        List<Link> links = pagedModel.getLinks().toList();
        PagedModel.PageMetadata pageMetadata = pagedModel.getMetadata();

        PageMetadata customPageMetadata = PageMetadata.builder()
                .size(pageMetadata.getSize())
                .totalElements(pageMetadata.getTotalElements())
                .totalPages(pageMetadata.getTotalPages())
                .number(pageMetadata.getNumber())
                .build();

        PaginatedAuthorsResponse response = new PaginatedAuthorsResponse(
                authorEntities,
                customPageMetadata,
                links
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{authorID}")
    public ResponseEntity<?> getAuthorById(@PathVariable Long authorID) {
        return authorService.getAuthorById(authorID)
                .map(authorAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createAuthor (@RequestBody List<AuthorRequestDTO> authorRequestDTOs) {
        if (authorRequestDTOs.size() == 1) {
            var createdAuthor = authorService.createAuthor(authorRequestDTOs.get(0));
            var model = authorAssembler.toModel(createdAuthor);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(model);
        }

        var createdAuthorsList = authorService.createAuthors(authorRequestDTOs);
        var modelList = authorAssembler.toAuthorListResponse(createdAuthorsList);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(modelList);
    }

    @PutMapping("/{authorID}")
    public ResponseEntity<?> updateAuthor(@PathVariable Long authorID, @RequestBody AuthorRequestDTO authorRequestDTO) {
        return authorService.updateAuthor(authorID, authorRequestDTO)
                .map(authorAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{authorID}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long authorID) {
        authorService.deleteAuthor(authorID);
        return ResponseEntity.noContent().build();
    }
}