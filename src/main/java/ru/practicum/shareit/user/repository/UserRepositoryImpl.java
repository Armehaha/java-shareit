package ru.practicum.shareit.user.repository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Component
public class UserRepositoryImpl implements UserRepository {
    private final Map<Long, User> userMap = new HashMap<>();
    private final Set<String> stringSet = new HashSet<>();
    private Long id = 0L;

    @Override
    public User addUser(User user) {
        if (stringSet.contains(user.getEmail())) {
            throw new IllegalArgumentException();
        }
        stringSet.add(user.getEmail());
        user.setId(++id);
        userMap.put(user.getId(), user);
        return userMap.get(user.getId());
    }

    @Override
    public User getUser(Long id) {
        if (!userMap.containsKey(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден");
        }
        return userMap.get(id);
    }

    @Override
    public User updateUser(User user) {
        if (!userMap.containsKey(user.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Пользователь не найден");
        }

        User olduser = userMap.get(user.getId());
        if (stringSet.contains(user.getEmail()) && !user.getEmail().equals(olduser.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "такой email  уже есть");
        }
        if (user.getEmail() != null && !stringSet.contains(user.getEmail())) {
            stringSet.remove(olduser.getEmail());
            stringSet.add(user.getEmail());
            olduser.setEmail(user.getEmail());
        }
        if (user.getName() != null) {
            olduser.setName(user.getName());
        }
        userMap.put(olduser.getId(), olduser);
        return userMap.get(olduser.getId());
    }

    @Override
    public void deleteUserById(Long id) {
        stringSet.remove(userMap.get(id).getEmail());
        userMap.remove(id);
    }

    @Override
    public List<User> getAllUser() {

        return new ArrayList<>(userMap.values());
    }

}
