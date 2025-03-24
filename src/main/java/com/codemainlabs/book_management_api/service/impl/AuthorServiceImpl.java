package com.codemainlabs.book_management_api.service.impl;

import com.codemainlabs.book_management_api.exception.ResourceNotFoundException;
import com.codemainlabs.book_management_api.model.dto.AuthorRequestDTO;
import com.codemainlabs.book_management_api.model.dto.AuthorResponseDTO;
import com.codemainlabs.book_management_api.model.dto.BookResponseDTO;
import com.codemainlabs.book_management_api.model.entity.Author;
import com.codemainlabs.book_management_api.model.entity.Book;
import com.codemainlabs.book_management_api.repository.AuthorRepository;
import com.codemainlabs.book_management_api.service.AuthorService;
import com.codemainlabs.book_management_api.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final BookService bookService;


    @Override
    public List<AuthorResponseDTO> getAllAuthors() {
        return authorRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<AuthorResponseDTO> getAuthorById(Long id) {
        return authorRepository.findById(id)
                .map(this::convertToDto);
    }

    @Override
    public AuthorResponseDTO createAuthor(AuthorRequestDTO authorRequestDTO) {
        Author author = convertToEntity(authorRequestDTO);
        Author savedAuthor = authorRepository.save(author);
        return convertToDto(savedAuthor);
    }

    @Override
    public Optional<AuthorResponseDTO> updateAuthor(Long id, AuthorRequestDTO authorRequestDTO) {
        var author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + id));

        author.setFirstName(authorRequestDTO.firstName());
        author.setLastName(authorRequestDTO.lastName());
        author.setBirthDate(authorRequestDTO.birthDate());
        author.setDeathDate(authorRequestDTO.deathDate());
        author.setBiography(authorRequestDTO.biography());
        author.setNationality(authorRequestDTO.nationality());

        return Optional.of(convertToDto(authorRepository.save(author)));
    }

    @Override
    public void deleteAuthor(Long id) {
        if (!authorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Author not found with id: " + id);
        }
        authorRepository.deleteById(id);
    }

    private AuthorResponseDTO convertToDto(Author author) {
        List<BookResponseDTO> bookDtos = author.getBooks().stream()
                .map(bookService::convertToBookDto) // Convierte cada Book en un BookResponseDTO
                .collect(Collectors.toList());

        // Devolver el AuthorResponseDTO con la lista de libros convertidos
        return AuthorResponseDTO.builder()
                .id(author.getId())
                .name(author.getName())
                .biography(author.getBiography())
                .bookCount(author.getBookCount())
                .birthDate(author.getBirthDate())
                .nationality(author.getNationality())
                .isAlive(author.isAlive())
                .deathDate(author.getDeathDate())
                .books(bookDtos)
                .city(author.getCity())
                .build();
    }

    private Author convertToEntity(AuthorRequestDTO requestDTO) {
        if (requestDTO.id() != null) {
            return authorRepository.findById(requestDTO.id())
                    .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + requestDTO.id()));
        }
        return createNewAuthor(requestDTO);
    }

    @Override
    public List<AuthorResponseDTO> createAuthors(List<AuthorRequestDTO> authorRequestDTOs) {
        var authors = authorRequestDTOs.stream()
                .map(this::convertToEntity)
                .collect(Collectors.toList());

        return authorRepository.saveAll(authors)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    private Author createNewAuthor(AuthorRequestDTO requestDTO) {
        return Author.builder()
                .firstName(requestDTO.firstName())
                .lastName(requestDTO.lastName())
                .birthDate(requestDTO.birthDate())
                .deathDate(requestDTO.deathDate())
                .biography(requestDTO.biography())
                .nationality(requestDTO.nationality())
                .city(requestDTO.city())
                .books(new ArrayList<>()) // Evita NullPointerException
                .build();
    }


}