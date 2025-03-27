package com.codemainlabs.book_management_api.controller;

import com.codemainlabs.book_management_api.model.dto.BookRequestDTO;
import com.codemainlabs.book_management_api.model.dto.BookResponseDTO;
import com.codemainlabs.book_management_api.service.BookService;
import lombok.AllArgsConstructor;
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
    public ResponseEntity<List<BookResponseDTO>> getAllBooks() {
        return new ResponseEntity<>(bookService.getAllBooks(), HttpStatus.OK);
    }

    // Endpoint to get a single book by bookID
    @GetMapping("/{bookID}")
    public ResponseEntity<BookResponseDTO> getBookById(@PathVariable Long bookID) {
        Optional<BookResponseDTO> book = bookService.getBookById(bookID);
        return book.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint to create a new book
    @PostMapping
    public ResponseEntity<?> createBook(@RequestBody List<BookRequestDTO> bookRequestDTOs) {
        Object bookResponse = (bookRequestDTOs.size() == 1)
                ? bookService.createBook(bookRequestDTOs.get(0))
                : bookService.createBooks(bookRequestDTOs);

        return new ResponseEntity<>(bookResponse, HttpStatus.CREATED);
    }

    // Endpoint to update an existing book
    @PutMapping("/{bookID}")
    public ResponseEntity<BookResponseDTO> updateBook(@PathVariable Long bookID, @RequestBody BookRequestDTO bookRequestDTO) {
        Optional<BookResponseDTO> updatedBook = bookService.updateBook(bookID, bookRequestDTO);
        return updatedBook.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint to delete a book
    @DeleteMapping("/{bookID}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long bookID) {
        bookService.deleteBook(bookID);
        return ResponseEntity.noContent().build();
    }
}


