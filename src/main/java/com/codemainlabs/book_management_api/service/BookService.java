package com.codemainlabs.book_management_api.service;

import com.codemainlabs.book_management_api.model.dto.BookRequestDTO;
import com.codemainlabs.book_management_api.model.dto.BookResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface BookService {
    Page<BookResponseDTO> getAllBooks(Pageable pageable);
    Optional<BookResponseDTO> getBookById(Long id);
    BookResponseDTO createBook(BookRequestDTO bookRequestDTO);
    Optional<BookResponseDTO> updateBook(Long id, BookRequestDTO bookRequestDTO);
    void deleteBook(Long id);
    List<BookResponseDTO> createBooks(List<BookRequestDTO> bookRequestDTOs);
}

