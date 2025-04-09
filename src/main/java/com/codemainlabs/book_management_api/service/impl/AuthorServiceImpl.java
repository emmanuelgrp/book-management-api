package com.codemainlabs.book_management_api.service.impl;

import com.codemainlabs.book_management_api.exception.ResourceNotFoundException;
import com.codemainlabs.book_management_api.model.dto.AuthorRequestDTO;
import com.codemainlabs.book_management_api.model.dto.AuthorResponseDTO;
import com.codemainlabs.book_management_api.model.dto.BookResponseDTO;
import com.codemainlabs.book_management_api.model.entity.Author;
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
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with authorID: " + id));

        author.setFirstName(authorRequestDTO.firstName() != null ? authorRequestDTO.firstName() : author.getFirstName());
        author.setLastName(authorRequestDTO.lastName() != null ? authorRequestDTO.lastName() : author.getLastName());
        author.setBirthDate(authorRequestDTO.birthDate() != null ? authorRequestDTO.birthDate() : author.getBirthDate());


        // Si el campo deathDate fue enviado en el JSON, actualiza su valor (puede ser un valor o null)
        if (authorRequestDTO.deathDate() != null && authorRequestDTO.deathDate().isPresent()) {
            author.setDeathDate(authorRequestDTO.deathDate().getValue());
        }

        author.setBiography(authorRequestDTO.biography() != null ? authorRequestDTO.biography() : author.getBiography());
        author.setNationality(authorRequestDTO.nationality() != null ? authorRequestDTO.nationality() : author.getNationality());
        author.setCity(authorRequestDTO.city() != null ? authorRequestDTO.city() : author.getCity());

        return Optional.of(convertToDto(authorRepository.save(author)));
    }


    @Override
    public void deleteAuthor(Long id) {
        if (!authorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Author not found with authorID: " + id);
        }
        authorRepository.deleteById(id);
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

    private AuthorResponseDTO convertToDto(Author author) {
        List<BookResponseDTO> bookDtos = author.getBooks().stream()
                .map(bookService::convertToBookDto) // Convierte cada Book en un BookResponseDTO
                .collect(Collectors.toList());

        // Devolver el AuthorResponseDTO con la lista de libros convertidos
        return AuthorResponseDTO.builder()
                .authorID(author.getId())
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
        if (requestDTO.authorID() != null) {
            return authorRepository.findById(requestDTO.authorID())
                    .orElseThrow(() -> new ResourceNotFoundException("Author not found with authorID: " + requestDTO.authorID()));
        }
        return createNewAuthor(requestDTO);
    }

    private Author createNewAuthor(AuthorRequestDTO requestDTO) {
        return Author.builder()
                .firstName(requestDTO.firstName())
                .lastName(requestDTO.lastName())
                .birthDate(requestDTO.birthDate())
                .deathDate(requestDTO.deathDate().getValue())
                .biography(requestDTO.biography())
                .nationality(requestDTO.nationality())
                .city(requestDTO.city())
                .books(new ArrayList<>()) // Evita NullPointerException
                .build();
    }


}