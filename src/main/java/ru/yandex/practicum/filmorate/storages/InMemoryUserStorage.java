package ru.yandex.practicum.filmorate.storages;

import com.sun.jdi.InternalException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private int id = 1;
    @Getter
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public Collection<User> findAll() {
        log.info("Текущее количество постов " + users.size());
        return users.values();
    }

    @Override
    public User create(User user) {
        validate(user);
        checkUsers(user);
        user.setId(id++);
        users.put(user.getId(), user);
        log.info("Пользователь " + user.getLogin() + " добавлен");
        return user;

    }

    @Override
    public User getUserById(int id) {
        if (!users.containsKey(id)) {
            throw new UserException("Такого пользователя не существует");
        }
        return users.get(id);
    }

    @Override
    public User deleteUserById(int id) {
        if (!users.containsKey(id)) {
            throw new UserException("Невозможно удалить пользователя");
        }
        users.remove(id);
        return users.get(id);
    }

    private boolean checkUsers(User user) {
        if (!users.values().stream()
                .noneMatch(userToCompare -> userToCompare.getName().equals(user.getName())
                        || userToCompare.getEmail().equals(user.getEmail()))) {
            throw new InternalException("Такой пользователь уже есть");
        }
        return users.values().stream()
                .noneMatch(userToCompare -> userToCompare.getName().equals(user.getName())
                        || userToCompare.getEmail().equals(user.getEmail()));
    }

    @Override
    public User put(User user) {
        if (!users.containsKey(user.getId())) {
            throw new UserException("Нет такого пользователя");
        }
        validate(user);
        users.put(user.getId(), user);
        log.info("Информация о пользователе обновлена");
        return user;
    }

    private void validate(User user) {
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Логин содержит пробел");
        }
        if (user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
