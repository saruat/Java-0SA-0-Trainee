package ru.saruat.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.saruat.entity.User;
import ru.saruat.util.HibernateUtil;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class UserDaoImplIntegrationTest {

    private PostgreSQLContainer<?> postgresContainer;
    private IUserDao userDao;

    @BeforeEach
    public void setUp() {
        postgresContainer = new PostgreSQLContainer<>("postgres:latest")
                .withDatabaseName("user_service_db")
                .withUsername("postgres")
                .withPassword("postgres");
        postgresContainer.start();

        // Настройка Hibernate для использования контейнера PostgreSQL
        System.setProperty("hibernate.connection.url", postgresContainer.getJdbcUrl());
        System.setProperty("hibernate.connection.username", postgresContainer.getUsername());
        System.setProperty("hibernate.connection.password", postgresContainer.getPassword());

        userDao = new UserDaoImpl();
    }

    @AfterEach
    public void tearDown() {
        postgresContainer.stop();
        deleteAll(); // Очистка базы данных после каждого теста

    }

    @Test
    public void testCreateUser() {
        User user = new User();
        user.setName("Test User");
        user.setEmail(generateUniqueEmail("test","example.com"));
        user.setAge(30);
        user.setCreatedAt(java.time.LocalDateTime.now());

        userDao.create(user);

        List<User> users = userDao.findAll();
        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals("Test User", users.get(0).getName());
    }

    @Test
    public void testReadUser() {
        User user = new User();
        user.setName("Test User");
        user.setEmail(generateUniqueEmail("test","example.com"));
        user.setAge(30);
        user.setCreatedAt(java.time.LocalDateTime.now());

        userDao.create(user);

        UUID userId = user.getId();
        User retrievedUser = userDao.read(userId).orElse(null);

        assertNotNull(retrievedUser);
        assertEquals("Test User", retrievedUser.getName());
    }

    @Test
    public void testUpdateUser() {
        User user = new User();
        user.setName("Test User");
        user.setEmail(generateUniqueEmail("test","example.com"));
        user.setAge(30);
        user.setCreatedAt(java.time.LocalDateTime.now());

        userDao.create(user);

        UUID userId = user.getId();
        User updatedUser = userDao.read(userId).orElse(null);
        updatedUser.setName("Updated User");
        userDao.update(updatedUser);

        User retrievedUser = userDao.read(userId).orElse(null);
        assertNotNull(retrievedUser);
        assertEquals("Updated User", retrievedUser.getName());
    }

    @Test
    public void testDeleteUser() {
        User user = new User();
        user.setName("Test User");
        user.setEmail(generateUniqueEmail("test","example.com"));
        user.setAge(30);
        user.setCreatedAt(java.time.LocalDateTime.now());

        userDao.create(user);

        UUID userId = user.getId();
        userDao.delete(userId);

        User retrievedUser = userDao.read(userId).orElse(null);
        assertNull(retrievedUser);
    }

    @Test
    public void testFindAllUsers() {
        deleteAll();
        User user1 = new User();
        user1.setName("User 1");
        user1.setEmail(generateUniqueEmail("user1","example.com"));
        user1.setAge(25);
        user1.setCreatedAt(java.time.LocalDateTime.now());

        User user2 = new User();
        user2.setName("User 2");
        user2.setEmail(generateUniqueEmail("user2","example.com"));
        user2.setAge(35);
        user2.setCreatedAt(java.time.LocalDateTime.now());

        userDao.create(user1);
        userDao.create(user2);

        List<User> users = userDao.findAll();
        assertNotNull(users);
        assertEquals(2, users.size());
    }

    private String generateUniqueEmail(String commonPart, String domain) {
        String uniquePart = String.valueOf(UUID.randomUUID());
        return commonPart +"-" + uniquePart + "@" + domain;
    }

    public void deleteAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createQuery("DELETE FROM User").executeUpdate();
            transaction.commit();
        } catch (Exception ex) {
            throw ex;
        }
    }
}
