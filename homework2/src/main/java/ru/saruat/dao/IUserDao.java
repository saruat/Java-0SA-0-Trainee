package ru.saruat.dao;

import ru.saruat.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface IUserDao {
    void create(User user);
    Optional<User> read(UUID id);
    void update(User user);
    void delete(UUID id);
    List<User> findAll();
}