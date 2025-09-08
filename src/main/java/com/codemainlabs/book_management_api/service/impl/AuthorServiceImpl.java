package com.codemainlabs.book_management_api.service.impl;

import com.codemainlabs.book_management_api.exception.ResourceNotFoundException;
import com.codemainlabs.book_management_api.mapper.AuthorMapper;
import com.codemainlabs.book_management_api.model.dto.author.AuthorRequestDTO;
import com.codemainlabs.book_management_api.model.dto.author.AuthorResponseDTO;
import com.codemainlabs.book_management_api.model.entity.Author;
import com.codemainlabs.book_management_api.repository.AuthorRepository;
import com.codemainlabs.book_management_api.service.AuthorService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    @Override
    @Cacheable("authorsPageable")
    public Page<AuthorResponseDTO> getAllAuthors(Pageable pageable) {
        return authorRepository.findAll(pageable)
                .map(authorMapper::toAuthorResponseDTO);
    }

    @Override
    @Cacheable(value = "authors", key = "#id")
    public Optional<AuthorResponseDTO> getAuthorById(Long id) {
        return authorRepository.findById(id)
                .map(authorMapper::toAuthorResponseDTO);
    }

    @Override
    @Transactional
    @CacheEvict(value = "authorsPageable", allEntries = true)
    public AuthorResponseDTO createAuthor(AuthorRequestDTO authorRequestDTO) {
        return authorMapper.toAuthorResponseDTO(
                authorRepository.save(
                        authorMapper.toAuthor(authorRequestDTO)
                )
        );
    }

    @Override
    @Transactional
    @CachePut(value = "authors", key = "#id")
    @CacheEvict(value = "authorsPageable", allEntries = true)
    public Optional<AuthorResponseDTO> updateAuthor(Long id, AuthorRequestDTO authorRequestDTO) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with authorID: " + id));

        authorMapper.updateAuthorFromDto(authorRequestDTO, author);
        Author updatedAuthor = authorRepository.save(author);

        return Optional.of(authorMapper.toAuthorResponseDTO(updatedAuthor));
    }

    @Override
    @Transactional
    @CacheEvict(value = {"authors", "authorsPageable"}, allEntries = true)
    public void deleteAuthor(Long id) {
        if (!authorRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found with ID: " + id);
        }
        authorRepository.deleteBookAssociations(id);
        authorRepository.deleteById(id);
    }

    @Override
    @Transactional
    @CacheEvict(value = "authorsPageable", allEntries = true)
    public List<AuthorResponseDTO> createAuthors(List<AuthorRequestDTO> authorRequestDTOs) {
        var authors = authorRequestDTOs.stream()
                .map(authorMapper::toAuthor)
                .collect(Collectors.toList());

        List<Author> savedAuthors = authorRepository.saveAll(authors);

        return savedAuthors.stream()
                .map(authorMapper::toAuthorResponseDTO)
                .collect(Collectors.toList());
    }

}