package de.thws.adapter.out.openlibrary;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.thws.adapter.out.openlibrary.dto.OpenLibraryBookDto;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OpenLibraryResponse(
        List<OpenLibraryBookDto> docs
) {}
