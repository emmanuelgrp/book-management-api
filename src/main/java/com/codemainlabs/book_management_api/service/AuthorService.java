package com.codemainlabs.book_management_api.service;


import com.codemainlabs.book_management_api.model.dto.author.AuthorRequestDTO;
import com.codemainlabs.book_management_api.model.dto.author.AuthorResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AuthorService {

    Page<AuthorResponseDTO> getAllAuthors(Pageable pageable);
    Optional<AuthorResponseDTO> getAuthorById(Long id);
    AuthorResponseDTO createAuthor(AuthorRequestDTO authorRequestDTO);
    Optional<AuthorResponseDTO> updateAuthor(Long id, AuthorRequestDTO authorRequestDTO);
    void deleteAuthor(Long id);
    List<AuthorResponseDTO> createAuthors(List<AuthorRequestDTO> authorRequestDTOs);

}