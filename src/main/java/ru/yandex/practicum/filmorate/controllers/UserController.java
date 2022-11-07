package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/users")
public class UserController {
    private int id = 1;
    private final Map<Integer,User> users = new HashMap<>();
    @GetMapping
    public Collection<User> findAll(){
        log.info("Текущее количество постов "+users.size());
        return users.values();
    }
    @PostMapping
    public User create(@Valid @RequestBody User user) throws ValidationException {

        validate(user);
        if (!users.containsKey(user.getId())) {
            user.setId(id++);
            users.put(user.getId(), user);
            log.info("Пользователь " + user.getLogin() + " добавлен");
            return user;
        } else {
            log.warn(user.getLogin());
            throw new ValidationException("Такой пользователь уже добавлен");
        }
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
    public static void validate(@Valid @RequestBody User user) throws ValidationException {
        if (user.getLogin().contains(" ")){
            log.warn("User login = "+user.getLogin());
            throw new ValidationException("Логин содержит пробел");
        }
        if (user.getName()==null){
            user.setName(user.getLogin());
        }
    }
}
