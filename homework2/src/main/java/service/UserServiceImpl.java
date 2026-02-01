package service;

import dao.IUserDao;
import dao.UserDaoImpl;
import entity.User;

import java.util.List;
import java.util.UUID;

public class UserServiceImpl implements IUserService {

    private final IUserDao userDao = new UserDaoImpl();

    @Override
    public void create(User user) {
        userDao.create(user);
    }

    @Override
    public User read(UUID id) {
        return userDao.read(id);
    }

    @Override
    public void update(User user) {
        userDao.update(user);
    }

    @Override
    public void delete(UUID id) {
        userDao.delete(id);
    }

    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }
}