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
    public User create(@Valid @RequestBody User user){
        try {
            validate(user);
            if (!users.containsKey(user.getId())) {
                user.setId(id++);
                users.put(user.getId(), user);
                log.info("Пользователь " + user.getLogin() + " добавлен");
            } else{
                log.info("Такой пользователь уже добавлен");
            }
        } catch (Exception e){
            log.info("Ошибка добавления пользователя");
        }
        return user;
    }

    @PutMapping
    public User put(@RequestBody User user){
        try {
            validate(user);
            if (users.containsKey(user.getId())) {
                users.remove(user.getId());
                users.put(user.getId(), user);
                log.info("Информация о пользователе обновлена");
            } else {
                log.info("Нет такого пользователя");
            }
        } catch (ValidationException e){
            log.info("Ошибка при обновлении данных пользователя");
        }
        return user;
    }
    public static void validate(@Valid @RequestBody User user) throws ValidationException {
        if (user.getLogin().contains(" ")){
            log.info("User login = "+user.getLogin());
            throw new ValidationException("Логин содержит пробел");
        }
        if (user.getName()==null){
            user.setName(user.getLogin());
        }
    }
}
