package de.thws.adapter.in.api.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class UserDTO {
    @PositiveOrZero
    private long id;
    private String username;
    @Email
    private String email;
    private String password;

    @NotBlank
    @Pattern(regexp = "ADMIN|USER", message = "Role must be ADMIN or USER")
    private String role;
}
