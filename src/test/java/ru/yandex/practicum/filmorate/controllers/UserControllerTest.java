package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
class UserControllerTest {
    //без бд не проходит тесты
    @Autowired
    private UserController userController;

    @Test
    public void testLoginWithSpace() {
        User user = new User(1, "email", "log in", "name", LocalDate.of(2010, 10, 10));
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userController.create(user));
        assertEquals(ValidationException.class, exception.getClass());
    }

    @Test
    public void testNullName() throws ValidationException {
        User user = new User(1, "email", "login", null, LocalDate.of(2010, 10, 10));
        user = userController.create(user);
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
}
