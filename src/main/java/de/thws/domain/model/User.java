package de.thws.domain.model;
import lombok.Data;

@Data
public class User {
String id;
String username;
String email;
String password;
Role role;

public User(){
    super();
}

public User(String id, String username, String email, String password, Role role) {
    this.id = id;
    this.username = username;
    this.email = email;
    this.password = password;
    this.role = role;
}
}
