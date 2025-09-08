package com.codemainlabs.book_management_api.model.dto.pagination;

import com.codemainlabs.book_management_api.assembler.author.AuthorRespresentation;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Getter
@JsonPropertyOrder({"authors", "page", "_links"})
public class PaginatedAuthorsResponse extends RepresentationModel<PaginatedAuthorsResponse> {

    @JsonProperty("authors")
    private final List<EntityModel<AuthorRespresentation>> authors;
    @JsonProperty("page")
    private final PageMetadata page;

    @JsonCreator
    public PaginatedAuthorsResponse(@JsonProperty("authors") List<EntityModel<AuthorRespresentation>> authors,
                                    @JsonProperty("page") PageMetadata page,
                                    @JsonProperty("_links") List<Link> links) {
        this.authors = authors;
        this.page = page;
        if (links != null) {
            this.add(links);
        }
    }
}