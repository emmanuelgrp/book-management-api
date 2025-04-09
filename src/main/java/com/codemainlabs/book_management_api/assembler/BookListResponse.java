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
@JsonPropertyOrder({ "books", "_links" })
public class BookListResponse extends RepresentationModel<BookListResponse> {

    @JsonProperty("books")
    private final List<EntityModel<BookRepresentation>> books;

    public BookListResponse(List<EntityModel<BookRepresentation>> books, Link... links) {
        this.books = books;
        this.add(links);
    }
}