package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storages.InMemoryFilmStorage;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {
    InMemoryFilmStorage inMemoryFilmStorage = new InMemoryFilmStorage();

    public Collection<Film> findAll() {
        return inMemoryFilmStorage.findAll();
    }

    public Film create(Film film) {
        return inMemoryFilmStorage.create(film);
    }

    public Film getById(int id) {
        return inMemoryFilmStorage.getById(id);
    }

    public Film deleteById(int id) {
        return inMemoryFilmStorage.deleteById(id);
    }

    public Film put(Film film) {
        return inMemoryFilmStorage.put(film);
    }

    public Film addLike(int userId, int filmId) {
        if (!inMemoryFilmStorage.getFilms().containsKey(filmId)) {
            throw new FilmException("Невозможно поставить лайк, фильм не существует");
        }
        inMemoryFilmStorage.getById(filmId).add(userId);
        log.info("Лайк успешно поставлен");
        return inMemoryFilmStorage.getById(filmId);
    }

    public Film removeLike(int userId, int filmId) {
        if (!inMemoryFilmStorage.getFilms().containsKey(filmId)) {
            throw new FilmException("Фильм не существует");
        }
        if (!inMemoryFilmStorage.getById(filmId).getLikes().contains(userId)) {
            throw new FilmException("Нет лайка от пользователя с id = ");
        }
        inMemoryFilmStorage.getById(filmId).remove(userId);
        log.info("Лайк успешно удален");
        return inMemoryFilmStorage.getById(filmId);

    }

    public List<Film> getTop10(int count) {
        log.info("Список успешно получен");
        return inMemoryFilmStorage.findAll().stream()
                .sorted((o1, o2) -> Integer.compare(o2.getLikes().size(), o1.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }
}
