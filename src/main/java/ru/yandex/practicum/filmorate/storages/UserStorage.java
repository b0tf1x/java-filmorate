package ru.yandex.practicum.filmorate.storages;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    Collection<User> findAll();

    User create(User user);

    User put(User user);

    User deleteUserById(int id);

    User getUserById(int id);
}
