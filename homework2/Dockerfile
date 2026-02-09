# Этап сборки приложения
FROM openjdk:27-ea-slim
# AS builder

# Установка Maven
RUN apt-get update && apt-get install -y maven

# Рабочая директория
# WORKDIR /app

# Копируем pom.xml и исходники
COPY pom.xml .
COPY .editorconfig .
COPY dependency-reduced-pom.xml .
COPY src ./src

# Строим приложение
RUN mvn clean package -DskipTests

# Последний этап (развёртывание с PostgreSQL)
#FROM postgres:18
RUN apt-get update && apt-get install -y postgresql


# Настройка PostgreSQL
ENV POSTGRES_USER=postgres1
ENV POSTGRES_PASSWORD=
ENV POSTGRES_DB=user_service_db

# Монтирование директории данных
VOLUME /var/lib/postgresql/data

# Создаём непривилегированного пользователя
RUN useradd -ms /bin/bash postgres-user

# Инициализируем PostgreSQL
RUN su postgres -c '/usr/lib/postgresql/*/bin/initdb -D /var/lib/postgresql/18/docker'

# Копируем собранный JAR-файл из предыдущего этапа , --from=builder /app
COPY  /target/homework2-1.0-SNAPSHOT.jar /app/app.jar

WORKDIR /app

# Запускаем PostgreSQL и затем приложение
USER postgres-user
ENTRYPOINT ["sh", "-c", "su postgres -c '/usr/lib/postgresql/*/bin/postgres &' && sleep 5 && java -jar app.jar"]
