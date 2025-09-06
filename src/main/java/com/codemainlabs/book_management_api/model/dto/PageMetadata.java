package com.codemainlabs.book_management_api.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PageMetadata {
    @JsonProperty("size")
    private final long size;
    @JsonProperty("totalElements")
    private final long totalElements;
    @JsonProperty("totalPages")
    private final long totalPages;
    @JsonProperty("number")
    private final long number;
}