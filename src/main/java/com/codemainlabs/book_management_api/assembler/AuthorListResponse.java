package com.codemainlabs.book_management_api.assembler;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({ "authors", "_links" })
public class AuthorListResponse extends RepresentationModel<AuthorListResponse> {

    @JsonProperty("authors")
    private final List<EntityModel<AuthorRespresentation>> authors;

    public AuthorListResponse(List<EntityModel<AuthorRespresentation>> authors, Link... links) {
        this.authors = authors;
        this.add(links);
    }
}