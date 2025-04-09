package com.codemainlabs.book_management_api.controller;

import com.codemainlabs.book_management_api.assembler.BookAssembler;
import com.codemainlabs.book_management_api.assembler.BookListModel;
import com.codemainlabs.book_management_api.assembler.BookListResponse;
import com.codemainlabs.book_management_api.assembler.BookRepresentation;
import com.codemainlabs.book_management_api.model.dto.BookRequestDTO;
import com.codemainlabs.book_management_api.model.dto.BookResponseDTO;
import com.codemainlabs.book_management_api.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
@AllArgsConstructor
public class BookController {

    private final BookService bookService;
    private final BookAssembler bookAssembler;

    @GetMapping
    public ResponseEntity<BookListResponse> getAllBooks() { // <-- Tipo de retorno cambiado
        List<BookResponseDTO> bookDTOs = bookService.getAllBooks(); // Obtiene lista de DTOs
        // Convierte la lista de DTOs a CollectionModel usando el assembler
        BookListResponse collectionModel = bookAssembler.toBookListResponse(bookDTOs);
        return ResponseEntity.ok(collectionModel);
    }


    @GetMapping("/{bookID}")
    public ResponseEntity<?> getBookById(@PathVariable Long bookID) {
        return bookService.getBookById(bookID)
                .map(bookAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createBook(@RequestBody List<BookRequestDTO> bookRequestDTOs) {
        if (bookRequestDTOs.size() == 1) {
            var created = bookService.createBook(bookRequestDTOs.get(0));
            var model = bookAssembler.toModel(created);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(model);
        }

        var createdList = bookService.createBooks(bookRequestDTOs);
        var modelList = bookAssembler.toBookListResponse(createdList);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(modelList);
    }

    @PutMapping("/{bookID}")
    public ResponseEntity<?> updateBook(
            @PathVariable Long bookID,
            @RequestBody BookRequestDTO bookRequestDTO) {

        Optional<BookResponseDTO> updatedBook = bookService.updateBook(bookID, bookRequestDTO);

        return updatedBook
                .map(bookAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{bookID}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long bookID) {
        bookService.deleteBook(bookID);
        return ResponseEntity.noContent().build();
    }
}
