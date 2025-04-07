package com.codemainlabs.book_management_api.service.impl;

import com.codemainlabs.book_management_api.controller.BookController;
import com.codemainlabs.book_management_api.exception.ResourceNotFoundException;
import com.codemainlabs.book_management_api.model.dto.AuthorIDDTO;
import com.codemainlabs.book_management_api.model.dto.BookRequestDTO;
import com.codemainlabs.book_management_api.model.dto.BookResponseDTO;
import com.codemainlabs.book_management_api.model.entity.Author;
import com.codemainlabs.book_management_api.model.entity.Book;
import com.codemainlabs.book_management_api.repository.AuthorRepository;
import com.codemainlabs.book_management_api.repository.BookRepository;
import com.codemainlabs.book_management_api.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;


import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;


    @Override
    public List<BookResponseDTO> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(this::convertToBookDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<BookResponseDTO> getBookById(Long bookID) {
        return bookRepository.findById(bookID)
                .map(this::convertToBookDto);
    }

    @Override
    public BookResponseDTO createBook(BookRequestDTO bookRequestDTO) {
        Book book = convertToEntity(bookRequestDTO);
        Book savedBook = bookRepository.save(book);
        return convertToBookDto(savedBook);
    }

    @Override
    public Optional<BookResponseDTO> updateBook(Long bookID, BookRequestDTO bookRequestDTO) {
        Book book = bookRepository.findById(bookID)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + bookID));

        book.setTitle(bookRequestDTO.title() != null ? bookRequestDTO.title() : book.getTitle());
        book.setIsbn(bookRequestDTO.isbn() != null ? bookRequestDTO.isbn() : book.getIsbn());
        book.setSynopsis(bookRequestDTO.synopsis() != null ? bookRequestDTO.synopsis() : book.getSynopsis());
        book.setPublicationDate(bookRequestDTO.publicationDate() != null ? bookRequestDTO.publicationDate() : book.getPublicationDate());
        book.setGenre(bookRequestDTO.genre() != null ? bookRequestDTO.genre() : book.getGenre());
        book.setPageCount(bookRequestDTO.pageCount() != null ? bookRequestDTO.pageCount() : book.getPageCount());
        book.setEditorial(bookRequestDTO.editorial() != null ? bookRequestDTO.editorial() : book.getEditorial());

        //System.out.println(bookRequestDTO.authorIds());
        if (bookRequestDTO.authorIds() != null && !bookRequestDTO.authorIds().isEmpty()) {
            List<Author> authors = bookRequestDTO.authorIds().stream()
                    .map(id -> authorRepository.findById(id)
                            .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + id)))
                    .collect(Collectors.toList());

            book.setAuthors(authors);
        }

        return Optional.of(convertToBookDto(bookRepository.save(book)));
    }

    @Override
    public void deleteBook(Long bookID) {
        if (!bookRepository.existsById(bookID)) {
            throw new ResourceNotFoundException("Book not found with id: " + bookID);
        }
        bookRepository.deleteById(bookID);
    }

    @Override
    public List<BookResponseDTO> createBooks(List<BookRequestDTO> bookRequestDTOs) {
        var books = bookRequestDTOs.stream()
                .map(this::convertToEntity)
                .collect(Collectors.toList());

        return bookRepository.saveAll(books).stream()
                .map(this::convertToBookDto)
                .collect(Collectors.toList());
    }

    public BookResponseDTO convertToBookDto(Book book) {
        return BookResponseDTO.builder()
                .bookID(book.getId())
                .title(book.getTitle())
                .authors(
                        Optional.ofNullable(book.getAuthors())
                                .map(authors -> authors.stream()
                                        .map(author -> new AuthorIDDTO(author.getId(), author.getName()))
                                        .collect(Collectors.toList()))
                                .orElse(List.of())
                )
                .isbn(book.getIsbn())
                .synopsis(book.getSynopsis())
                .publicationDate(book.getPublicationDate())
                .genre(book.getGenre())
                .pageCount(book.getPageCount())
                .build();
    }

    private Book convertToEntity(BookRequestDTO bookRequestDTO) {
        List<Author> authors = (bookRequestDTO.authorIds() != null && !bookRequestDTO.authorIds().isEmpty())
                ? bookRequestDTO.authorIds().stream()
                .map(id -> authorRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + id)))
                .toList()
                : Collections.emptyList();

        return Book.builder()
                .title(bookRequestDTO.title())
                .isbn(bookRequestDTO.isbn())
                .synopsis(bookRequestDTO.synopsis())
                .publicationDate(bookRequestDTO.publicationDate())
                .genre(bookRequestDTO.genre())
                .pageCount(bookRequestDTO.pageCount())
                .editorial(bookRequestDTO.editorial())
                .authors(authors)
                .build();
    }

    @Override
    public Optional<EntityModel<BookResponseDTO>> getBookByIdWithLinks(Long id) {
        return getBookById(id)
                .map(book -> convertToEntityModel(book, id));
    }

    @Override
    public List<Optional<EntityModel<BookResponseDTO>>> getAllBooksWithLinks() {
        return getAllBooks().stream()
                .map(book -> Optional.ofNullable(convertToEntityModel(book, book.bookID())))
                .collect(Collectors.toList());
    }

    public EntityModel<BookResponseDTO> convertToEntityModel(BookResponseDTO bookResponseDTO, Long id) {
        return EntityModel.of(
                bookResponseDTO,
                linkTo(methodOn(BookController.class).getBookById(id)).withSelfRel(),
                // Puedes agregar más enlaces aquí según lo necesites
                linkTo(methodOn(BookController.class).updateBook(id, null)).withRel("update").withType("PUT"),
                linkTo(methodOn(BookController.class).deleteBook(id)).withRel("delete").withType("DELETE")
        );
    }

    @Override
    public Optional<EntityModel<BookResponseDTO>> createBookWithLinks(BookRequestDTO bookRequestDTO) {
        // Primero, creamos el libro
        BookResponseDTO createdBook = createBook(bookRequestDTO);

        // Luego, envolvemos el DTO con los enlaces
        return Optional.of(convertToEntityModel(createdBook, createdBook.bookID()));
    }

    @Override
    public List<Optional<EntityModel<BookResponseDTO>>> createBooksWithLinks(List<BookRequestDTO> bookRequestDTOs) {
        // Creamos todos los libros
        List<BookResponseDTO> createdBooks = createBooks(bookRequestDTOs);

        // Envolvemos cada libro con los enlaces y lo colocamos en un Optional
        return createdBooks.stream()
                .map(book -> Optional.of(convertToEntityModel(book, book.bookID())))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<EntityModel<BookResponseDTO>> updateBookWithLinks(Long bookID, BookRequestDTO bookRequestDTO) {
        Optional<BookResponseDTO> updatedBook = updateBook(bookID, bookRequestDTO);

        return updatedBook.map(book -> Optional.of(convertToEntityModel(book, book.bookID())))
                .orElseGet(() -> Optional.empty());
    }



}
