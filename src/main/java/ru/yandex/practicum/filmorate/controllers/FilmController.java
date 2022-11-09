package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private static final LocalDate MIN_DATE = LocalDate.of(1895, 12, 28);
    private int id = 1;
    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        log.info("Текущее количество постов " + films.size());
        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) throws ValidationException {
        validate(film);
        if (checkFilm(film)) {
            film.setId(id++);
            films.put(film.getId(), film);
            log.info("Фильм " + film.getName() + " добавлен");
            return film;
        } else {
            log.warn(film.getName());
            throw new ValidationException("Такой фильм уже добавлен");
        }

    }

    public boolean checkFilm(Film film) {
        Collection<Film> collectionFilms = films.values();
        boolean check = collectionFilms.stream()
                .anyMatch(film1 -> film1.getName().equals(film.getName()));
        if (check) {
            return false;
        }
        return true;
    }

    @PutMapping
    public Film put(@RequestBody Film film) throws ValidationException {
        validate(film);
        if (films.containsKey(film.getId())) {
            films.remove(film.getId());
            films.put(film.getId(), film);
            log.info("Информация о фильме обновлена");
            return film;
        } else {
            log.warn(film.getName());
            throw new ValidationException("Нет такого фильма");
        }
    }

    private void validate(@Valid @RequestBody Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(MIN_DATE) || film.getDuration() <= 0) {
            log.warn("Дата " + film.getReleaseDate() + " / Длительность " + film.getDuration());
            throw new ValidationException("Ошибка даты/длительности");
        }
    }
}
