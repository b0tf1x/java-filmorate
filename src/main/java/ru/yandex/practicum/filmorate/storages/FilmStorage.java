package ru.yandex.practicum.filmorate.storages;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface FilmStorage {
    Collection<Film> findAll();

    Film create(Film film);

    Film put(Film film);

    Film getById(int id);

    Film deleteById(int id);
    Map<Integer, Film> getFilms();
    Film addLike(int userId, int filmId);
    Film removeLike(int filmId, int userId);
    List<Film> getTop(int count);
}
