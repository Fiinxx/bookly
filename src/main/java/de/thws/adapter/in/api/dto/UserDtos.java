package de.thws.adapter.in.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;

public class UserDtos {
    public record Detail(
            @PositiveOrZero
            long id,
            String username,

            @Email
            String email,

            @NotBlank
            @Pattern(regexp = "ADMIN|USER", message = "Role must be ADMIN or USER")
            String role
    ){}
}
