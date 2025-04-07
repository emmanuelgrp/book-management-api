package com.codemainlabs.book_management_api.controller;

import com.codemainlabs.book_management_api.model.dto.BookRequestDTO;
import com.codemainlabs.book_management_api.model.dto.BookResponseDTO;
import com.codemainlabs.book_management_api.service.BookService;
import lombok.AllArgsConstructor;
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

    // Endpoint to get all books
    @GetMapping
    public ResponseEntity<List<Optional<EntityModel<BookResponseDTO>>>> getAllBooksWithLinks() {
        return new ResponseEntity<>(bookService.getAllBooksWithLinks(), HttpStatus.OK);
    }

    // Endpoint to get a single book by bookID with HATEOAS links
    @GetMapping("/{bookID}")
    public ResponseEntity<EntityModel<BookResponseDTO>> getBookById(@PathVariable Long bookID) {
        return bookService.getBookByIdWithLinks(bookID)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint to create a new book
    @PostMapping
    public ResponseEntity<?> createBook(@RequestBody List<BookRequestDTO> bookRequestDTOs) {
        Object bookResponse = (bookRequestDTOs.size() == 1)
                ? bookService.createBookWithLinks(bookRequestDTOs.get(0))
                : bookService.createBooksWithLinks(bookRequestDTOs);

        return new ResponseEntity<>(bookResponse, HttpStatus.CREATED);
    }

    // Endpoint to update an existing book
    @PutMapping("/{bookID}")
    public ResponseEntity<EntityModel<BookResponseDTO>> updateBook(@PathVariable Long bookID, @RequestBody BookRequestDTO bookRequestDTO) {
        Optional<EntityModel<BookResponseDTO>> updatedBookWithLinks = bookService.updateBookWithLinks(bookID, bookRequestDTO);
        return updatedBookWithLinks
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint to delete a book
    @DeleteMapping("/{bookID}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long bookID) {
        bookService.deleteBook(bookID);
        return ResponseEntity.noContent().build();
    }
}


