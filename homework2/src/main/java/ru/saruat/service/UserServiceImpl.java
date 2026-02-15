package ru.saruat.service;

import ru.saruat.dao.IUserDao;
import ru.saruat.dao.UserDaoImpl;
import ru.saruat.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserServiceImpl implements IUserService {

    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    private IUserDao userDao = new UserDaoImpl();

    // Только для тестов!
    protected void setUserDaoForTesting(IUserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void create(User user) {
        try {
            userDao.create(user);
        } catch (Exception ex){
            logger.error("Ошибка при создании пользователя:{}", ex.getMessage(), ex);
        }
    }

    @Override
    public User read(UUID id) {
        try {
            Optional<User> userOptional = userDao.read(id);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                return user;
            } else {
                return null;            }
        } catch (Exception ex){
            logger.error("Ошибка при чтении пользователя:{}", ex.getMessage(), ex);
            return null;
        }
    }

    @Override
    public void update(User user) {
        try {
            userDao.update(user);
        } catch (Exception ex){
            logger.error("Ошибка при изменении пользователя:{}", ex.getMessage(), ex);
        }
    }

    @Override
    public void delete(UUID id) {
        try {
            userDao.delete(id);
        } catch (Exception ex){
            logger.error("Ошибка при удалении пользователя:{}", ex.getMessage(), ex);
        }
    }

    @Override
    public List<User> findAll() {
        try {
            return userDao.findAll();
        } catch (Exception ex) {
            logger.error("Ошибка при получении списка пользователей: {}", ex.getMessage(), ex);
            return Collections.emptyList();
        }
    }
}