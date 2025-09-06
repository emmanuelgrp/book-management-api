package com.codemainlabs.book_management_api.service.impl;

import com.codemainlabs.book_management_api.exception.ResourceNotFoundException;
import com.codemainlabs.book_management_api.mapper.BookMapper;
import com.codemainlabs.book_management_api.model.dto.BookRequestDTO;
import com.codemainlabs.book_management_api.model.dto.BookResponseDTO;
import com.codemainlabs.book_management_api.model.entity.Book;
import com.codemainlabs.book_management_api.repository.BookRepository;
import com.codemainlabs.book_management_api.service.BookService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;


    @Override
    public Page<BookResponseDTO> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(bookMapper::toBookResponseDTO);
    }

    @Override
    public Optional<BookResponseDTO> getBookById(Long bookID) {
        return bookRepository.findById(bookID)
                .map(bookMapper::toBookResponseDTO);
    }

    @Override
    @Transactional
    public BookResponseDTO createBook(BookRequestDTO bookRequestDTO) {
        return bookMapper.toBookResponseDTO(
                bookRepository.save(
                        bookMapper.toBook(bookRequestDTO)
                )
        );
    }

    @Override
    @Transactional
    public Optional<BookResponseDTO> updateBook(Long bookID, BookRequestDTO bookRequestDTO) {
        Book book = bookRepository.findById(bookID)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with bookID: " + bookID));

        bookMapper.updateBookFromDto(bookRequestDTO, book);

        Book updatedBook = bookRepository.save(book);

        return Optional.of(bookMapper.toBookResponseDTO(updatedBook));
    }

    @Override
    @Transactional
    public void deleteBook(Long bookID) {
        if (!bookRepository.existsById(bookID)) {
            throw new ResourceNotFoundException("Book not found with bookID: " + bookID);
        }
        bookRepository.deleteById(bookID);
    }

    @Override
    @Transactional
    public List<BookResponseDTO> createBooks(List<BookRequestDTO> bookRequestDTOs) {
        var books = bookRequestDTOs.stream()
                .map(bookMapper::toBook)
                .collect(Collectors.toList());

        List<Book> savedBooks = bookRepository.saveAll(books);

        return savedBooks.stream()
                .map(bookMapper::toBookResponseDTO)
                .collect(Collectors.toList());
    }

}
