package ru.yandex.practicum.filmorate.storages;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Map;

public interface UserStorage {
    Collection<User> findAll();

    User create(User user);

    User put(User user);

    User deleteUserById(int id);

    User getUserById(int id);
    Map<Integer,User> getUsers();
}
