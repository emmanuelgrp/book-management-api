package com.codemainlabs.book_management_api.service.impl;

import com.codemainlabs.book_management_api.model.dto.BookResponseDTO;
import com.codemainlabs.book_management_api.model.entity.Book;
import com.codemainlabs.book_management_api.service.BookService;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {

    @Override
    public BookResponseDTO convertToBookDto(Book book) {
        return BookResponseDTO.builder()
                .id(book.getId())
                .title(book.getTitle())
                .isbn(book.getIsbn())
                .synopsis(book.getSynopsis())
                .authorName(book.getAuthor().getName()) // Solo el nombre del autor para evitar recursión
                .publicationDate(book.getPublicationDate())
                .price(book.getPrice())
                .genre(book.getGenre())
                .pageCount(book.getPageCount())
                .build();
    }
}
