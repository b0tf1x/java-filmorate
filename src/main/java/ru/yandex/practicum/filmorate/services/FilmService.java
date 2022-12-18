package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storages.FilmStorage;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {
   FilmStorage filmStorage;

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film getById(int id) {
        return filmStorage.getById(id);
    }

    public Film deleteById(int id) {
        return filmStorage.deleteById(id);
    }

    public Film put(Film film) {
        return filmStorage.put(film);
    }

    public Film addLike(int userId, int filmId) {
        return filmStorage.addLike(userId,filmId);
    }

    public Film removeLike(int userId, int filmId) {
    return filmStorage.removeLike(userId,filmId);
    }

    public List<Film> getTop(int count) {
        return filmStorage.getTop(count);
    }
}
