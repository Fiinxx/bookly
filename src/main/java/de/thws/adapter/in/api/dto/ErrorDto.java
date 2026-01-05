package de.thws.adapter.in.api.dto;

public record ErrorDto(
        String message,
        int status,
        String path
) {
}
