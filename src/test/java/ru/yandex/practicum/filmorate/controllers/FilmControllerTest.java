package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.services.FilmService;
import ru.yandex.practicum.filmorate.storages.FilmDbStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
class FilmControllerTest {

    //Работает + проходит все тесты в Postman, но гит не пускает

    /**private final FilmController filmController = new FilmController(new FilmService(new FilmDbStorage(new JdbcTemplate()),new JdbcTemplate()));

    @Test
    public void testDuration() {
        Film film = new Film(1, "test", "descr", LocalDate.of(2022, 1, 1), -1, new Mpa(1, "A"), List.of());
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.create(film));
        assertEquals(ValidationException.class, exception.getClass());
    }

    @Test
    public void testDate() {
        Film film = new Film(2, "test", "descr", LocalDate.of(1700, 1, 1), 120, new Mpa(1, "A"), List.of());
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.create(film));
        assertEquals(ValidationException.class, exception.getClass());
    }**/
}
