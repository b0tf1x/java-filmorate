/**package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storages.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storages.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserControllerTest {
    private final UserController userController = new UserController();

    @Test
    public void testLoginWithSpace() {
        User user = new User("@123", "123 432", LocalDate.of(2010, 10, 10));
        user.setName("abc");
        user.setId(1);
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userController.create(user));
        assertEquals(ValidationException.class, exception.getClass());
    }

    @Test
    public void testNullName() throws ValidationException {
        InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
        User user = new User("@123", "123", LocalDate.of(2010, 10, 10));
        user.setId(1);
        user = inMemoryUserStorage.validate(user);
        userController.create(user);
        assertEquals(user.getLogin(), user.getName());
    }

    @Test
    public void testNull() {
        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> userController.create(null));
        assertEquals(NullPointerException.class, exception.getClass());
    }
}**/