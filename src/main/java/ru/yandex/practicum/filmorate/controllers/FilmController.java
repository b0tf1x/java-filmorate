package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;


import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/films")
public class FilmController {
    private static final LocalDate MIN_DATE = LocalDate.of(1895, 12, 28);
    private int id = 1;
private Map<Integer,Film> films = new HashMap<>();
    @GetMapping
     public Collection<Film> findAll(){
        log.info("Текущее количество постов "+films.size());
        return films.values();
    }
    @PostMapping
    public Film create(@Valid @RequestBody Film film){
       try {
           validate(film);
           if (!films.containsKey(film.getId())) {
               film.setId(id++);
               films.put(film.getId(), film);
               log.info("Фильм " + film.getName() + " добавлен");
           } else {
               log.info("Такой фильм уже добавлен");

           }
       } catch (ValidationException e){
           log.info("Ошибка при добавлении фильма");
       }
        return film;
    }

    @PutMapping
        public Film put(@RequestBody Film film){
        try {
            validate(film);
            if (films.containsKey(film.getId())) {
                films.remove(film.getId());
                films.put(film.getId(), film);
                log.info("Информация о фильме обновлена");
            } else {
                log.info("Нет такого фильма");
            }
        } catch (ValidationException e){
            log.info("Ошибка обновления данных о фильме");
        }
        return film;
    }
    public static void validate(@Valid @RequestBody Film film) throws ValidationException {
      if (film.getReleaseDate().isBefore(MIN_DATE) || film.getDuration()<=0){
          log.info("Дата "+film.getReleaseDate()+" / Длительность "+film.getDuration());
          throw new ValidationException("Ошибка даты/длительности");
      }
    }
}