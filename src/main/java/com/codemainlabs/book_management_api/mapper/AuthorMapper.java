package com.codemainlabs.book_management_api.mapper;

import com.codemainlabs.book_management_api.model.dto.author.AuthorIDDTO;
import com.codemainlabs.book_management_api.model.dto.author.AuthorRequestDTO;
import com.codemainlabs.book_management_api.model.dto.author.AuthorResponseDTO;
import com.codemainlabs.book_management_api.model.entity.Author;
import com.codemainlabs.book_management_api.util.serializer.NullableField; // Asegúrate que este import sea correcto
import org.mapstruct.*;

import java.time.LocalDate;
import java.util.List;

// ... otros imports ...

// ... otros imports ...

/**
 * AuthorMapper ahora solo depende de BookMapper (unidireccional a nivel de 'uses').
 */
@Mapper(componentModel = "spring",
        // Todavía necesita BookMapper para mapear la lista de libros en AuthorResponseDTO
        uses = { BookMapper.class })
public interface AuthorMapper {

    // --- Mapeo Author Entity -> AuthorResponseDTO ---
    @Mapping(source = "id", target = "authorID")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "bookCount", target = "bookCount")
    @Mapping(source = "alive", target = "isAlive")
    // MapStruct usará BookMapper.toBookResponseDTO para la lista 'books'
    AuthorResponseDTO toAuthorResponseDTO(Author author);

    List<AuthorResponseDTO> toAuthorResponseDTOList(List<Author> authors);

    // --- Mapeo Author Entity -> AuthorIDDTO ---
    // Este método puede quedarse aquí o moverse completamente a AuthorInfoMapper.
    // Si se queda, no causa problema porque BookMapper ya no usa AuthorMapper.
    // Por limpieza, podrías quitarlo si solo se usa internamente por BookMapper.
    // Si se usa en otro lugar, déjalo. Lo dejaremos por ahora.
    @Mapping(source = "id", target = "authorID")
    @Mapping(source = "name", target = "name")
    AuthorIDDTO toAuthorIDDTO(Author author);

    // ... resto de métodos de AuthorMapper sin cambios ...
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "books", expression = "java(new java.util.ArrayList<>())")
    @Mapping(target = "deathDate", source = "deathDate")
    Author toAuthor(AuthorRequestDTO authorRequestDTO);
    default LocalDate mapNullableFieldToLocalDate(NullableField<LocalDate> nullableField) {
        return (nullableField != null && nullableField.isPresent()) ? nullableField.getValue() : null;
    }
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "books", ignore = true)
    @Mapping(target = "deathDate", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAuthorFromDto(AuthorRequestDTO dto, @MappingTarget Author entity);
    @AfterMapping
    default void afterUpdateAuthorFromDto(AuthorRequestDTO dto, @MappingTarget Author entity) {
        if (dto.deathDate() != null && dto.deathDate().isPresent()) {
            entity.setDeathDate(dto.deathDate().getValue());
        }
    }
}