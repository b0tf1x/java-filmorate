package ru.yandex.practicum.filmorate.services;

import com.sun.jdi.InternalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storages.InMemoryUserStorage;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();

    public Collection<User> findAll() {
        return inMemoryUserStorage.findAll();
    }

    public User create(User user) {
        return inMemoryUserStorage.create(user);
    }

    public User put(User user) {
        return inMemoryUserStorage.put(user);
    }

    public User getUserById(int id) {
        return inMemoryUserStorage.getUserById(id);
    }

    public User deleteUserById(int id) {
        return inMemoryUserStorage.deleteUserById(id);
    }

    public List<User> addFriend(int firstId, int secondId) {
        if (!inMemoryUserStorage.getUsers().containsKey(firstId) || !inMemoryUserStorage.getUsers().containsKey(secondId)) {
            throw new UserException("Невозможно добавить в друзья, нет таких пользователей");
        }
        if (inMemoryUserStorage.getUserById(firstId).getFriends().contains(secondId)
                || inMemoryUserStorage.getUserById(secondId).getFriends().contains(firstId)) {
            throw new InternalException("Невозможно добавить в друзья, они уже друзья");
        }
        inMemoryUserStorage.getUserById(firstId).add(secondId);
        inMemoryUserStorage.getUserById(secondId).add(firstId);
        log.info("Пользователи " + firstId + " и " + secondId + "теперь друзья");
        return Arrays.asList(inMemoryUserStorage.getUserById(firstId), inMemoryUserStorage.getUserById(secondId));
    }

    public List<User> removeFriend(int firstId, int secondId) {
        if (!inMemoryUserStorage.getUsers().containsKey(firstId) || !inMemoryUserStorage.getUsers().containsKey(secondId)) {
            throw new UserException("Невозможно удалить из друзей, нет таких пользователей");
        }
        if (!inMemoryUserStorage.getUserById(firstId).getFriends().contains(secondId)
                || !inMemoryUserStorage.getUserById(secondId).getFriends().contains(firstId)) {
            throw new InternalException("Невомозжно удалить из друзей, они не в друзьях");
        }
        inMemoryUserStorage.getUserById(firstId).remove(secondId);
        inMemoryUserStorage.getUserById(secondId).remove(firstId);
        log.info("Удаление из друзей успешно");
        return Arrays.asList(inMemoryUserStorage.getUserById(firstId), inMemoryUserStorage.getUserById(secondId));
    }

    public List<User> getFriends(int id) {
        if (!inMemoryUserStorage.getUsers().containsKey(id)) {
            throw new UserException("Нет такого пользовтеля");
        }
        return inMemoryUserStorage.getUserById(id).getFriends().stream()
                .map(inMemoryUserStorage::getUserById)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(int firstId, int secondId) {
        if (!inMemoryUserStorage.getUsers().containsKey(firstId) || !inMemoryUserStorage.getUsers().containsKey(secondId)) {
            throw new UserException("Такого(их) пользователя(ей) не существует");
        }

        log.info("Список общих друзей успешно выведен");
        return inMemoryUserStorage.getUserById(firstId).getFriends().stream()
                .filter(friendId -> inMemoryUserStorage.getUserById(secondId).getFriends().contains(friendId))
                .map(inMemoryUserStorage::getUserById)
                .collect(Collectors.toList());
    }
}
