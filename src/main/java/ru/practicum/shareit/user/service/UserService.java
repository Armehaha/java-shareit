package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    User addUser(User user);

    User getUser(Long id);

    User updateUser(User user, Long id);

    void deleteUserById(Long id);

    List<User> getAllUser();

}
