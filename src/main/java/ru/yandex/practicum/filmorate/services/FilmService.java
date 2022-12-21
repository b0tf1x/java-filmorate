package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.JdbcAccessor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmException;
import ru.yandex.practicum.filmorate.exceptions.UserException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storages.FilmStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film create(Film film) {
        validate(film);
        return filmStorage.create(film);
    }

    public Film getById(int id) {
        return filmStorage.getById(id);
    }

    public Film deleteById(int id) {
        return filmStorage.deleteById(id);
    }

    public Film put(Film film) {
        validate(film);
        return filmStorage.put(film);
    }

    public Film addLike(int userId, int filmId) {
        validate(userId,filmId);
        return filmStorage.addLike(userId, filmId);
    }

    public Film removeLike(int userId, int filmId) {
        validate(userId,filmId);
        return filmStorage.removeLike(userId, filmId);
    }

    public List<Film> getTop(int count) {
        return filmStorage.getTop(count);
    }
    private void validate(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Недопустимая дата");
        }
        if (film.getDuration()<0){
            throw new ValidationException("Недопустимая длительность");
        }
    }
    private final JdbcTemplate jdbcTemplate;
    private void validate(int filmId, int userId) {
        final String checkExistsFilm = "select * from films where id = ?";
        SqlRowSet filmRowSet = jdbcTemplate.queryForRowSet(checkExistsFilm, filmId);
        final String checkExistsUser = "select * from users where id = ?";
        SqlRowSet userRowSet = jdbcTemplate.queryForRowSet(checkExistsUser, userId);
        if (!userRowSet.next()) {
            throw new UserException("Пользователь не найден");
        }
        if (!filmRowSet.next()) {
            throw new FilmException("Фильм не найден");
        }
    }
}
