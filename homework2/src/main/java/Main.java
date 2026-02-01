import entity.User;
import service.IUserService;
import service.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class Main {

    private static final IUserService userService = new UserServiceImpl();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
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
                    System.out.println("Работа приложения завершена");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Нет такого пункта, введите число от 1 до 6");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\nМеню:");
        System.out.println("1. Создать пользователя");
        System.out.println("2. Просмотреть пользователя");
        System.out.println("3. Обновить пользователя");
        System.out.println("4. Удалить пользователя");
        System.out.println("5. Просмотреть всех пользователей");
        System.out.println("6. Выход");
        System.out.print("Ваш выбор: ");
    }

    private static void createUser() {
        System.out.print("Введите имя: ");
        String name = scanner.nextLine();
        System.out.print("Введите email: ");
        String email = scanner.nextLine();
        System.out.print("Введите возраст: ");
        int age = scanner.nextInt();
        scanner.nextLine(); // очистка буфера после ввода целого числа

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setAge(age);
        user.setCreatedAt(LocalDateTime.now());

        userService.create(user);
        System.out.println("Пользователь создан.");
    }

    private static void readUser() {
        System.out.print("Введите ID пользователя: ");
        String stringId = scanner.nextLine();

        UUID uuidId = UUID.fromString(stringId);
        User user = userService.read(uuidId);
        if (user != null) {
            System.out.println("Пользователь: " + user.toString());
        } else {
            System.out.println("Пользователь с таким ID не найден.");
        }
    }

    private static void updateUser() {
        System.out.print("Введите ID пользователя для внесения изменений: ");
        String stringId = scanner.nextLine();

        UUID uuidId = UUID.fromString(stringId);
        User user = userService.read(uuidId);
        if (user != null) {
            System.out.print("Введите новое имя пользователя: ");
            String name = scanner.nextLine();
            System.out.print("Введите новый email пользователя: ");
            String email = scanner.nextLine();
            System.out.print("Введите новый возраст пользователя: ");
            int age = scanner.nextInt();
            scanner.nextLine(); // очистка буфера после ввода целого числа

            user.setName(name);
            user.setEmail(email);
            user.setAge(age);

            userService.update(user);
            System.out.println("Данные обновлены.");
        } else {
            System.out.println("Пользователь с таким ID не найден.");
        }
    }

    private static void deleteUser() {
        System.out.print("Введите ID пользователя для удаления: ");
        String stringId = scanner.nextLine();

        UUID uuidId = UUID.fromString(stringId);
        userService.delete(uuidId);
        System.out.println("Пользователь успешно удален.");
    }

    private static void listAllUsers() {
        List<User> users = userService.findAll();
        if (users.isEmpty()) {
            System.out.println("Нет пользователей в базе данных.");
        } else {
            System.out.println("Список пользователей:");
            for (User user : users) {
                System.out.println(user.toString());
            }
        }
    }
}
