package com.codemainlabs.book_management_api.service;


import com.codemainlabs.book_management_api.model.dto.AuthorRequestDTO;
import com.codemainlabs.book_management_api.model.dto.AuthorResponseDTO;

import java.util.List;
import java.util.Optional;

public interface AuthorService {

    List<AuthorResponseDTO> getAllAuthors();
    Optional<AuthorResponseDTO> getAuthorById(Long id);
    AuthorResponseDTO createAuthor(AuthorRequestDTO authorRequestDTO);
    Optional<AuthorResponseDTO> updateAuthor(Long id, AuthorRequestDTO authorRequestDTO);
    void deleteAuthor(Long id);
    List<AuthorResponseDTO> createAuthors(List<AuthorRequestDTO> authorRequestDTOs);

}