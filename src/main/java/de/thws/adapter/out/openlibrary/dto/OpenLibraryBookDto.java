package de.thws.adapter.out.openlibrary.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OpenLibraryBookDto(
        String title,

        @JsonProperty("author_name")
        List<String> authors,

        @JsonProperty("number_of_pages_median")
        int numberOfPages, // Use Integer to handle nulls safely

        @JsonProperty("publisher")
        List<String> publishers,

        @JsonProperty("first_sentence")
        List<String> firstSentence,

        @JsonProperty("first_publish_year")
        int publishingYear
) {}
// title,author_name,number_of_pages_median,first_sentence,first_publish_year
