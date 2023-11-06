package ru.practicum.shareit.user.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public User addUser(User user) {
        if (user.getEmail() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User getUser(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден"));
    }

    @Override
    @Transactional
    public User updateUser(User user, Long id) {
        Optional<User> oldUser = userRepository.findById(id);
        user.setId(id);
        if (user.getEmail() == null) {
            user.setEmail(oldUser.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)).getEmail());
        }
        if (user.getName() == null) {
            user.setName(oldUser.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)).getName());
        }
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public List<User> getAllUser() {
        return userRepository.findAll();
    }
}
