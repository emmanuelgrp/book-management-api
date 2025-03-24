package com.codemainlabs.book_management_api.service;

import com.codemainlabs.book_management_api.model.dto.BookResponseDTO;
import com.codemainlabs.book_management_api.model.entity.Book;

public interface BookService {
    BookResponseDTO convertToBookDto(Book book);

}
