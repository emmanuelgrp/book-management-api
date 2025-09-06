package com.codemainlabs.book_management_api.controller;

import com.codemainlabs.book_management_api.assembler.AuthorAssembler;
import com.codemainlabs.book_management_api.assembler.AuthorListResponse;
import com.codemainlabs.book_management_api.model.dto.AuthorRequestDTO;
import com.codemainlabs.book_management_api.service.AuthorService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
@AllArgsConstructor
public class AuthorController {

    private final AuthorService authorService;
    private final AuthorAssembler authorAssembler;

    @GetMapping
    public ResponseEntity<AuthorListResponse> getAllAuthors() {
        return ResponseEntity.ok(
                authorAssembler.toAuthorListResponse(
                        authorService.getAllAuthors()
                )
        );
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