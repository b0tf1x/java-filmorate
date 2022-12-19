package ru.yandex.practicum.filmorate.storages;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface UserStorage {
    Collection<User> findAll();

    User create(User user);

    User put(User user);

    User deleteUserById(int id);

    User getUserById(int id);

    Map<Integer, User> getUsers();

    List<Integer> addFriend(int firstId, int secondId);

    List<Integer> removeFriend(int firstId, int secondId);

    List<User> getFriends(int id);

    List<User> getCommonFriends(int firstId, int secondId);
}
