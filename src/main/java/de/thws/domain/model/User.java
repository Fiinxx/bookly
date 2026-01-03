package de.thws.domain.model;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class User {
    private long id;
    private String username;
    private String email;
    private String password;
    private Role role;
}
