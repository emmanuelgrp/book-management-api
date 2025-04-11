package com.codemainlabs.book_management_api.mapper;


import com.codemainlabs.book_management_api.model.dto.AuthorIDDTO;
import com.codemainlabs.book_management_api.model.entity.Author;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper auxiliar para proporcionar información básica de Author (como AuthorIDDTO)
 * sin crear una dependencia directa con BookMapper.
 */
@Mapper(componentModel = "spring") // Es un bean de Spring
public interface AuthorInfoMapper {

    @Mapping(source = "id", target = "authorID")
    @Mapping(source = "name", target = "name") // Usa Author.getName()
    AuthorIDDTO toAuthorIDDTO(Author author);

    // No necesita 'uses = { BookMapper.class }'
}