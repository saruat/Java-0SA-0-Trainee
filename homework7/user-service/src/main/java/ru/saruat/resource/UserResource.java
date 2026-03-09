package ru.saruat.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.hateoas.RepresentationModel;
import ru.saruat.dto.UserDTO;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResource extends RepresentationModel<UserResource> {

    private UUID id;
    private String name;
    private String email;
    private int age;
    private LocalDateTime createdAt;

    public UserResource() {}

    public UserResource(UserDTO userDTO) {
        if (userDTO == null) return;
        this.id = userDTO.id();
        this.name = userDTO.name();
        this.email = userDTO.email();
        this.age = userDTO.age();
        this.createdAt = userDTO.createdAt();
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}