package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    @Test
    public void testLoginWithSpace(){
    User user = new User();
    user.setLogin("123 432");
    user.setName("abc");
    user.setId(1);
    user.setEmail("@123");
    user.setBirthday(LocalDate.of(2020,10,10));
    final ValidationException exception = assertThrows(
          ValidationException.class,
            () -> UserController.validate(user));
    assertEquals(ValidationException.class, exception.getClass());
}
    @Test
    public void testNullName(){
        User user = new User();
        user.setLogin("123 432");
        user.setId(1);
        user.setEmail("@123");
        try {
            UserController.validate(user);
            assertEquals(user.getLogin(),user.getName());
        } catch (Exception e){}
    }
    @Test
    public void testNull(){

        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> UserController.validate(null));
        assertEquals(NullPointerException.class, exception.getClass());
    }
}