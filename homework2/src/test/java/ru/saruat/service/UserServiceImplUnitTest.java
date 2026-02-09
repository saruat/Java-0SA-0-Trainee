package ru.saruat.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.saruat.dao.IUserDao;
import ru.saruat.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplUnitTest {

    @Mock
    private IUserDao userDao;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        userService = new UserServiceImpl();
        userService.setUserDaoForTesting(userDao); // подменяем
    }

    @Test
    public void testCreateUser() {
        User user = new User();
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setAge(30);
        user.setCreatedAt(java.time.LocalDateTime.now());

        userService.create(user);

        verify(userDao, times(1)).create(user);
    }

    @Test
    public void testReadUser() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setAge(30);
        user.setCreatedAt(java.time.LocalDateTime.now());

        when(userDao.read(userId)).thenReturn(Optional.of(user));

        User retrievedUser = userService.read(userId);

        assertNotNull(retrievedUser);
        assertEquals("Test User", retrievedUser.getName());
    }

    @Test
    public void testUpdateUser() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setAge(30);
        user.setCreatedAt(java.time.LocalDateTime.now());

        //when(userDao.read(userId)).thenReturn(Optional.of(user));

        User updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setName("Updated User");
        updatedUser.setEmail("updated@example.com");
        updatedUser.setAge(35);
        updatedUser.setCreatedAt(java.time.LocalDateTime.now());

        userService.update(updatedUser);

        verify(userDao, times(1)).update(updatedUser);
    }

    @Test
    public void testDeleteUser() {
        UUID userId = UUID.randomUUID();

        userService.delete(userId);

        verify(userDao, times(1)).delete(userId);
    }

    @Test
    public void testFindAllUsers() {
        User user1 = new User();
        user1.setName("User 1");
        user1.setEmail("user1@example.com");
        user1.setAge(25);
        user1.setCreatedAt(java.time.LocalDateTime.now());

        User user2 = new User();
        user2.setName("User 2");
        user2.setEmail("user2@example.com");
        user2.setAge(35);
        user2.setCreatedAt(java.time.LocalDateTime.now());

        when(userDao.findAll()).thenReturn(List.of(user1, user2));

        List<User> users = userService.findAll();

        assertNotNull(users);
        assertEquals(2, users.size());
    }
}