# Используем легковесный образ Java 21
FROM eclipse-temurin:21-jre-jammy

# Рабочая директория
WORKDIR /app

# Копируем JAR из target
COPY target/*.jar app.jar

# Открываем порт 8080
EXPOSE 8080

# Запускаем только Spring Boot приложение
ENTRYPOINT ["java", "-jar", "app.jar"]