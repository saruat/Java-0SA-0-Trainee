# Используем легковесный образ Java
FROM maven:3.9.6-eclipse-temurin-17 AS builder

# Рабочая директория
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
RUN apk add --no-cache curl
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

# Открываем порт 
EXPOSE 8888

# Запускаем только Spring Boot приложение
ENTRYPOINT ["java", "-jar", "app.jar"]