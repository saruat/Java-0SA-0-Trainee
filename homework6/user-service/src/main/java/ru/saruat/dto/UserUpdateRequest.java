package ru.saruat.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Запрос на обновление пользователя", example = """
{
  "name": "Иван Иванов",
  "email": "ivan@example.com",
  "age": 25
}
        """)
public record UserUpdateRequest(
        @Schema(description = "Имя пользователя", example = "Иван Иванов")
        String name,

        @Schema(description = "Email пользователя", example = "ivan@example.com")
        String email,

        @Schema(description = "Возраст пользователя", example = "26", minimum = "1", maximum = "120")
        int age
) {}