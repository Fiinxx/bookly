package de.thws.domain.model;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class User {
String id;
String username;
String email;
String password;
Role role;




}
