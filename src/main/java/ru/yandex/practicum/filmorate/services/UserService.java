package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storages.UserStorage;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User create(User user) {
        validate(user);
        return userStorage.create(user);
    }

    public User put(User user) {
        validate(user);
        return userStorage.put(user);
    }

    public User getUserById(int id) {
        return userStorage.getUserById(id);
    }

    public User deleteUserById(int id) {
        return userStorage.deleteUserById(id);
    }

    public List<Integer> addFriend(int firstId, int secondId) {
        validate(firstId,secondId);
        return userStorage.addFriend(firstId, secondId);
    }

    public List<Integer> removeFriend(int firstId, int secondId) {
        validate(firstId,secondId);
        return userStorage.removeFriend(firstId, secondId);
    }

    public List<User> getFriends(int id) {
        return userStorage.getFriends(id);
    }

    public List<User> getCommonFriends(int firstId, int secondId) {
        validate(firstId,secondId);
        return userStorage.getCommonFriends(firstId, secondId);
    }

    private void validate(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if(user.getLogin().contains(" ")){
            throw new ValidationException("Неправильный логин");
        }
    }
    private final JdbcTemplate jdbcTemplate;
    private void validate(int firstId, int secondId) {
        final String checkExists = "select * from users where id = ?";
        SqlRowSet firstRowSet = jdbcTemplate.queryForRowSet(checkExists, firstId);
        SqlRowSet secondRowSet = jdbcTemplate.queryForRowSet(checkExists, secondId);
        if (!firstRowSet.next() || !secondRowSet.next()) {
            throw new UserException("Один или несколько пользователей не найден(ы)");
        }
    }
}
