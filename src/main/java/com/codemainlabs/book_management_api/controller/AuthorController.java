package com.codemainlabs.book_management_api.controller;

import com.codemainlabs.book_management_api.model.dto.AuthorRequestDTO;
import com.codemainlabs.book_management_api.model.dto.AuthorResponseDTO;
import com.codemainlabs.book_management_api.service.AuthorService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/authors")
@AllArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    // Endpoint to get all authors
    @GetMapping
    public ResponseEntity<List<AuthorResponseDTO>> getAllAuthors() {
        return new ResponseEntity<>(authorService.getAllAuthors(), HttpStatus.OK);
    }

    // Endpoint to get a single author by authorID
    @GetMapping("/{authorID}")
    public ResponseEntity<AuthorResponseDTO> getAuthorById(@PathVariable Long authorID) {
        Optional<AuthorResponseDTO> author = authorService.getAuthorById(authorID);
        return author.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint to create a single author
    @PostMapping
    public ResponseEntity<AuthorResponseDTO> createAuthor(@RequestBody AuthorRequestDTO authorRequestDTO) {
        AuthorResponseDTO createdAuthor = authorService.createAuthor(authorRequestDTO);
        return new ResponseEntity<>(createdAuthor, HttpStatus.CREATED);
    }

    // Endpoint to create multiple authors at once
    @PostMapping("/batch")
    public ResponseEntity<List<AuthorResponseDTO>> createAuthors(@RequestBody List<AuthorRequestDTO> authorRequestDTOs) {
        List<AuthorResponseDTO> createdAuthors = authorService.createAuthors(authorRequestDTOs);
        return new ResponseEntity<>(createdAuthors, HttpStatus.CREATED);
    }

    // Endpoint to update an existing author
    @PutMapping("/{authorID}")
    public ResponseEntity<AuthorResponseDTO> updateAuthor(@PathVariable Long authorID, @RequestBody AuthorRequestDTO authorRequestDTO) {
        Optional<AuthorResponseDTO> updatedAuthor = authorService.updateAuthor(authorID, authorRequestDTO);
        return updatedAuthor.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint to delete an author
    @DeleteMapping("/{authorID}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long authorID) {
        authorService.deleteAuthor(authorID);
        return ResponseEntity.noContent().build();
    }
}
