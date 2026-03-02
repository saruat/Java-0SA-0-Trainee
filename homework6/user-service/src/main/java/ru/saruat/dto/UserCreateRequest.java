package ru.saruat.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Запрос на создание нового пользователя", example = """
{
  "name": "Иван Иванов",
  "email": "ivan@example.com",
  "age": 25
}
        """)
public record UserCreateRequest(
        @Schema(description = "Имя пользователя", example = "Иван Иванов", requiredMode = Schema.RequiredMode.REQUIRED)
        String name,

        @Schema(description = "Email пользователя", example = "ivan@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
        String email,

        @Schema(description = "Возраст пользователя", example = "25", minimum = "1", maximum = "120")
        int age
) {}