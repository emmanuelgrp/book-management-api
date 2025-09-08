package com.codemainlabs.book_management_api.mapper;

import com.codemainlabs.book_management_api.exception.ResourceNotFoundException;
import com.codemainlabs.book_management_api.model.dto.book.BookRequestDTO;
import com.codemainlabs.book_management_api.model.dto.book.BookResponseDTO;
import com.codemainlabs.book_management_api.model.entity.Author;
import com.codemainlabs.book_management_api.model.entity.Book;
import com.codemainlabs.book_management_api.repository.AuthorRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


// ... otros imports ...

// ... otros imports ...

@Mapper(componentModel = "spring",
        // Ahora usa AuthorInfoMapper en lugar de AuthorMapper completo
        uses = { AuthorInfoMapper.class },
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class BookMapper {

    protected AuthorRepository authorRepository;
    // Inyecta el AuthorInfoMapper (MapStruct lo usará internamente)
    // protected AuthorInfoMapper authorInfoMapper; // No necesitas inyectarlo explícitamente aquí si está en 'uses'

    @Autowired
    public void setAuthorRepository(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    // --- Mapeo Book Entity -> BookResponseDTO ---
    @Mapping(source = "id", target = "bookID")
    // MapStruct ahora usará AuthorInfoMapper.toAuthorIDDTO para esta conversión
    @Mapping(source = "authors", target = "authors")
    public abstract BookResponseDTO toBookResponseDTO(Book book);

    // ... resto de métodos de BookMapper sin cambios ...
    public abstract List<BookResponseDTO> toBookResponseDTOList(List<Book> books);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "authors", ignore = true)
    public abstract Book toBook(BookRequestDTO bookRequestDTO);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "authors", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updateBookFromDto(BookRequestDTO dto, @MappingTarget Book entity);
    @AfterMapping
    protected void afterToBook(BookRequestDTO dto, @MappingTarget Book book) {
        book.setAuthors(mapAuthorIdsToAuthors(dto.authorIds()));
    }
    @AfterMapping
    protected void afterUpdateBookFromDto(BookRequestDTO dto, @MappingTarget Book book) {
        if (dto.authorIds() != null) {
            book.setAuthors(mapAuthorIdsToAuthors(dto.authorIds()));
        }
    }
    protected List<Author> mapAuthorIdsToAuthors(List<Long> authorIds) {
        if (authorIds == null || authorIds.isEmpty()) {
            return Collections.emptyList();
        }
        if (this.authorRepository == null) {
            throw new IllegalStateException("AuthorRepository no fue inyectado en BookMapper.");
        }
        return authorIds.stream()
                .map(id -> this.authorRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Author not found with authorID: " + id)))
                .collect(Collectors.toList());
    }
}