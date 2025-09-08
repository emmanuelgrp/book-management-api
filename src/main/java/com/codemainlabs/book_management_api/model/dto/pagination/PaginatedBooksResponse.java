package com.codemainlabs.book_management_api.model.dto.pagination;

import com.codemainlabs.book_management_api.assembler.book.BookRepresentation;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Getter
@JsonPropertyOrder({"books", "page", "_links"})
public class PaginatedBooksResponse extends RepresentationModel<PaginatedBooksResponse> {

    @JsonProperty("books")
    private final List<EntityModel<BookRepresentation>> books;
    @JsonProperty("page")
    private final PageMetadata page;

    @JsonCreator
    public PaginatedBooksResponse(@JsonProperty("books") List<EntityModel<BookRepresentation>> books,
                                  @JsonProperty("page") PageMetadata page,
                                  @JsonProperty("_links") List<Link> links) {
        this.books = books;
        this.page = page;
        if (links != null) {
            this.add(links);
        }
    }
}