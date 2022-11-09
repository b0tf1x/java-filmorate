package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    UserController userController = new UserController();

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
    public void testNullName() {
        User user = new User("@123", "123", LocalDate.of(2010, 10, 10));
        user.setId(1);
        try {
            userController.create(user);
            assertEquals(user.getLogin(), user.getName());
        } catch (Exception e) {
        }
    }

    @Test
    public void testNull() {
        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> userController.create(null));
        assertEquals(NullPointerException.class, exception.getClass());
    }
}