package ru.khusnullin.SpringBootUpdate2.service;

import ru.khusnullin.SpringBootUpdate2.model.User;

import java.util.List;

public interface UserService {
    void addUser(User user);
    void deleteUser(Long id);
    User getUserById(Long id);
    User getUserByEmail(String email);
    void updateUser(User user);
    List<User> listUsers();
}
