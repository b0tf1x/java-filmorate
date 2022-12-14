package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FilmControllerTest {
    private final FilmController filmController = new FilmController();

    @Test
    public void testDuration() {
        Film film = new Film("abc", "123", LocalDate.of(2022, 10, 10), 0);
        film.setId(1);
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.create(film));
        assertEquals(ValidationException.class, exception.getClass());
    }

    @Test
    public void testDate() {
        Film film = new Film("abc", "123", LocalDate.of(1895, 12, 27), 10);
        film.setId(1);
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.create(film));
        assertEquals(ValidationException.class, exception.getClass());
    }
}