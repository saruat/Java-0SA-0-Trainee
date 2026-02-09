package ru.saruat;

import ru.saruat.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.saruat.service.IUserService;
import ru.saruat.service.UserServiceImpl;

import java.io.BufferedOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import static org.apache.logging.log4j.LogManager.*;

public class Main {
    private static final Logger logger = getLogger(Main.class);
    private static final PrintStream syncOut = new PrintStream(new BufferedOutputStream(System.out), true);

    private static final IUserService userService = new UserServiceImpl();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        logger.info("Начало работы приложения");

        while (true) {
            printMenu();
            int choice = scanner.nextInt();
            scanner.nextLine(); // очистка буфера от символа перевода строки

            switch (choice) {
                case 1:
                    createUser();
                    break;
                case 2:
                    readUser();
                    break;
                case 3:
                    updateUser();
                    break;
                case 4:
                    deleteUser();
                    break;
                case 5:
                    listAllUsers();
                    break;
                case 6:
                    logger.info("Работа приложения завершена");
                    System.exit(0);
                    break;
                default:
                    logger.warn("Нет такого пункта, введите число от 1 до 6");
            }
        }
    }

    private static void printMenu() {
        syncOut.println("\nМеню:");
        syncOut.println("1. Создать пользователя");
        syncOut.println("2. Просмотреть пользователя");
        syncOut.println("3. Обновить пользователя");
        syncOut.println("4. Удалить пользователя");
        syncOut.println("5. Просмотреть всех пользователей");
        syncOut.println("6. Выход");
        syncOut.print("Ваш выбор: ");
    }

    private static void createUser() {
        syncOut.print("Введите имя: ");
        String name = scanner.nextLine();
        syncOut.print("Введите email: ");
        String email = scanner.nextLine();
        syncOut.print("Введите возраст: ");
        int age = scanner.nextInt();
        scanner.nextLine(); // очистка буфера после ввода целого числа

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setAge(age);
        user.setCreatedAt(LocalDateTime.now());

        userService.create(user);
        logger.info("Пользователь создан.");
    }

    private static void readUser() {
        syncOut.print("Введите ID пользователя: ");
        String stringId = scanner.nextLine();

        UUID uuidId = UUID.fromString(stringId);
        User user = userService.read(uuidId);
        if (user != null) {
            logger.info("Пользователь: " + user.toString());
        } else {
            logger.warn("Пользователь с таким ID не найден.");
        }
    }

    private static void updateUser() {
        syncOut.print("Введите ID для внесения изменений: ");
        String stringId = scanner.nextLine();

        UUID uuidId = UUID.fromString(stringId);
        User user = userService.read(uuidId);
        if (user != null) {
            syncOut.print("Введите новое имя: ");
            String name = scanner.nextLine();
            syncOut.print("Введите новый email: ");
            String email = scanner.nextLine();
            syncOut.print("Введите новый возраст: ");
            int age = scanner.nextInt();
            scanner.nextLine(); // очистка буфера после ввода целого числа

            user.setName(name);
            user.setEmail(email);
            user.setAge(age);

            userService.update(user);
            logger.info("Данные обновлены.");
        } else {
            logger.warn("Пользователь с таким ID не найден.");
        }
    }

    private static void deleteUser() {
        syncOut.print("Введите ID пользователя для удаления: ");
        String stringId = scanner.nextLine();

        UUID uuidId = UUID.fromString(stringId);
        userService.delete(uuidId);
        logger.info("Пользователь успешно удален.");
    }

    private static void listAllUsers() {
        List<User> users = userService.findAll();
        if (users.isEmpty()) {
            logger.info("Нет пользователей в базе данных.");
        } else {
            logger.info("Список пользователей:");
            for (User user : users) {
                System.out.println(user.toString());
            }
        }
    }
}
