package ru.saruat.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserDTO (
        UUID id,
       String name,
       String email,
       int age,
       LocalDateTime createdAt
) {

    public static UserDTO of(String name, String email, int age) {
        return new UserDTO(null, name, email, age, null);
    }

    @Override
    public String toString() {
        return String.format("User{id=%s, name=%s, email=%s, age=%s}",id,name, email, age);

    }
}