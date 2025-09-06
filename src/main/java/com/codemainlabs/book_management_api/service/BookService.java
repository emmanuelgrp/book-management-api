package com.codemainlabs.book_management_api.service;

import com.codemainlabs.book_management_api.model.dto.BookRequestDTO;
import com.codemainlabs.book_management_api.model.dto.BookResponseDTO;

import java.util.List;
import java.util.Optional;

public interface BookService {
    List<BookResponseDTO> getAllBooks();
    Optional<BookResponseDTO> getBookById(Long id);
    BookResponseDTO createBook(BookRequestDTO bookRequestDTO);
    Optional<BookResponseDTO> updateBook(Long id, BookRequestDTO bookRequestDTO);
    void deleteBook(Long id);
    List<BookResponseDTO> createBooks(List<BookRequestDTO> bookRequestDTOs);
}

