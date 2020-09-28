package ru.lis154.SpringBootSec.service;

import ru.lis154.SpringBootSec.model.User;

import java.util.List;

public interface UserService {
    List<User> allUser(int page);
    void add(User user);
    void delete(int id);
    void edit(User user);
    User getById(int id);
    public int userCount();
}
