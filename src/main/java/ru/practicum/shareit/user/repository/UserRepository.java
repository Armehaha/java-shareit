package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {
    User addUser(User user);

    User getUser(Long id);

    User updateUser(User user);

    void deleteUserById(Long id);

    List<User> getAllUser();
}
