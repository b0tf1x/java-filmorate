package ru.yandex.practicum.filmorate.storages;

import com.sun.jdi.InternalException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private static final LocalDate MIN_DATE = LocalDate.of(1895, 12, 28);
    private int id = 1;
    @Getter
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public Collection<Film> findAll() {
        return films.values();
    }

    @Override
    public Film create(Film film) {
        validate(film);
        checkFilm(film);
        film.setId(id++);
        films.put(film.getId(), film);
        log.info("Фильм " + film.getName() + " добавлен");
        return film;
    }

    @Override
    public Film getById(int id) {
        if (!films.containsKey(id)) {
            throw new FilmException("Нет такого фильма");
        }
        return films.get(id);
    }

    @Override
    public Film deleteById(int id) {
        if (!films.containsKey(id)) {
            throw new FilmException("Невозможно удалить, нет такого фильма");
        }
        films.remove(id);
        return films.get(id);
    }

    private boolean checkFilm(Film film) {
        if (films.values().stream()
                .anyMatch(filmToCompare -> filmToCompare.getName().equals(film.getName()))) {
            throw new InternalException("Такой фильм уже есть");
        }
        return !films.values().stream()
                .anyMatch(filmToCompare -> filmToCompare.getName().equals(film.getName()));
    }

    @Override
    public Film put(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new FilmException("Нет такого фильма");
        }
        validate(film);
        films.put(film.getId(), film);
        log.info("Информация о фильме обновлена");
        return film;
    }

    private void validate(Film film) {
        if (film.getReleaseDate().isBefore(MIN_DATE) || film.getDuration() <= 0) {
            throw new ValidationException("Ошибка даты/длительности");
        }
    }
}
