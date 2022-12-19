package ru.yandex.practicum.filmorate.storages;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Map;

public interface FilmStorage {
    Collection<Film> findAll();

    Film create(Film film);

    Film put(Film film);

    Film getById(int id);

    Film deleteById(int id);
    Map<Integer, Film> getFilms();
}
