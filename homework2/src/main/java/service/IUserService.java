package service;

import entity.User;

import java.util.List;
import java.util.UUID;

public interface IUserService {
    void create(User user);
    User read(UUID id);
    void update(User user);
    void delete(UUID id);
    List<User> findAll();
}