package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    @Test
    public void testDuration(){
       Film film = new Film();
       film.setDuration(0);
       film.setDescription("123");
       film.setName("abc");
       film.setId(1);
       film.setReleaseDate(LocalDate.of(2022,10,10));
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> FilmController.validate(film));
        assertEquals(ValidationException.class, exception.getClass());
    }
    @Test
    public void testDate(){
        Film film = new Film();
        film.setDuration(10);
        film.setDescription("123");
        film.setName("abc");
        film.setId(1);
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> FilmController.validate(film));
        assertEquals(ValidationException.class, exception.getClass());
    }
}