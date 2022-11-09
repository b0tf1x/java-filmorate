package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private int id = 1;
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        log.info("Текущее количество постов " + users.size());
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) throws ValidationException {

        validate(user);
        if (checkUsers(user)) {
            user.setId(id++);
            users.put(user.getId(), user);
            log.info("Пользователь " + user.getLogin() + " добавлен");
            return user;
        } else {
            log.warn(user.getLogin());
            throw new ValidationException("Такой пользователь уже добавлен");
        }
    }

    public boolean checkUsers(User user) {
        Collection<User> collectionUsers = users.values();
        boolean check = collectionUsers.stream()
                .anyMatch(user1 -> user1.getLogin().equals(user.getLogin()) || user1.getEmail().equals(user.getEmail()));
        if (check) {
            return false;
        }
        return true;
    }


    @PutMapping
    public User put(@RequestBody User user) throws ValidationException {
        validate(user);
        if (users.containsKey(user.getId())) {
            users.remove(user.getId());
            users.put(user.getId(), user);
            log.info("Информация о пользователе обновлена");
            return user;
        } else {
            log.warn("Нет такого пользователя");
            log.info(user.getLogin());
            throw new ValidationException("Нет такого пользователя");
        }
    }

    private void validate(@Valid @RequestBody User user) throws ValidationException {
        if (user.getLogin().contains(" ")) {
            log.warn("User login = " + user.getId());
            throw new ValidationException("Логин содержит пробел");
        }
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
    }
}
