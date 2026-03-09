package ru.saruat.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.saruat.dto.UserCreateRequest;
import ru.saruat.dto.UserDTO;
import ru.saruat.dto.UserUpdateRequest;
import ru.saruat.resource.UserResource;
import ru.saruat.service.IUserService;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User API", description = "CRUD операции с пользователями")
public class UserController {

    private final IUserService userService;

    @Autowired
    public UserController(IUserService userService){
        this.userService = userService;
    }

    @Operation(summary = "Получить всех пользователей", description = "Возвращает список пользователей с гиперссылками")
    @ApiResponse(responseCode = "200", description = "Успешное получение списка",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserResource.class))))
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<UserResource>>> getAllUsers() {
        List<EntityModel<UserResource>> users = userService.findAll().stream()
                .map(UserResource::new)
                .map(userResource -> userResource.add(
                        linkTo(methodOn(UserController.class).getUser(userResource.getId())).withSelfRel()))
                .map(EntityModel::of)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<UserResource>> collection = CollectionModel.of(users);
        collection.add(linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel());

        return ResponseEntity.ok(collection);
    }

    @Operation(summary = "Получить пользователя по ID", description = "Возвращает одного пользователя с ссылками")
    @ApiResponse(responseCode = "200", description = "Найден",
            content = @Content(schema = @Schema(implementation = UserResource.class)))
    @ApiResponse(responseCode = "404", description = "Не найден")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<UserResource>> getUser(@PathVariable UUID id) {
        UserDTO userDTO = userService.findById(id);
        if (userDTO == null) {
            return ResponseEntity.notFound().build();
        }

        UserResource resource = new UserResource(userDTO);
        resource.add(linkTo(methodOn(UserController.class).getUser(id)).withSelfRel());
        resource.add(linkTo(methodOn(UserController.class).updateUser(id, null)).withRel("update"));
        resource.add(linkTo(methodOn(UserController.class).deleteUser(id)).withRel("delete"));

        return ResponseEntity.ok(EntityModel.of(resource));
    }

    @Operation(summary = "Создать нового пользователя", description = "Создаёт пользователя и возвращает с ссылкой. ID и дата создания генерируются автоматически")
    @ApiResponse(responseCode = "201", description = "Создан",
            content = @Content(schema = @Schema(implementation = UserResource.class)))
    @PostMapping
    public ResponseEntity<EntityModel<UserResource>> createUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для создания пользователя",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UserCreateRequest.class)))
            @RequestBody @Schema(description = "Данные для создания пользователя") UserCreateRequest request) {
        UserDTO userDTO = UserDTO.of(request.name(), request.email(), request.age());
        UserDTO created = userService.create(userDTO);
        UserResource resource = new UserResource(created);

        resource.add(linkTo(methodOn(UserController.class).getUser(created.id())).withSelfRel());

        return ResponseEntity
                .created(URI.create("/api/users/" + created.id()))
                .body(EntityModel.of(resource));
    }

    @Operation(summary = "Обновить пользователя", description = "Частичное или полное обновление пользователя по ID")
    @ApiResponse(responseCode = "200", description = "Обновлён",
            content = @Content(schema = @Schema(implementation = UserResource.class)))
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<UserResource>> updateUser(
            @PathVariable UUID id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для обновления пользователя",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UserUpdateRequest.class)))
            @RequestBody @Schema(description = "Данные для обновления пользователя") UserUpdateRequest request) {

        UserDTO existing = userService.findById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }

        UserDTO updatedDTO = new UserDTO(
                id,
                request.name() != null ? request.name() : existing.name(),
                request.email() != null ? request.email() : existing.email(),
                request.age() != 0 ? request.age() : existing.age(),
                existing.createdAt()
        );

        UserDTO updated = userService.update(id, updatedDTO);
        UserResource resource = new UserResource(updated);

        resource.add(linkTo(methodOn(UserController.class).getUser(id)).withSelfRel());
        resource.add(linkTo(methodOn(UserController.class).getAllUsers()).withRel("users"));

        return ResponseEntity.ok(EntityModel.of(resource));
    }

    @Operation(summary = "Удалить пользователя", description = "Удаляет пользователя по ID")
    @ApiResponse(responseCode = "204", description = "Успешно удалён")
    @ApiResponse(responseCode = "404", description = "Не найден")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable UUID id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
