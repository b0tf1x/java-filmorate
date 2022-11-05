package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    @Test
    public void testDuration(){
       Film film = new Film("abc","123",LocalDate.of(2022,10,10),0);
       film.setId(1);
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> FilmController.validate(film));
        assertEquals(ValidationException.class, exception.getClass());
    }
    @Test
    public void testDate(){
        Film film = new Film("abc","123",LocalDate.of(1895, 12, 27),10);
        film.setId(1);
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> FilmController.validate(film));
        assertEquals(ValidationException.class, exception.getClass());
    }
}