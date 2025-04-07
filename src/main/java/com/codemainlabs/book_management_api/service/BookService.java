package com.codemainlabs.book_management_api.service;

import com.codemainlabs.book_management_api.model.dto.AuthorRequestDTO;
import com.codemainlabs.book_management_api.model.dto.AuthorResponseDTO;
import com.codemainlabs.book_management_api.model.dto.BookRequestDTO;
import com.codemainlabs.book_management_api.model.dto.BookResponseDTO;
import com.codemainlabs.book_management_api.model.entity.Book;
import org.springframework.hateoas.EntityModel;

import java.util.List;
import java.util.Optional;

public interface BookService {
    List<BookResponseDTO> getAllBooks();
    Optional<BookResponseDTO> getBookById(Long id);
    BookResponseDTO createBook(BookRequestDTO bookRequestDTO);
    Optional<BookResponseDTO> updateBook(Long id, BookRequestDTO bookRequestDTO);
    void deleteBook(Long id);
    List<BookResponseDTO> createBooks(List<BookRequestDTO> bookRequestDTOs);
    BookResponseDTO convertToBookDto(Book book);

    Optional<EntityModel<BookResponseDTO>> getBookByIdWithLinks(Long id);
    List<Optional<EntityModel<BookResponseDTO>>> getAllBooksWithLinks();
    Optional<EntityModel<BookResponseDTO>> createBookWithLinks(BookRequestDTO bookRequestDTO);
    List<Optional<EntityModel<BookResponseDTO>>> createBooksWithLinks(List<BookRequestDTO> bookRequestDTOs);
    Optional<EntityModel<BookResponseDTO>> updateBookWithLinks(Long bookID, BookRequestDTO bookRequestDTO);
}

