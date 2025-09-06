package com.codemainlabs.book_management_api.controller;

import com.codemainlabs.book_management_api.assembler.BookAssembler;
import com.codemainlabs.book_management_api.model.dto.BookRequestDTO;
import com.codemainlabs.book_management_api.model.dto.BookResponseDTO;
import com.codemainlabs.book_management_api.model.dto.PageMetadata;
import com.codemainlabs.book_management_api.model.dto.PaginatedBooksResponse;
import com.codemainlabs.book_management_api.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

import com.codemainlabs.book_management_api.assembler.BookRepresentation;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;

@RestController
@RequestMapping("/api/books")
@AllArgsConstructor
public class BookController {

    private final BookService bookService;
    private final BookAssembler bookAssembler;
    @Qualifier("bookPagedResourcesAssembler")
    private final PagedResourcesAssembler<BookResponseDTO> pagedResourcesAssembler;

    @GetMapping
    public ResponseEntity<PaginatedBooksResponse> getAllBooks(Pageable pageable) {
        var booksPage = bookService.getAllBooks(pageable);

        // Genera temporalmente el PagedModel para obtener los links y el contenido
        PagedModel<EntityModel<BookRepresentation>> pagedModel = pagedResourcesAssembler.toModel(booksPage, bookAssembler);

        List<EntityModel<BookRepresentation>> bookEntities = pagedModel.getContent().stream().collect(Collectors.toList());
        List<Link> links = pagedModel.getLinks().toList();
        PagedModel.PageMetadata pageMetadata = pagedModel.getMetadata();

        PageMetadata customPageMetadata = PageMetadata.builder()
                .size(pageMetadata.getSize())
                .totalElements(pageMetadata.getTotalElements())
                .totalPages(pageMetadata.getTotalPages())
                .number(pageMetadata.getNumber())
                .build();

        PaginatedBooksResponse response = new PaginatedBooksResponse(
                bookEntities,
                customPageMetadata,
                links
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{bookID}")
    public ResponseEntity<?> getBookById(@PathVariable Long bookID) {
        return bookService.getBookById(bookID)
                .map(book -> bookAssembler.toBookListResponse(List.of(book)))
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
    public ResponseEntity<?> updateBook(@PathVariable Long bookID, @RequestBody BookRequestDTO bookRequestDTO) {
        return  bookService.updateBook(bookID, bookRequestDTO)
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
