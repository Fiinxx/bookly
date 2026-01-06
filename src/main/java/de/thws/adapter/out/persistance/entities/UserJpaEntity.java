package de.thws.adapter.out.persistance.entities;

import de.thws.domain.model.Role;
import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@UserDefinition
@Data
@NoArgsConstructor()
public class UserJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Username
    private String username;
    private String email;
    @Password
    private String password;


    @Enumerated(EnumType.STRING)
    private Role role;

    @Roles
    public String getRoleName() {
        return role != null ? role.name() : null;
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RatingJpaEntity> ratings = new ArrayList<>();

}
