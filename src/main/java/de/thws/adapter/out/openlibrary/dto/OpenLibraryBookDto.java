package de.thws.adapter.out.openlibrary.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OpenLibraryBookDto(
        String title,
        @JsonProperty("number_of_pages") int numberOfPages,
        List<String> publishers,
        @JsonProperty("publish_date") String publishDate
) {}
