package ru.saruat.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import ru.saruat.dto.UserDTO;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

@Service
public class ConsoleService {
    private static final Logger logger = LogManager.getLogger(ConsoleService.class);
    private final ClientService clientService;
    private Scanner scanner = new Scanner(System.in);

    public ConsoleService(ClientService clientService) {
        this.clientService = clientService;
        // Убедимся, что System.out использует UTF-8
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
    }

    public void runConsole() {
        while (true) {
            printMenu();
            int choice = scanner.nextInt();
            scanner.nextLine(); // очистка буфера от символа перевода строки

            switch (choice) {
                case 1 -> createUser();
                case 2 -> readUser();
                case 3 -> updateUser();
                case 4 -> deleteUser();
                case 5 -> listAllUsers();
                case 6 -> {
                    logger.info("Завершение работы...");
                    clientService.stopApplication();
                }
                default -> logger.info("Нет такого пункта, введите число от 1 до 6");
            }
        }
    }

    private void printMenu() {
        System.out.println("\nМеню:");
        System.out.println("1. Создать пользователя");
        System.out.println("2. Просмотреть пользователя");
        System.out.println("3. Обновить пользователя");
        System.out.println("4. Удалить пользователя");
        System.out.println("5. Просмотреть всех пользователей");
        System.out.println("6. Выход");
        System.out.print("Ваш выбор: ");
    }

    private void createUser() {
        System.out.println("Введите имя: ");
        String name = scanner.nextLine();

        System.out.println("Введите email: ");
        String email = scanner.nextLine();

        System.out.println("Введите возраст: ");
        int age = scanner.nextInt();
        scanner.nextLine(); // очистка

        UserDTO userDTO = UserDTO.of(name, email, age);
        UserDTO createdUser = clientService.createUser(userDTO);
        logger.info("Пользователь создан: {}", createdUser);
    }

    private void readUser() {
        System.out.println("Введите ID пользователя: ");
        try {
            UUID uuidId = UUID.fromString(scanner.nextLine());
            UserDTO user = clientService.getUser(uuidId);
            if (user != null) {
                logger.info("Пользователь: {}", user.toString());
            } else {
                logger.info("Пользователь с таким ID не найден.");
            }
        } catch (IllegalArgumentException e) {
            logger.info("Некорректный формат ID.");
        }
    }

    private void updateUser() {
        System.out.println("Введите ID для внесения изменений: ");

        try {
            UUID uuidId = UUID.fromString(scanner.nextLine());
            UserDTO user = clientService.getUser(uuidId);
            if (user != null) {
                System.out.println("Введите новое имя: ");
                String name = scanner.nextLine();

                System.out.println("Введите новый email: ");
                String email = scanner.nextLine();

                System.out.println("Введите новый возраст: ");
                int age = scanner.nextInt();
                scanner.nextLine(); // очистка буфера после ввода целого числа

                UserDTO updatedUser = UserDTO.of(name, email, age);
                clientService.updateUser(uuidId, updatedUser);
                logger.info("Данные обновлены: " + updatedUser);
            }
        } catch (IllegalArgumentException e) {
            logger.info("Некорректный формат ID.");
        }
    }

    private void deleteUser() {
        System.out.println("Введите ID пользователя для удаления: ");
        try {
            UUID uuidId = UUID.fromString(scanner.nextLine());
            clientService.deleteUser(uuidId);
            logger.info("Пользователь успешно удален.");
        } catch (IllegalArgumentException e) {
            logger.info("Некорректный формат ID.");
        }
    }

    private void listAllUsers() {
        List<UserDTO> users = clientService.getAllUsers();
        if (users.isEmpty()) {
            logger.info("Нет пользователей в базе данных.");
        } else {
            System.out.println("Список пользователей:");
            users.forEach(System.out::println);
        }
    }
}